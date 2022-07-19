package com.utils.io.folder_deleters;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FolderDeleterImpl implements FolderDeleter {

	static final FolderDeleterImpl INSTANCE = new FolderDeleterImpl();

	private FolderDeleterImpl() {
	}

	@Override
	@ApiMethod
	public boolean deleteFolder(
			final Path folderPath,
			final boolean verbose) {

		final boolean success;
		if (IoUtils.directoryExists(folderPath)) {
			success = deleteFolderNoChecks(folderPath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@Override
	@ApiMethod
	public boolean deleteFolderNoChecks(
			final Path folderPath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(
						final Path file,
						final BasicFileAttributes attrs) throws IOException {

					FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFileNoChecks(file, true);
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(
						final Path dir,
						final IOException exc) throws IOException {

					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}

			});
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to delete folder:" + System.lineSeparator() + folderPath);
			}
			Logger.printException(exc);
		}
		return success;
	}

	@Override
	@ApiMethod
	public boolean cleanFolder(
			final Path folderPath,
			final boolean verbose) {

		final boolean success;
		if (IoUtils.directoryExists(folderPath)) {
			success = cleanFolderNoChecks(folderPath, verbose);
		} else {
			success = true;
		}
		return success;
	}

	@Override
	@ApiMethod
	public boolean cleanFolderNoChecks(
			final Path folderPath,
			final boolean verbose) {

		boolean success = false;
		try {
			Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult visitFile(
						final Path file,
						final BasicFileAttributes attrs) throws IOException {

					FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFileNoChecks(file, true);
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(
						final Path dir,
						final IOException exc) throws IOException {

					if (!folderPath.equals(dir)) {
						Files.delete(dir);
					}
					return FileVisitResult.CONTINUE;
				}
			});
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to clean folder:" + System.lineSeparator() + folderPath);
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
