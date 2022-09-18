package com.utils.csv;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;

public abstract class AbstractCsvWriter implements CsvWriter {

	private final String name;
	private final Path outputPath;

	protected AbstractCsvWriter(
			final String name,
			final Path outputPath) {

		this.name = name;
		this.outputPath = outputPath;
	}

	@Override
	@ApiMethod
	public boolean writeCsv() {

		boolean success = false;
		if (StringUtils.isNotBlank(name)) {
			Logger.printProgress("writing " + name + ":");
			Logger.printLine(outputPath);
		}

		FactoryFolderCreator.getInstance().createParentDirectories(outputPath, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(outputPath, true);
		try (PrintStream printStream = new PrintStream(
				new BufferedOutputStream(Files.newOutputStream(outputPath)),
				false, StandardCharsets.UTF_8)) {

			printStream.println("\"sep=,\"");
			write(printStream);

			success = true;

		} catch (final Exception exc) {
			if (StringUtils.isNotBlank(name)) {
				Logger.printError("failed to write " + name);
				Logger.printException(exc);
			}
		}
		return success;
	}

	protected abstract void write(
			PrintStream printStream);
}
