package com.utils.io.file_deleters;

import java.nio.file.Files;
import java.nio.file.Path;

import com.utils.io.IoUtils;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FileDeleterImpl implements FileDeleter {

	static final FileDeleterImpl INSTANCE = new FileDeleterImpl();

	private FileDeleterImpl() {
	}

	@Override
	public boolean deleteFile(
			final Path filePath,
			final boolean verbose) {

		final boolean success;
		if (IoUtils.fileExists(filePath)) {
			success = deleteFileNoChecks(filePath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@Override
	public boolean deleteFileNoChecks(
			final Path filePath,
			final boolean verbose) {

		boolean success = false;
		try {
			FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(filePath, true);
			Files.delete(filePath);
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to delete file:" + System.lineSeparator() + filePath);
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
