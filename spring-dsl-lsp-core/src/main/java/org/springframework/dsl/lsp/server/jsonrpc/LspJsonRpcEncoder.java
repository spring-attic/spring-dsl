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

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class LspJsonRpcEncoder extends MessageToMessageEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		int readableBytes = msg.readableBytes();
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeCharSequence("Content-Length: ", Charset.defaultCharset());
		buf.writeCharSequence(Integer.toString(readableBytes), Charset.defaultCharset());
		buf.writeCharSequence("\r\n", Charset.defaultCharset());
		buf.writeCharSequence("\r\n", Charset.defaultCharset());
		buf.writeBytes(msg);
		out.add(buf);
	}

}
