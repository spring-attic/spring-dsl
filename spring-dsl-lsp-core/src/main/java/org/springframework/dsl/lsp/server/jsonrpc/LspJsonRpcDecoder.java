/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.lsp.server.jsonrpc;

import static io.netty.buffer.ByteBufUtil.readBytes;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dsl.lsp.server.jsonrpc.LspJsonRpcDecoder.State;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.AppendableCharSequence;

public class LspJsonRpcDecoder extends ReplayingDecoder<State> {

	private static final byte CR = 13;
	private static final byte LF = 10;
	private static final byte COLON = 58;
	private static final String CONTENT_LENGTH = "Content-Length";

	private final int maxLineLength;
//	private final int maxChunkSize;
	private long contentLength = -1;
	private String lastContent;
	private int alreadyReadChunkSize;

	enum State {
		READ_HEADERS, READ_CONTENT, FINALIZE_FRAME_READ;
	}

	public LspJsonRpcDecoder() {
		super(State.READ_HEADERS);
		this.maxLineLength = 128;
//		this.maxChunkSize = 1024;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (state()) {
			case READ_HEADERS:
				Map<String, String> headers = new HashMap<>();
				checkpoint(readHeaders(in, headers));
				break;
			case READ_CONTENT:
                int toRead = in.readableBytes();
                if (toRead == 0) {
                    return;
                }
                if (contentLength > -1) {
                	toRead = (int)contentLength;
                }
//                if (toRead > maxChunkSize) {
//                    toRead = maxChunkSize;
//                }
                if (contentLength >= 0) {
                    int remainingLength = (int) (contentLength - alreadyReadChunkSize);
                    if (toRead > remainingLength) {
                        toRead = remainingLength;
                    }
                    ByteBuf chunkBuffer = readBytes(ctx.alloc(), in, toRead);
                    if ((alreadyReadChunkSize += toRead) >= contentLength) {
                    	lastContent = chunkBuffer.retain().duplicate().toString(Charset.defaultCharset());
                        checkpoint(State.FINALIZE_FRAME_READ);
                    } else {
                    	String payload = chunkBuffer.retain().duplicate().toString(Charset.defaultCharset());
                    	out.add(payload);
                        return;
                    }
                }
			case FINALIZE_FRAME_READ:
				out.add(lastContent);
				resetDecoder();
			default:
				break;
		}
	}

    private State readHeaders(ByteBuf buffer, Map<String, String> headers) {
        AppendableCharSequence buf = new AppendableCharSequence(128);
        for (;;) {
            boolean headerRead = readHeader(headers, buf, buffer);
            if (!headerRead) {
                if (headers.containsKey(CONTENT_LENGTH)) {
                    contentLength = getContentLength(headers, 0);
//                    if (contentLength == 0) {
//                        return State.FINALIZE_FRAME_READ;
//                    }
                }
                return State.READ_CONTENT;
            }
        }
    }

    private long getContentLength(Map<String, String> headers, long defaultValue) {
    	String value = headers.get(CONTENT_LENGTH);
    	if (value != null) {
    		return Long.parseLong(value);
    	}
    	// TODO: handle parse error and negative value
        return contentLength;
    }

	private void invalidLineLength() {
        throw new TooLongFrameException("An JSRPC line is larger than " + maxLineLength + " bytes.");
    }

    private boolean readHeader(Map<String, String> headers, AppendableCharSequence buf, ByteBuf buffer) {
        buf.reset();
        int lineLength = 0;
        String key = null;
        boolean valid = false;

        for (;;) {
            byte nextByte = buffer.readByte();

            if (nextByte == COLON && key == null) {
                key = buf.toString();
                valid = true;
                buf.reset();
            } else if (nextByte == CR) {
                //do nothing
            } else if (nextByte == LF) {
                if (key == null && lineLength == 0) {
                    return false;
                } else if (valid) {
                    headers.put(key, buf.toString().trim());
                }
                return true;
            } else {
                if (lineLength >= maxLineLength) {
                    invalidLineLength();
                }
                if (nextByte == COLON && key != null) {
                    valid = false;
                }
                lineLength ++;
                buf.append((char) nextByte);
            }
        }
    }

	private void resetDecoder() {
        checkpoint(State.READ_HEADERS);
        contentLength = -1;
        alreadyReadChunkSize = 0;
        lastContent = null;
    }
}
