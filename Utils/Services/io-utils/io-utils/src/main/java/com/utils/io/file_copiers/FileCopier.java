package com.utils.io.file_copiers;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;

public interface FileCopier {

	@ApiMethod
	boolean copyFile(
			Path srcPath,
			Path destPath,
			boolean copyAttributes,
			boolean verbose);

	@ApiMethod
	boolean copyFileNoChecks(
			Path srcPath,
			Path destPath,
			boolean destFileExists,
			boolean copyAttributes,
			boolean verbose);
}
