package com.tz.quiz.support;

import java.io.PrintWriter;

import com.tz.quiz.support.Constants;

public class Logger extends PrintWriter {

	private PrintWriter out = null;
	private StringBuffer logs = null;

	/**
	 * <pre>
	 * constructor for logger
	 * </pre>
	 */
	public Logger() {
		super(System.out, true);
		out = new PrintWriter(System.out);
		logs = new StringBuffer();
	}

	/**
	 * <pre>
	 * append logs to StringBuffer
	 * </pre>
	 * 
	 * @param strInput
	 */
	public void println(String strInput) {
		synchronized (lock) {
			logs.append(strInput + "\n");
		}
	}

	/**
	 * <pre>
	 * prinlnt StringBuffer to console
	 * </pre>
	 * 
	 */
	public void flush() {
		synchronized (lock) {
			out.print(logs);
			out.flush();
			logs = new StringBuffer();
		}
	}

	/**
	 * <pre>
	 * print log to console for debugging
	 * </pre>
	 * 
	 * @param strInput
	 */
	public static void debug(String strInput) {
		if (Constants.debug) {
			System.out.println("\t" + strInput);
		}
	}

}
