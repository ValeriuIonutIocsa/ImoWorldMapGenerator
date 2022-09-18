package com.utils.io.zip;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.utils.concurrency.no_progress.ConcurrencyUtilsSimpleRegular;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class ZipFileCreator {

	private final Path srcFilePath;
	private final Path zipArchiveFilePath;
	private final boolean useTempFile;
	private final boolean deleteExisting;
	private final int threadCount;
	private final boolean updateFileTimes;
	private final boolean verbose;

	private boolean folder;
	private boolean success;

	public ZipFileCreator(
			final Path srcFilePath,
			final Path zipArchiveFilePath,
			final boolean useTempFile,
			final boolean deleteExisting,
			final int threadCount,
			final boolean updateFileTimes,
			final boolean verbose) {

		this.srcFilePath = srcFilePath;
		this.zipArchiveFilePath = zipArchiveFilePath;
		this.useTempFile = useTempFile;
		this.deleteExisting = deleteExisting;
		this.threadCount = threadCount;
		this.updateFileTimes = updateFileTimes;
		this.verbose = verbose;
	}

	public void work() {

		folder = false;
		success = false;

		Logger.printProgress("creating ZIP archive:");
		Logger.printLine(zipArchiveFilePath);

		final boolean keepGoing;

		if (IoUtils.directoryExists(srcFilePath)) {

			folder = true;
			keepGoing = true;

		} else {
			if (IoUtils.fileExists(srcFilePath)) {
				keepGoing = true;
			} else {
				Logger.printWarning("the source file does not exist:" +
						System.lineSeparator() + srcFilePath);
				keepGoing = false;
			}
		}
		if (keepGoing) {

			if (deleteExisting && IoUtils.fileExists(zipArchiveFilePath)) {
				FactoryFileDeleter.getInstance().deleteFile(zipArchiveFilePath, true);
			}

			FactoryFolderCreator.getInstance().createParentDirectories(zipArchiveFilePath, true);

			try (FileSystem zipFileSystem = ZipUtils.createNewZipFileSystem(zipArchiveFilePath, useTempFile)) {

				if (folder) {

					final List<Runnable> runnableList = new ArrayList<>();
					Files.walkFileTree(srcFilePath, new SimpleFileVisitor<>() {

						@Override
						public FileVisitResult visitFile(
								final Path filePath,
								final BasicFileAttributes attrs) throws IOException {

							runnableList.add(() -> {

								try {
									if (verbose) {
										Logger.printLine("zipping file: " + filePath);
									}

									final Path relativeFilePath = srcFilePath.relativize(filePath);
									final Path zipFilePath = zipFileSystem.getPath(relativeFilePath.toString());
									final Path zipFileParentFolderPath = zipFilePath.getParent();
									if (zipFileParentFolderPath != null &&
											!Files.isDirectory(zipFileParentFolderPath)) {
										synchronized (this) {
											Files.createDirectories(zipFileParentFolderPath);
										}
									}
									Files.copy(filePath, zipFilePath, StandardCopyOption.REPLACE_EXISTING,
											StandardCopyOption.COPY_ATTRIBUTES);
									if (updateFileTimes) {
										Files.setLastModifiedTime(zipFilePath, FileTime.from(Instant.now()));
									}

								} catch (final Exception exc) {
									Logger.printError("failed to zip file:" + System.lineSeparator() + filePath);
									Logger.printException(exc);
								}
							});

							return super.visitFile(filePath, attrs);
						}
					});
					if (!runnableList.isEmpty()) {
						new ConcurrencyUtilsSimpleRegular(threadCount).executeMultiThreadedTask(runnableList);
					}

				} else {

					try {
						if (verbose) {
							Logger.printLine("zipping file: " + srcFilePath);
						}

						final String srcFileName = PathUtils.computeFileName(srcFilePath);
						final Path zipFilePath = zipFileSystem.getPath(srcFileName);
						Files.copy(srcFilePath, zipFilePath, StandardCopyOption.REPLACE_EXISTING,
								StandardCopyOption.COPY_ATTRIBUTES);
						if (updateFileTimes) {
							Files.setLastModifiedTime(zipFilePath, FileTime.from(Instant.now()));
						}

					} catch (final Exception exc) {
						Logger.printError("failed to zip file:" + System.lineSeparator() + srcFilePath);
						Logger.printException(exc);
					}
				}

				Logger.printStatus("Finished creating ZIP archive.");
				success = true;

			} catch (final Exception exc) {
				Logger.printError("failed to create ZIP archive:" +
						System.lineSeparator() + zipArchiveFilePath);
				Logger.printException(exc);
			}
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public boolean isFolder() {
		return folder;
	}

	public boolean isSuccess() {
		return success;
	}
}
