package com.utils.io.ro_flag_clearers;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class ReadOnlyFlagClearerImpl implements ReadOnlyFlagClearer {

	static final ReadOnlyFlagClearerImpl INSTANCE = new ReadOnlyFlagClearerImpl();

	private ReadOnlyFlagClearerImpl() {
	}

	@Override
	public boolean clearReadOnlyFlagFile(
			final Path filePath,
			final boolean verbose) {

		final boolean success;
		if (IoUtils.fileExists(filePath)) {
			success = clearReadOnlyFlagFileNoChecks(filePath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@Override
	@ApiMethod
	public boolean clearReadOnlyFlagFileNoChecks(
			final Path filePath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.setAttribute(filePath, "dos:readonly", false);
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to clear readonly flag for path:" +
						System.lineSeparator() + filePath);
			}
			Logger.printException(exc);
		}
		return success;
	}

	@Override
	public boolean clearReadOnlyFlagFolder(
			final Path folderPath,
			final boolean verbose) {

		final boolean success;
		if (IoUtils.directoryExists(folderPath)) {
			success = clearReadOnlyFlagFolderNoChecks(folderPath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@Override
	public boolean clearReadOnlyFlagFolderNoChecks(
			final Path folderPath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(
						final Path file,
						final BasicFileAttributes attr) {

					clearReadOnlyFlagFileNoChecks(file, true);
					return FileVisitResult.CONTINUE;
				}
			});
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to clear the readonly flags of folder:" +
						System.lineSeparator() + folderPath);
			}
			Logger.printException(exc);
		}
		return success;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
