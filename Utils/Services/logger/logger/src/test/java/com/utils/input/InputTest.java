package com.utils.input;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

public class InputTest {

	@Test
	void testReadAndPrintInput() {

		final JTextField jTextField = new JTextField();
		final int option = JOptionPane.showConfirmDialog(null, jTextField, "Enter text:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {

			final String text = jTextField.getText();
			Logger.printLine("text: " + text);
		}
	}

	@Test
	void testReadAndPrintPassword() {

		final JPasswordField jPasswordField = new JPasswordField();
		final int option = JOptionPane.showConfirmDialog(null, jPasswordField, "Enter password:",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {

			final char[] passwordCharArray = jPasswordField.getPassword();
			final String password = new String(passwordCharArray);
			Logger.printLine("password: " + password);
		}
	}
}
