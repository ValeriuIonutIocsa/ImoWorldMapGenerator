package com.utils.compression;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public final class GZipFileCompressionUtils {

	private GZipFileCompressionUtils() {
	}

	public static void compressFile(
			final Path inputFilePath,
			final Path outputFilePath) throws Exception {

		try (InputStream inputStream = Files.newInputStream(inputFilePath);
				OutputStream outputStream = new GZIPOutputStream(Files.newOutputStream(outputFilePath))) {

			IOUtils.copy(inputStream, outputStream);
		}
	}

	public static void decompressFile(
			final Path inputFilePath,
			final Path outputFilePath) throws Exception {

		try (InputStream inputStream = new GZIPInputStream(Files.newInputStream(inputFilePath));
				OutputStream outputStream = Files.newOutputStream(outputFilePath)) {

			IOUtils.copy(inputStream, outputStream);
		}
	}
}
