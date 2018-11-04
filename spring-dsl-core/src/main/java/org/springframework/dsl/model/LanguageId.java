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
package org.springframework.dsl.model;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Instead of working with a plain {@code String} as a {@code languageId}, in
 * this class we also keep a generic name of the language.
 *
 * @author Janne Valkealahti
 *
 */
public class LanguageId {

	public static final LanguageId ALL = languageId("*");
	public static final LanguageId TXT = languageId("txt", "Plaintext");
	public static final LanguageId BAT = languageId("bat", "Windows Bat");
	public static final LanguageId BIBTEX = languageId("bibtex", "BibTeX");
	public static final LanguageId CLOJURE = languageId("clojure", "Clojure");
	public static final LanguageId COFFEESCRIPT = languageId("coffeescript", "Coffeescript");
	public static final LanguageId C = languageId("c", "C");
	public static final LanguageId CPP = languageId("cpp", "C++");
	public static final LanguageId CSHARP = languageId("csharp", "C#");
	public static final LanguageId CSS = languageId("css", "CSS");
	public static final LanguageId DIFF = languageId("diff", "Diff");
	public static final LanguageId DOCKERFILE = languageId("dockerfile", "Dockerfile");
	public static final LanguageId FSHARP = languageId("fsharp", "F#");
	public static final LanguageId GIT_COMMIT = languageId("git-commit", "Git Commit", new String[] { "git-rebase" });
	public static final LanguageId GIT_REBASE = languageId("git-rebase", "Git Rebase", new String[] { "git-commit" });
	public static final LanguageId GO = languageId("go", "Go");
	public static final LanguageId GROOVY = languageId("groovy", "Groovy");
	public static final LanguageId HANDLEBARS = languageId("handlebars", "Handlebars");
	public static final LanguageId HTML = languageId("html", "HTML");
	public static final LanguageId INI = languageId("ini", "Ini");
	public static final LanguageId JAVA = languageId("java", "Java");
	public static final LanguageId JAVASCRIPT = languageId("javascript", "JavaScript");
	public static final LanguageId JSON = languageId("json", "JSON");
	public static final LanguageId LATEX = languageId("latex", "LaTeX");
	public static final LanguageId LESS = languageId("less", "Less");
	public static final LanguageId LUA = languageId("lua", "Lua");
	public static final LanguageId MAKEFILE = languageId("makefile", "Makefile");
	public static final LanguageId MARKDOWN = languageId("markdown", "Markdown");
	public static final LanguageId OBJECTIVE_C = languageId("objective-c", "Objective-C");
	public static final LanguageId OBJECTIVE_CPP = languageId("objective-cpp", "Objective-C++");
	public static final LanguageId PERL = languageId("perl", "Perl", new String[] { "perl6" });
	public static final LanguageId PERL6 = languageId("perl6", "Perl", new String[] { "perl" });
	public static final LanguageId PHP = languageId("php", "PHP");
	public static final LanguageId POWERSHELL = languageId("powershell", "Powershell");
	public static final LanguageId JADE = languageId("jade", "Pug");
	public static final LanguageId PYTHON = languageId("python", "Python");
	public static final LanguageId R = languageId("r", "R");
	public static final LanguageId RAZOR = languageId("razor", "Razor (cshtml)");
	public static final LanguageId RUBY = languageId("ruby", "Ruby");
	public static final LanguageId RUST = languageId("rust", "Rust");
	public static final LanguageId SCSS = languageId("scss", "Sass");
	public static final LanguageId SHADERLAB = languageId("shaderlab", "ShaderLab");
	public static final LanguageId SHELLSCRIPT = languageId("shellscript", "Shell Script (Bash)");
	public static final LanguageId SQL = languageId("sql", "SQL");
	public static final LanguageId SWIFT = languageId("swift", "Swift");
	public static final LanguageId TYPESCRIPT = languageId("typescript", "TypeScript");
	public static final LanguageId TEX = languageId("tex", "TeX");
	public static final LanguageId VB = languageId("vb", "Visual Basic");
	public static final LanguageId XML = languageId("xml", "XML");
	public static final LanguageId XSL = languageId("xsl", "XSL");
	public static final LanguageId YAML = languageId("yaml", "YAML");	
	
	private final String identifier;
	private final String description;
	private final String[] compatible;

	/**
	 * Instantiates a new language id.
	 *
	 * @param identifier the identifier
	 * @param description the language
	 */
	public LanguageId(String identifier, String description) {
		this(identifier, description, null);
	}

	/**
	 * Instantiates a new language id.
	 *
	 * @param identifier the identifier
	 * @param description the language
	 * @param compatible the compatible languages
	 */
	public LanguageId(String identifier, String description, String[] compatible) {
		Assert.hasLength(identifier, "identifier must not be empty");
		this.identifier = identifier;
		this.description = description;
		this.compatible = compatible != null ? compatible : new String[0];
	}

	/**
	 * Gets a {@link LanguageId} with identifier.
	 *
	 * @param identifier the identifier
	 * @return the language id
	 */
	public static LanguageId languageId(String identifier) {
		return languageId(identifier, null, null);
	}

	/**
	 * Gets a {@link LanguageId} with identifier and defined language description.
	 *
	 * @param identifier the identifier
	 * @param language the language
	 * @return the language id
	 */
	public static LanguageId languageId(String identifier, String language) {
		return languageId(identifier, language, null);
	}

	/**
	 * Gets a {@link LanguageId} with identifier and defined language description.
	 *
	 * @param identifier the identifier
	 * @param language the language
	 * @param compatible the compatible languages
	 * @return the language id
	 */
	public static LanguageId languageId(String identifier, String language, String[] compatible) {
		return new LanguageId(identifier, language, compatible);
	}
	
	/**
	 * Gets the language identifier.
	 *
	 * @return the language identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the language description.
	 *
	 * @return the language description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Indicates if a given {@link LanguageId} is compatible.
	 *
	 * @param other the language id
	 * @return true, if is compatible with
	 */
	public boolean isCompatibleWith(@Nullable LanguageId other) {
		if (other == null) {
			return false;
		}
		if (this == ALL) {
			return true;
		}
		for (String c : compatible) {
			if (ObjectUtils.nullSafeEquals(c, other.getIdentifier())) {
				return true;
			}
		}
		return ObjectUtils.nullSafeEquals(this, other);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LanguageId other = (LanguageId) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
}
