package com.meelin.core.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoreBaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String lnSpt = System.getProperty("line.separator");
	private List<Throwable> rootExceptions = new ArrayList<Throwable>();
	private String messageKey = null;
	private Object[] messageArgs = null;

	public CoreBaseException() {
	}

	public CoreBaseException(Throwable rootCause) {
		super(rootCause);
		this.rootExceptions.add(rootCause);
	}

	public CoreBaseException(String message, Throwable rootCause) {
		super(message, rootCause);
		this.rootExceptions.add(rootCause);
	}

	public CoreBaseException(String message) {
		super(message);
	}

	public List<Throwable> getRootExceptions() {
		return this.rootExceptions;
	}

	public synchronized void addRootException(Throwable ex) {
		if ((getCause() == null) && (this.rootExceptions.size() < 1)) {
			initCause(ex);
		}
		this.rootExceptions.add(ex);
	}

	public void setMessageKey(String key) {
		this.messageKey = key;
	}

	public String getMessageKey() {
		return this.messageKey;
	}

	public void setMessageArgs(Object[] args) {
		this.messageArgs = args;
	}

	public Object[] getMessageArgs() {
		return this.messageArgs;
	}

	public void printStackTrace() {
		printStackTrace(System.err);
	}

	public void printStackTrace(PrintStream outStream) {
		printStackTrace(new PrintWriter(outStream));
	}

	public void printStackTrace(PrintWriter writer) {
		writer.append(lnSpt);

		StackTraceElement[] elements = onlyPrintStackTrace(writer, this, 0);
		if (this.rootExceptions.size() > 0) {
			printRootExceptions(writer, this.rootExceptions,
					elements.length - 1);
		}
		writer.flush();
	}

	protected StackTraceElement[] onlyPrintStackTrace(PrintWriter writer,
			Throwable exception, int b) {
		String t = lnSpt + "\t";
		StackTraceElement[] elements = exception.getStackTrace();
		writer.append(exception.getClass().getName() + ": "
				+ exception.getMessage());
		for (int i = 0; i < elements.length - b; i++) {
			writer.append(t + "at " + elements[i].getClassName() + "."
					+ elements[i].getMethodName() + "("
					+ elements[i].getFileName() + ":"
					+ elements[i].getLineNumber() + ")");
		}
		if (b > 0) {
			writer.append(t + "... " + b + " more");
		}
		return elements;
	}

	protected void printRootExceptions(PrintWriter writer,
			List<Throwable> rootExceptions, int b) {
		int i = 1;
		Iterator<Throwable> iter = rootExceptions.iterator();
		while (iter.hasNext()) {
			Throwable rootException = (Throwable) iter.next();
			writer.append(lnSpt + "Caused By " + i++ + " ");

			StackTraceElement[] stackTraceElements = onlyPrintStackTrace(
					writer, rootException, b);
			if ((rootException instanceof CoreBaseException)) {
				CoreBaseException newRootException = (CoreBaseException) rootException;
				printRootExceptions(writer,
						newRootException.getRootExceptions(),
						stackTraceElements.length - 1);
			} else if (rootException.getCause() != null) {
				List<Throwable> newRootException = new ArrayList<Throwable>();
				newRootException.add(rootException.getCause());
				printRootExceptions(writer, newRootException,
						stackTraceElements.length - 1);
			}
		}
	}
}
