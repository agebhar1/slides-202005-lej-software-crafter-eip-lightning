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
package com.github.agebhar1.demo.flows;

import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.integration.dsl.Pollers.fixedDelay;

import java.io.File;
import java.net.URL;
import java.time.Clock;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.dsl.FileWritingMessageHandlerSpec;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.dsl.HttpMessageHandlerSpec;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.stereotype.Component;

import com.github.agebhar1.demo.config.KickerSeasonStandingsScrapeConfiguration;
import com.github.agebhar1.demo.flows.transformer.HtmlCleaner;
import com.github.agebhar1.demo.flows.transformer.KickerWebPageScrape;

@Component
public class KickerSeasonStandingsScrape {

	private static final Logger logger = LoggerFactory.getLogger(KickerSeasonStandingsScrape.class);

	private final Clock clock;
	private final KickerSeasonStandingsScrapeConfiguration configuration;

	public KickerSeasonStandingsScrape(final Clock clock,
			final KickerSeasonStandingsScrapeConfiguration configuration) {

		logger.info("Create instance of class '{}'.", getClass().getCanonicalName());

		this.clock = clock;
		this.configuration = configuration;
	}

	@Bean
	public IntegrationFlow kickerSeasonStandingsScrapeFlow() {

		logger.info("Create 'Kicker' season standings scrape flow w/ configuration: {}", configuration);

		final URL page = configuration.getPage();

		final String scheme = page.getProtocol();
		final String authority = page.getAuthority();

		final MessageSource<String> pageSupplier = () -> MessageBuilder //
				.withPayload(page.toString()) //
				.setCorrelationId(randomUUID()) //
				.build();

		return IntegrationFlows //

				.from(pageSupplier, spec -> {
					spec.poller(fixedDelay(configuration.getPollingDelay()));
				}) //
				.handle(fetchURLBy("payload")) //
				.transform(cleanHtml()) //
				.enrichHeaders(headers -> {
					headers //
							.header("scheme", scheme) //
							.header("authority", authority) //
							.headerFunction("scrapedAt", message -> Instant.now(clock).toString());
				}) //
				.transform(KickerWebPageScrape.standings()) //
				.log() //
				.publishSubscribeChannel(channel -> {
					channel.subscribe(subFlow -> {
						subFlow.channel("seasonStandingsXmlOut")
								.handle(writeAsFileToDirectory(configuration.getTargetDirectory()));
					});
				}).get();
	}

	public static HttpMessageHandlerSpec fetchURLBy(final String expression) {

		logger.info("Create HTML outbound gateway w/ expression: '{}'", expression);

		return Http //
				.outboundGateway(new SpelExpressionParser().parseExpression(expression)) //
				.httpMethod(GET) //
				.expectedResponseType(String.class);
	}

	public static GenericTransformer<?, ?> cleanHtml() {
		return new HtmlCleaner();
	}

	public static FileWritingMessageHandlerSpec writeAsFileToDirectory(final File directory) {

		logger.info("Create file outbound adapter w/ target directory: '{}'", directory);

		return Files //
				.outboundAdapter(directory) //
				.fileNameGenerator(message -> "standings.xml") //
				.fileExistsMode(FileExistsMode.REPLACE);
	}

}
