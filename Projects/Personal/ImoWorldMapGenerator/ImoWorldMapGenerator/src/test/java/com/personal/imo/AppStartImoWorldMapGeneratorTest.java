package com.personal.imo;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AppStartImoWorldMapGeneratorTest {

	@Test
	void testMain() {

		final String resultsXmlFilePathString =
				Paths.get("inputs", "IMO_Team_2022.xml").toString();
		final String outputHtmlFilePathString =
				Paths.get("outputs", "IMO_Team_2022_WorldMap.html").toString();
		final String[] args = new String[] {
				resultsXmlFilePathString,
				outputHtmlFilePathString
		};
		AppStartImoWorldMapGenerator.main(args);
	}
}
