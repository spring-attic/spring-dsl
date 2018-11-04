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
package demo.dotdsl;

import org.springframework.dsl.antlr.AntlrParseResult;

/**
 * {@code ANTLR} visitor for {@code dot} language;
 * <p>
 * currently visitor is just used to lint it, so no actual result is needed,
 * thus just dummy Object type
 *
 * @author Janne Valkealahti
 *
 */
//tag::snippet1[]
public class DOTLanguageVisitor extends DOTBaseVisitor<AntlrParseResult<Object>> {
}
//end::snippet1[]
