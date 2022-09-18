package com.utils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class IoUtils {

	private IoUtils() {
	}

	/**
	 * @param path
	 *            the input path
	 * @return false if the path is null, blank or if the file does not exists, true otherwise
	 */
	@ApiMethod
	public static boolean fileExists(
			final Path path) {
		return path != null && fileExists(path.toString());
	}

	@ApiMethod
	public static boolean fileExists(
			final String pathString) {
		return StringUtils.isNotBlank(pathString) && new File(pathString).exists();
	}

	/**
	 * @param path
	 *            the input path
	 * @return false if the path is null, blank or if the file is not a directory, true otherwise
	 */
	@ApiMethod
	public static boolean directoryExists(
			final Path path) {
		return path != null && directoryExists(path.toString());
	}

	@ApiMethod
	public static boolean directoryExists(
			final String pathString) {
		return StringUtils.isNotBlank(pathString) && new File(pathString).isDirectory();
	}

	@ApiMethod
	public static long fileSize(
			final String filePathString) {

		final Path filePath = Paths.get(filePathString);
		return fileSize(filePath);
	}

	@ApiMethod
	public static long fileSize(
			final Path filePath) {

		long size = -1;
		try {
			size = Files.size(filePath);
		} catch (final Exception ignored) {
		}
		return size;
	}

	@ApiMethod
	public static long fileLastModifiedTime(
			final String filePathString) {

		final Path filePath = Paths.get(filePathString);
		return fileLastModifiedTime(filePath);
	}

	@ApiMethod
	public static long fileLastModifiedTime(
			final Path filePath) {

		long lastModifiedTime = -1;
		try {
			lastModifiedTime = Files.getLastModifiedTime(filePath).toMillis();
		} catch (final Exception ignored) {
		}
		return lastModifiedTime;
	}

	@ApiMethod
	public static byte[] fileMd5HashCode(
			final String filePathString) {

		final Path filePath = Paths.get(filePathString);
		return fileMd5HashCode(filePath);
	}

	@ApiMethod
	public static byte[] fileMd5HashCode(
			final Path filePath) {

		byte[] md5HashCode = null;
		try {
			final byte[] fileBytes = Files.readAllBytes(filePath);
			final MessageDigest messageDigestMd5 = MessageDigest.getInstance("MD5");
			md5HashCode = messageDigestMd5.digest(fileBytes);

		} catch (final Exception ignored) {
		}
		return md5HashCode;
	}

	@ApiMethod
	public static List<Path> listFiles(
			final Path directoryPath) {

		final List<Path> filePathList = new ArrayList<>();
		try (Stream<Path> fileListStream = Files.list(directoryPath)) {
			fileListStream.forEach(filePathList::add);

		} catch (final Exception exc) {
			Logger.printError("failed to list files inside folder:" +
					System.lineSeparator() + directoryPath);
			Logger.printException(exc);
		}
		return filePathList;
	}

	@ApiMethod
	public static List<Path> listFiles(
			final Path directoryPath,
			final Predicate<Path> filterPredicate) {

		final List<Path> filePathList;
		if (filterPredicate == null) {
			filePathList = listFiles(directoryPath);

		} else {
			filePathList = new ArrayList<>();
			try (Stream<Path> fileListStream = Files.list(directoryPath)) {
				fileListStream.filter(filterPredicate).forEach(filePathList::add);

			} catch (final Exception exc) {
				Logger.printError("failed to list files inside folder:" +
						System.lineSeparator() + directoryPath);
				Logger.printException(exc);
			}
		}
		return filePathList;
	}

	@ApiMethod
	public static List<Path> listFilesRecursively(
			final Path directoryPath) {

		final List<Path> filePathList = new ArrayList<>();
		try (Stream<Path> fileListStream = Files.walk(directoryPath)) {
			fileListStream.forEach(filePathList::add);

		} catch (final Exception exc) {
			Logger.printError("failed to list files recursively inside folder:" +
					System.lineSeparator() + directoryPath);
			Logger.printException(exc);
		}
		return filePathList;
	}

	@ApiMethod
	public static List<Path> listFilesRecursively(
			final Path directoryPath,
			final Predicate<Path> filterPredicate) {

		final List<Path> filePathList;
		if (filterPredicate == null) {
			filePathList = listFilesRecursively(directoryPath);

		} else {
			filePathList = new ArrayList<>();
			try (Stream<Path> fileListStream = Files.walk(directoryPath)) {
				fileListStream.filter(filterPredicate).forEach(filePathList::add);

			} catch (final Exception exc) {
				Logger.printError("failed to list files inside folder:" +
						System.lineSeparator() + directoryPath);
				Logger.printException(exc);
			}
		}
		return filePathList;
	}

	@ApiMethod
	public static int computeLineCount(
			final Path filePath) {

		int lineCount = 0;
		try (BufferedReader bufferedReader = Files.newBufferedReader(filePath)) {

			while (bufferedReader.readLine() != null) {
				lineCount++;
			}

		} catch (final Exception exc) {
			Logger.printError("failed to compute line count of file:" +
					System.lineSeparator() + filePath);
			Logger.printException(exc);
		}
		return lineCount;
	}

	@ApiMethod
	public static byte[] fileToByteArray(
			final Path filePath) {

		byte[] byteArray = null;
		try {
			byteArray = Files.readAllBytes(filePath);

		} catch (final Exception exc) {
			Logger.printError("failed to read byte array from file:" +
					System.lineSeparator() + filePath);
			Logger.printException(exc);
		}
		return byteArray;
	}

	@ApiMethod
	public static boolean byteArrayToFile(
			final byte[] zipByteArray,
			final Path zipArchivePath) {

		boolean success = false;
		try {
			Files.write(zipArchivePath, zipByteArray);
			success = true;

		} catch (final Exception exc) {
			Logger.printError("failed to write byte array to file:" +
					System.lineSeparator() + zipArchivePath);
			Logger.printException(exc);
		}
		return success;
	}

	@ApiMethod
	public static String fileToString(
			final Path filePath) throws IOException {
		return fileToString(filePath, StandardCharsets.UTF_8);
	}

	@ApiMethod
	public static String fileToString(
			final Path filePath,
			final Charset charset) throws IOException {

		try (InputStream inputStream = Files.newInputStream(filePath)) {
			return inputStreamToString(inputStream, charset.name());
		}
	}

	@ApiMethod
	public static String inputStreamToString(
			final InputStream inputStream) throws IOException {
		return inputStreamToString(inputStream, StandardCharsets.UTF_8.name());
	}

	@ApiMethod
	public static String inputStreamToString(
			final InputStream inputStream,
			final String encoding) throws IOException {
		return IOUtils.toString(inputStream, encoding);
	}

	@ApiMethod
	public static String resourceFileToString(
			final String resourceFilePathString) {

		String str = "";
		try {
			final InputStream inputStream = resourceFileToInputStream(resourceFilePathString);
			str = inputStreamToString(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to get resource file content for:" +
					System.lineSeparator() + resourceFilePathString);
			Logger.printException(exc);
		}
		return str;
	}

	@ApiMethod
	public static byte[] resourceFileToByteArray(
			final String resourceFilePathString) {

		byte[] bytes = null;
		try {
			final InputStream inputStream = resourceFileToInputStream(resourceFilePathString);
			bytes = IOUtils.toByteArray(inputStream);

		} catch (final Exception exc) {
			Logger.printError("failed to get resource file content for:" +
					System.lineSeparator() + resourceFilePathString);
			Logger.printException(exc);
		}
		return bytes;
	}

	@ApiMethod
	public static InputStream resourceFileToInputStream(
			final String resourceFilePathString) {
		return Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(resourceFilePathString);
	}

	@ApiMethod
	public static URL resourceToUrl(
			final String resourceFilePathString) {
		return Thread.currentThread().getContextClassLoader()
				.getResource(resourceFilePathString);
	}

	@ApiMethod
	public static File createTemporaryFile(
			final InputStream inputStream) throws IOException {

		final File tempFile = File.createTempFile(StrUtils.createDateTimeString(), ".tmp");
		tempFile.deleteOnExit();
		try (OutputStream outputStream = Files.newOutputStream(tempFile.toPath())) {
			IOUtils.copy(inputStream, outputStream);
		}
		return tempFile;
	}

	@ApiMethod
	public static void writeStringToFile(
			final File file,
			final String string,
			final Charset charset) throws IOException {
		writeStringToFile(file.toPath(), string, charset);
	}

	@ApiMethod
	public static void writeStringToFile(
			final Path path,
			final String string,
			final Charset charset) throws IOException {

		FactoryFolderCreator.getInstance().createParentDirectories(path, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(path, true);
		try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, charset)) {
			bufferedWriter.write(string);
		}
	}

	@ApiMethod
	public static boolean openFileWithDefaultApp(
            final String filePathString) {

		boolean success = false;
		try {
			final ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd", "/c", "start", filePathString);
			processBuilder.inheritIO();
			final Process process = processBuilder.start();
			final int exitCode = process.waitFor();
			success = exitCode == 0;

		} catch (final Exception exc) {
			Logger.printError("failed to open file with default app:" + System.lineSeparator() + filePathString);
			Logger.printException(exc);
		}
		return success;
	}
}
