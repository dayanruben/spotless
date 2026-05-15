/*
 * Copyright 2025-2026 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.spotless.maven.java;

import org.junit.jupiter.api.Test;

import com.diffplug.spotless.maven.MavenIntegrationHarness;

class ExpandWildcardImportsStepTest extends MavenIntegrationHarness {

	@Test
	void expandWildcardImports() throws Exception {
		writePomWithJavaSteps("<expandWildcardImports/>");

		// Create supporting classes in source roots so JavaParserTypeSolver can resolve them
		setFile("src/main/java/foo/bar/AnotherClassInSamePackage.java")
				.toResource("java/expandwildcardimports/AnotherClassInSamePackage.test");
		setFile("src/main/java/foo/bar/baz/AnotherImportedClass.java")
				.toResource("java/expandwildcardimports/AnotherImportedClass.test");
		// Source for the annotation used in the test (resolves via source root, not JAR)
		setFile("src/main/java/org/example/SomeAnnotation.java")
				.toContent("package org.example;\n\npublic @interface SomeAnnotation {}\n");

		String path = "src/main/java/foo/bar/JavaClassWithWildcards.java";
		setFile(path).toResource("java/expandwildcardimports/JavaClassWithWildcardsUnformatted.test");

		mavenRunner().withArguments("spotless:apply").runNoError();

		assertFile(path).sameAsResource("java/expandwildcardimports/JavaClassWithWildcardsFormatted.test");
	}
}
