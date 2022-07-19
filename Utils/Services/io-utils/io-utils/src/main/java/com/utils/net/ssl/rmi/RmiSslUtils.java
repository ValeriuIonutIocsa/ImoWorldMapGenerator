package com.utils.net.ssl.rmi;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import com.utils.io.IoUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;

public final class RmiSslUtils {

	private RmiSslUtils() {
	}

	public static void configureSettingsResources(
			final String password,
			final String keyStoreResourceFilePath,
			final String trustStoreResourceFilePath) {

		try {
			final String keyStorePathString = copyCertificateResources(keyStoreResourceFilePath);
			final String trustStorePathString = copyCertificateResources(trustStoreResourceFilePath);

			configureSettingsCommon(password, keyStorePathString, trustStorePathString);

		} catch (final Exception exc) {
			Logger.printError("failed to configure RMI SSL settings");
			Logger.printException(exc);
		}
	}

	private static String copyCertificateResources(
			final String certificateResourceFilePath) throws Exception {

		final Path certificatePath = Paths.get(SystemUtils.USER_HOME,
				"JavaCertificates", certificateResourceFilePath);
		if (!IoUtils.fileExists(certificatePath)) {

			FactoryFolderCreator.getInstance().createParentDirectories(certificatePath, true);

			try (InputStream inputStream = IoUtils.resourceFileToInputStream(certificateResourceFilePath);
					OutputStream outputStream = Files.newOutputStream(certificatePath)) {
				IOUtils.copy(inputStream, outputStream);
			}

		} else {
			try (InputStream inputStream = IoUtils.resourceFileToInputStream(certificateResourceFilePath);
					OutputStream outputStream = Files.newOutputStream(certificatePath)) {
				IOUtils.copy(inputStream, outputStream);
			} catch (final Exception ignored) {
			}
		}
		return certificatePath.toString();
	}

	public static void configureSettingsByteArrays(
			final String password,
			final byte[] keyStoreByteArray,
			final String keyStoreResourceFilePath,
			final byte[] trustStoreByteArray,
			final String trustStoreResourceFilePath) {

		try {
			final String keyStorePathString =
					copyCertificateString(keyStoreByteArray, keyStoreResourceFilePath);
			final String trustStorePathString =
					copyCertificateString(trustStoreByteArray, trustStoreResourceFilePath);

			configureSettingsCommon(password, keyStorePathString, trustStorePathString);

		} catch (final Exception exc) {
			Logger.printError("failed to configure RMI SSL settings");
			Logger.printException(exc);
		}
	}

	private static String copyCertificateString(
			final byte[] certificateByteArray,
			final String certificateResourceFilePath) throws Exception {

		final Path certificatePath = Paths.get(SystemUtils.USER_HOME,
				"JavaCertificates", certificateResourceFilePath);
		if (!IoUtils.fileExists(certificatePath)) {

			FactoryFolderCreator.getInstance().createParentDirectories(certificatePath, true);

			Files.write(certificatePath, certificateByteArray);

		} else {
			try {
				Files.write(certificatePath, certificateByteArray);
			} catch (final Exception ignored) {
			}
		}
		return certificatePath.toString();
	}

	public static void configureSettingsCommon(
			final String password,
			final String keyStorePathString,
			final String trustStorePathString) {

		System.setProperty("javax.net.ssl.debug", "all");

		System.setProperty("javax.net.ssl.keyStore", keyStorePathString);
		System.setProperty("javax.net.ssl.keyStorePassword", password);

		System.setProperty("javax.net.ssl.trustStore", trustStorePathString);
		System.setProperty("javax.net.ssl.trustStorePassword", password);
	}
}
