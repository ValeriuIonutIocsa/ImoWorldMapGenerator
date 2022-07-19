package com.utils.io.folder_deleters;

import java.nio.file.Path;

import com.utils.annotations.ApiMethod;

public interface FolderDeleter {

	/**
	 * If folder exists, deletes the folder and all of its contents.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean deleteFolder(
			Path folderPath,
			boolean verbose);

	/**
	 * Deletes a folder and all of its contents.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean deleteFolderNoChecks(
			Path folderPath,
			boolean verbose);

	/**
	 * If folder exists, deletes everything inside the folder but not the folder itself.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean cleanFolder(
			Path folderPath,
			boolean verbose);

	/**
	 * Deletes everything inside a folder but not the folder itself.
	 *
	 * @param folderPath
	 *            The path to the folder.
	 * @param verbose
	 *            If true, messages will be logged.
	 */
	@ApiMethod
	boolean cleanFolderNoChecks(
			Path folderPath,
			boolean verbose);
}
