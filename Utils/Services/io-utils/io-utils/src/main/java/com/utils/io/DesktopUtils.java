package com.utils.io;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

import com.utils.annotations.ApiMethod;
import com.utils.log.Logger;

public final class DesktopUtils {

	private DesktopUtils() {
	}

	@ApiMethod
	public static void tryBrowse(
			final String url) {

		try {
			final Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(url));

		} catch (final Exception exc) {
			Logger.printError("failed to browse url:" + System.lineSeparator() + url);
			Logger.printException(exc);
		}
	}

	@ApiMethod
	public static void tryOpen(
			final File file) {

		try {
			final Desktop desktop = Desktop.getDesktop();
			desktop.open(file);

		} catch (final Exception exc) {
			Logger.printError("failed to open file:" + System.lineSeparator() + file);
			Logger.printException(exc);
		}
	}
}
