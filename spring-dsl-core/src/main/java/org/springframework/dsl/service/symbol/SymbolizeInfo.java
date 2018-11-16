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
package org.springframework.dsl.service.symbol;

import org.springframework.dsl.document.Document;
import org.springframework.dsl.domain.DocumentSymbol;
import org.springframework.dsl.domain.SymbolInformation;

import reactor.core.publisher.Flux;

/**
 * Interface used as a return type from {@link Symbolizer#symbolize(Document)}.
 * Allows to return {@link DocumentSymbol} and {@link SymbolInformation}
 * separately.
 * <p>
 * For example lsp's {@code textDocument/documentSymbol} request response may
 * have types of {@code DocumentSymbol[]}, {@code SymbolInformation[]} or
 * {@code null} and interface is first used to check document symbols and then
 * symbol information.
 *
 * @author Janne Valkealahti
 *
 */
public interface SymbolizeInfo {

	/**
	 * Return a flux of document symbols. Defaults to empty flux.
	 *
	 * @return the flux of document symbols
	 */
	default Flux<DocumentSymbol> documentSymbols() {
		return Flux.empty();
	}

	/**
	 * Return a flux of symbol informations. Defaults to empty flux.
	 *
	 * @return the flux of symbol informations
	 */
	default Flux<SymbolInformation> symbolInformations() {
		return Flux.empty();
	}

	/**
	 * Helper method to build {@code SymbolizeInfo} as just using default methods.
	 *
	 * @return the symbolize info
	 */
	public static SymbolizeInfo empty() {
		return new SymbolizeInfo() {
		};
	}

	/**
	 * Helper method to build {@code SymbolizeInfo} out from flux's for
	 * {@link DocumentSymbol} and {@link SymbolInformation}.
	 *
	 * @param documentSymbols    the document symbols
	 * @param symbolInformations the symbol informations
	 * @return the symbolize info
	 */
	public static SymbolizeInfo of(Flux<DocumentSymbol> documentSymbols, Flux<SymbolInformation> symbolInformations) {
		return new SymbolizeInfo() {

			@Override
			public Flux<DocumentSymbol> documentSymbols() {
				return documentSymbols;
			}

			@Override
			public Flux<SymbolInformation> symbolInformations() {
				return symbolInformations;
			}
		};
	}

	/**
	 * Helper method to build {@code SymbolizeInfo} out from flux for
	 * {@link DocumentSymbol}.
	 *
	 * @param documentSymbols the document symbols
	 * @return the symbolize info
	 */
	public static SymbolizeInfo ofDocumentSymbol(Flux<DocumentSymbol> documentSymbols) {
		return new SymbolizeInfo() {

			@Override
			public Flux<DocumentSymbol> documentSymbols() {
				return documentSymbols;
			}
		};
	}

	/**
	 * Helper method to build {@code SymbolizeInfo} out from flux for
	 * {@link SymbolInformation}.
	 *
	 * @param symbolInformations the symbol informations
	 * @return the symbolize info
	 */
	public static SymbolizeInfo ofSymbolInformation(Flux<SymbolInformation> symbolInformations) {
		return new SymbolizeInfo() {

			@Override
			public Flux<SymbolInformation> symbolInformations() {
				return symbolInformations;
			}
		};
	}
}
