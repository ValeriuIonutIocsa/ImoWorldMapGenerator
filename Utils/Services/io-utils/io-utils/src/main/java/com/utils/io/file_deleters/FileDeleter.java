package com.utils.io.file_deleters;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;

public interface FileDeleter {

	@ApiMethod
	boolean deleteFile(
			Path filePath,
			boolean verbose);

	@ApiMethod
	boolean deleteFileNoChecks(
			Path filePath,
			boolean verbose);
}
