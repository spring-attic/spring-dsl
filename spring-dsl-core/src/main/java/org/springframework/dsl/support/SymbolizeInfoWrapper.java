/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.dsl.support;

import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.SymbolInformation;
import org.springframework.dsl.service.symbol.SymbolizeInfo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Utility class to wrap given {@link SymbolizeInfo} of a {@link Mono} type into
 * {@link Flux} types.
 *
 * @author Janne Valkealahti
 *
 */
class SymbolizeInfoWrapper implements SymbolizeInfo {

	private final Mono<SymbolizeInfo> symbolizeInfo;

	SymbolizeInfoWrapper(Mono<SymbolizeInfo> symbolizeInfo) {
		this.symbolizeInfo = symbolizeInfo.cache();
	}

	@Override
	public Flux<DocumentSymbol> documentSymbols() {
		return symbolizeInfo.map(si -> si.documentSymbols()).flatMapMany(i -> i);
	}

	@Override
	public Flux<SymbolInformation> symbolInformations() {
		return symbolizeInfo.map(si -> si.symbolInformations()).flatMapMany(i -> i);
	}
}
