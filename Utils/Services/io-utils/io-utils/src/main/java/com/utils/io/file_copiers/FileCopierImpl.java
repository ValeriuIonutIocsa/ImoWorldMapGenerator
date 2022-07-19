package com.utils.io.file_copiers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FileCopierImpl implements FileCopier {

	static final FileCopierImpl INSTANCE = new FileCopierImpl();

	private FileCopierImpl() {
	}

	@ApiMethod
	@Override
	public boolean copyFile(
			final Path srcPath,
			final Path destPath,
			final boolean copyAttributes,
			final boolean verbose) {

		final boolean destFileExists = IoUtils.fileExists(destPath);
		return copyFileNoChecks(srcPath, destPath, destFileExists, copyAttributes, verbose);
	}

	@ApiMethod
	@Override
	public boolean copyFileNoChecks(
			final Path srcPath,
			final Path destPath,
			final boolean destFileExists,
			final boolean copyAttributes,
			final boolean verbose) {

		boolean success = false;
		try {
			if (destFileExists) {
				FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFileNoChecks(destPath, true);
			}
			if (copyAttributes) {
				Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES);
			} else {
				Files.copy(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
			}
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to copy file " +
						System.lineSeparator() + srcPath +
						System.lineSeparator() + "to:" +
						System.lineSeparator() + destPath);
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
