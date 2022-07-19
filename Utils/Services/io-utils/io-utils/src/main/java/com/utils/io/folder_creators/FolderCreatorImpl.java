package com.utils.io.folder_creators;

import java.nio.file.Files;
import java.nio.file.Path;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FolderCreatorImpl implements FolderCreator {

	static final FolderCreatorImpl INSTANCE = new FolderCreatorImpl();

	private FolderCreatorImpl() {
	}

	@ApiMethod
	@Override
	public boolean createParentDirectories(
			final Path filePath,
			final boolean verbose) {

		final boolean success;
		if (filePath != null) {
			final Path parentFile = filePath.getParent();
			success = createDirectories(parentFile, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@ApiMethod
	@Override
	public boolean createDirectories(
			final Path directoryPath,
			final boolean verbose) {

		final boolean success;
		if (directoryPath != null && !IoUtils.directoryExists(directoryPath)) {
			success = createDirectoriesNoCheck(directoryPath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@ApiMethod
	@Override
	public boolean createDirectoriesNoCheck(
			final Path directoryPath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.createDirectories(directoryPath);
			success = true;

		} catch (final Exception exc) {
			Logger.printError("failed to create directory:" + System.lineSeparator() + directoryPath);
			Logger.printException(exc);
		}
		return success;
	}

	@ApiMethod
	@Override
	public boolean createDirectory(
			final Path directoryPath,
			final boolean verbose) {

		final boolean success;
		if (directoryPath != null && !IoUtils.directoryExists(directoryPath)) {
			success = createDirectoryNoChecks(directoryPath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@ApiMethod
	@Override
	public boolean createDirectoryNoChecks(
			final Path directoryPath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.createDirectory(directoryPath);
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to create directory:" + System.lineSeparator() + directoryPath);
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
