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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * Tests for {@link LspJsonRpcDecoder}.
 *
 * @author Janne Valkealahti
 *
 */
public class LspJsonRpcDecoderTests {

	private static final byte[] CONTENT1 = createContent("{\"jsonrpc\": \"2.0\",\"id\": 100}");
	private static final byte[] CONTENT2 = createContent("{\"jsonrpc\": \"2.0\",\"id\": 200}");
	private static final byte[] CONTENT3 = createContent("{\"jsonrpc\": \"2.0\",\"id\": 100, \"method:\"" + string(800) + "\"}");
	private static final byte[] CONTENT4 = createContent("{\"jsonrpc\": \"2.0\",\"id\": 100, \"method:\"" + string(1000) + "\"}");

	private static byte[] createContent(String... lines) {
		StringBuilder buf = new StringBuilder();
		for (String line : lines) {
			buf.append(line);
			buf.append("\r\n");
		}
		String message = "Content-Length: " + buf.length() + "\r\n\r\n" + buf.toString();
		return message.getBytes();
	}

	private static String string(int length) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			buf.append("x");
		}
		return buf.toString();
	}

	@Test
	public void testSingleRequest() {
		testSingleRequestAtOnce(CONTENT1);
		testSingleRequestByteAtTime(CONTENT1);
		testSingleRequestAtOnce(CONTENT3);
	}

	@Test
	public void testSingleLongRequest() {
		testSingleLongRequestAtOnce(CONTENT3, 800);
		testSingleLongRequestByteAtTime(CONTENT3, 800);
		testSingleLongRequestAtOnce(CONTENT4, 1000);
		testSingleLongRequestByteAtTime(CONTENT4, 1000);
	}

	@Test
	public void testDoubleRequest() throws Exception {
		testDoubleRequestAtOnce(concat(CONTENT1, CONTENT2));
	}

	private static void testSingleRequestAtOnce(byte[] content) {
        EmbeddedChannel channel = new EmbeddedChannel(new LspJsonRpcDecoder());
        assertThat(channel.writeInbound(Unpooled.wrappedBuffer(content))).isTrue();
        String req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("100");
        assertThat(channel.finish()).isFalse();

        Object readInbound = channel.readInbound();
        assertThat(readInbound).isNull();
	}

	private static void testSingleLongRequestAtOnce(byte[] content, int lentgh) {
        EmbeddedChannel channel = new EmbeddedChannel(new LspJsonRpcDecoder());
        assertThat(channel.writeInbound(Unpooled.wrappedBuffer(content))).isTrue();
        String req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("100");
        String xxx = string(lentgh);
        assertThat(req).contains(xxx);
        assertThat(channel.finish()).isFalse();

        Object readInbound = channel.readInbound();
        assertThat(readInbound).isNull();
	}

	private static void testSingleRequestByteAtTime(byte[] content) {
        EmbeddedChannel channel = new EmbeddedChannel(new LspJsonRpcDecoder());
        for (byte b : content) {
			byte[] bytes = new byte[] { b };
			channel.writeInbound(Unpooled.wrappedBuffer(bytes));
        }
        String req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("100");
        assertThat(channel.finish()).isFalse();

        Object readInbound = channel.readInbound();
        assertThat(readInbound).isNull();
	}

	private static void testSingleLongRequestByteAtTime(byte[] content, int lentgh) {
        EmbeddedChannel channel = new EmbeddedChannel(new LspJsonRpcDecoder());
        for (byte b : content) {
			byte[] bytes = new byte[] { b };
			channel.writeInbound(Unpooled.wrappedBuffer(bytes));
        }
        String req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("100");
        String xxx = string(lentgh);
        assertThat(req).contains(xxx);
        assertThat(channel.finish()).isFalse();

        Object readInbound = channel.readInbound();
        assertThat(readInbound).isNull();
	}

	private static void testDoubleRequestAtOnce(byte[] content) {
        EmbeddedChannel channel = new EmbeddedChannel(new LspJsonRpcDecoder());
        assertThat(channel.writeInbound(Unpooled.wrappedBuffer(content))).isTrue();
        String req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("100");
        assertThat(req).doesNotContain("200");

        req = channel.readInbound();
        assertThat(req).isNotNull();
        assertThat(req).isNotNull();
        assertThat(req).contains("2.0");
        assertThat(req).contains("200");

        assertThat(channel.finish()).isFalse();
        Object readInbound = channel.readInbound();
        assertThat(readInbound).isNull();
	}

	private static byte[] concat(byte[]... arrays) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		for (byte[] array : arrays) {
			outputStream.write(array);
		}
		return outputStream.toByteArray();
	}
}
