package com.utils.io.ro_flag_clearers;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;

public interface ReadOnlyFlagClearer {

	/**
	 * If file exist, clears the readonly flag of that file.
	 *
	 * @param filePath
	 *            The path to the file.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean clearReadOnlyFlagFile(
			Path filePath,
			boolean verbose);

	/**
	 * Clears the readonly flag of a file.
	 *
	 * @param filePath
	 *            The path to the file.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean clearReadOnlyFlagFileNoChecks(
			Path filePath,
			boolean verbose);

	/**
	 * If folder exists, clears the readonly flags of all files in that folder.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean clearReadOnlyFlagFolder(
			Path folderPath,
			boolean verbose);

	/**
	 * Clears the readonly flags of all files inside a folder.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean clearReadOnlyFlagFolderNoChecks(
			Path folderPath,
			boolean verbose);
}
