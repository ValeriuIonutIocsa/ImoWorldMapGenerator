package com.utils.net.proxy.settings;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.utils.crypt.EncryptionUtils;
import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FactoryProxySettings {

	private FactoryProxySettings() {
	}

	public static ProxySettings newInstance() {

		ProxySettings proxySettings = null;
		try {
			final Properties properties = new Properties();
			final Path proxySettingsPath = createProxySettingsPath();
			if (IoUtils.fileExists(proxySettingsPath)) {

				Logger.printProgress("parsing proxy settings file:");
				Logger.printLine(proxySettingsPath);

				final Cipher decryptCipher = EncryptionUtils.createDecryptCipher();
				try (InputStream inputStream = new CipherInputStream(
						new BufferedInputStream(Files.newInputStream(proxySettingsPath)), decryptCipher)) {

					properties.load(inputStream);

					final String httpHost = properties.getProperty("httpHost");
					if (StringUtils.isNotBlank(httpHost)) {

						final int httpPort = StrUtils.tryParsePositiveInt(properties.getProperty("httpPort"));
						if (httpPort >= 0) {

							final String httpUsername = properties.getProperty("httpUsername");
							final String httpPassword = properties.getProperty("httpPassword");
							proxySettings = new ProxySettings(httpHost, httpPort, httpUsername, httpPassword);
						}
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to load proxy settings");
			Logger.printException(exc);
		}
		return proxySettings;
	}

	static Path createProxySettingsPath() {
		return Paths.get(SystemUtils.USER_HOME, "IVIProxySettings.encrypted");
	}
}
