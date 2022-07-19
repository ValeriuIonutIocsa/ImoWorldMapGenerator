package com.utils.io.folder_creators;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;

public interface FolderCreator {

	@ApiMethod
	boolean createParentDirectories(
			Path filePath,
			boolean verbose);

	@ApiMethod
	boolean createDirectories(
			Path directoryPath,
			boolean verbose);

	@ApiMethod
	boolean createDirectoriesNoCheck(
			Path directoryPath,
			boolean verbose);

	@ApiMethod
	boolean createDirectory(
			Path directoryPath,
			boolean verbose);

	@ApiMethod
	boolean createDirectoryNoChecks(
			Path directoryPath,
			boolean verbose);
}
