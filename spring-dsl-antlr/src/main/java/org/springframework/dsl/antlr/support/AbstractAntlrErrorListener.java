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
package org.springframework.dsl.antlr.support;

import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.springframework.dsl.domain.Position;
import org.springframework.dsl.domain.Range;
import org.springframework.dsl.service.reconcile.DefaultReconcileProblem;
import org.springframework.dsl.service.reconcile.ReconcileProblem;
import org.springframework.util.Assert;

/**
 * Base abstract extension to {@code ANTLR} {@link BaseErrorListener} forcing to
 * pass in a typed list of {@link ReconcileProblem} where syntax errors are
 * stored.
 *
 * @author Janne Valkealahti
 *
 */
public abstract class AbstractAntlrErrorListener extends BaseErrorListener {

	private final List<ReconcileProblem> errors;

    /**
     * Instantiates a new abstract antlr error listener.
     *
     * @param errors the errors
     */
    public AbstractAntlrErrorListener(List<ReconcileProblem> errors) {
		Assert.notNull(errors, "errors list must be set");
		this.errors = errors;
	}

	@Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                    String msg, RecognitionException e) {
            Position start = Position.from(line - 1, charPositionInLine);
            Position end = Position.from(line - 1, charPositionInLine);
            errors.add(new DefaultReconcileProblem(msg, new Range(start, end)));
    }
}
