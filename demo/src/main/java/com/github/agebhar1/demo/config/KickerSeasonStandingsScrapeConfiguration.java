/*
 * Copyright 2020-2021 Andreas Gebhardt
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
package com.github.agebhar1.demo.config;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.style.ToStringCreator;

@ConstructorBinding
@ConfigurationProperties(prefix = "kicker.season.standings.scrape")
public class KickerSeasonStandingsScrapeConfiguration {

	private final URL page;
	private final Duration pollingDelay;
	private final File targetDirectory;

	public KickerSeasonStandingsScrapeConfiguration(final URL page, final Duration pollingDelay,
			final File targetDirectory) {
		this.page = page;
		this.pollingDelay = pollingDelay;
		this.targetDirectory = targetDirectory;
	}

	public URL getPage() {
		return page;
	}

	public Duration getPollingDelay() {
		return pollingDelay;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this) //
				.append("page", getPage()) //
				.append("pollingDelay", getPollingDelay()) //
				.append("targetDirectory", getTargetDirectory()) //
				.toString();
	}

}
