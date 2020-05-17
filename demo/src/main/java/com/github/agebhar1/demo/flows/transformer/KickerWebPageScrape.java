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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.integration.xml.transformer.ResultToStringTransformer;
import org.springframework.integration.xml.transformer.XsltPayloadTransformer;

public abstract class KickerWebPageScrape {

	private static final Logger logger = LoggerFactory.getLogger(KickerWebPageScrape.class);

	public KickerWebPageScrape() {

	}

	public static XsltPayloadTransformer standings() {

		logger.info("Create Xslt based 'Kicker' season standings transformer.");

		final XsltPayloadTransformer transformer = new XsltPayloadTransformer(
				new DefaultResourceLoader().getResource("classpath:xslt/page-scrape.xsl"),
				new ResultToStringTransformer());
		transformer.setAlwaysUseResultFactory(true);
		transformer.setXsltParamHeaders("scheme", "authority", "correlationId", "scrapedAt");

		return transformer;

	}

}
