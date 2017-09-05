package com.jModule.util;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Adapted from Graham King's "Non-Blocking Console Input in Python and Java"
 * https://www.darkcoding.net/software/non-blocking-console-io-is-not-possible/
 * 
 * Toggles the terminal between raw input mode (reading input character by
 * character) and regular input mode (reading input line by line)
 * 
 * @author Pierce Kelaita
 * @version 1.0.2
 */
public class ConsoleUtil {

	private static String ttyConfig;

	/**
	 * Sets terminal to raw input mode
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void setTerminalRawInput() throws IOException, InterruptedException {
		ttyConfig = stty("-g");
		stty("-icanon min 1");
		stty("-echo");
	}

	/**
	 * Sets terminal to regular input mode
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void setTerminalRegularInput() throws IOException, InterruptedException {
		stty(ttyConfig.trim());
	}

	/**
	 * Execute the stty command with the specified arguments against the current
	 * active terminal.
	 */
	private static String stty(final String args) throws IOException, InterruptedException {
		String cmd = "stty " + args + " < /dev/tty";

		return exec(new String[] { "sh", "-c", cmd });
	}

	/**
	 * Execute the specified command and return the output (both stdout and stderr).
	 */
	private static String exec(final String[] cmd) throws IOException, InterruptedException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Process p = Runtime.getRuntime().exec(cmd);
		InputStream in = p.getInputStream();

		int c;
		while ((c = in.read()) != -1) {
			bout.write(c);
		}

		String result = new String(bout.toByteArray());
		return result;
	}

}