package com.utils.string.junit;

import java.util.Map;

import com.utils.annotations.ApiMethod;

public final class JUnitUtils {

	private JUnitUtils() {
	}

	@ApiMethod
	public static boolean isJUnitTest() {

		boolean jUnitTest = false;
		final Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		for (final StackTraceElement[] stackTrace : allStackTraces.values()) {

			for (final StackTraceElement stackTraceElement : stackTrace) {

				final String clsName = stackTraceElement.getClassName();
				if (clsName.startsWith("org.junit.")) {

					jUnitTest = true;
					break;
				}
			}
			if (jUnitTest) {
				break;
			}
		}
		return jUnitTest;
	}
}
