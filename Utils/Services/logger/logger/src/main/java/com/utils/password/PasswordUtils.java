package com.utils.password;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public final class PasswordUtils {

	private PasswordUtils() {
	}

	public static String inputPassword() {

		final String password;
		final JPasswordField jPasswordField = new JPasswordField();
		final int option = JOptionPane.showConfirmDialog(null, jPasswordField, "Enter password:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {

			final char[] passwordCharArray = jPasswordField.getPassword();
			password = new String(passwordCharArray);

		} else {
			password = null;
		}
		return password;
	}
}
