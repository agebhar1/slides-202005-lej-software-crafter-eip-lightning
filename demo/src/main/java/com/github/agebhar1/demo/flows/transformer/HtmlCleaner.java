/*
 * Copyright 2020 Andreas Gebhardt
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
package com.github.agebhar1.demo.flows.transformer;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.DomSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.transformer.GenericTransformer;
import org.w3c.dom.Document;

public class HtmlCleaner implements GenericTransformer<String, Document> {

	private static final Logger logger = LoggerFactory.getLogger(HtmlCleaner.class);

	private final org.htmlcleaner.HtmlCleaner htmlCleaner = new org.htmlcleaner.HtmlCleaner();

	public HtmlCleaner() {
		logger.info("Create instance of class '{}'.", getClass().getCanonicalName());
	}

	@Override
	public Document transform(final String source) {
		try {
			return new DomSerializer(htmlCleaner.getProperties()).createDOM(htmlCleaner.clean(source));
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

}
