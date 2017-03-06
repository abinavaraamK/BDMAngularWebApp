package com.app.framework.exception;

import java.util.HashMap;
import java.io.StringWriter;
import java.io.PrintWriter;

public class HexApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5911347222216193590L;
	private boolean ibIsLoggable;
	private boolean ibIsFormatted = true;
	private String isCode;
	@SuppressWarnings("rawtypes")
	private HashMap ioMessages;
	private Throwable ioCause;
	private String stackTraceString;
	private String csEndLine = System.getProperty("line.separator");
	private String isMessage;

	public static final String DUPLICATE_KEY_EXCEPTION = "DUPLICATE_KEY_EXCEPTION";
	public static final String REQUIRED_DATA_EXCEPTION = "REQUIRED_DATA_EXCEPTION";
	public static final String DATA_INTEGRITY_EXCEPTION = "DATA_INTEGRITY_EXCEPTION";
	public static final String RECORD_NOT_FOUND_EXCEPTION = "RECORD_NOT_FOUND_EXCEPTION";

	public HexApplicationException(String isCode) {
		super(isCode);
		this.isCode = isCode;
	}

	public HexApplicationException(String isCode, Exception exception) {
		super(isCode, exception);
		this.isCode = isCode;
		ioCause = exception;
	}

	/**
	 * @param psMessage
	 *            This represent the Message that has to displayed
	 * @param poCause
	 *            This represent the cause for the error
	 */
	@SuppressWarnings("rawtypes")
	public HexApplicationException(String psCode, HashMap psMessages, Throwable poCause, boolean pbIsLoggable) {
		super(psCode);
		ioMessages = psMessages;
		isCode = psCode;
		ioCause = poCause;
		ibIsLoggable = pbIsLoggable;
		stackTraceString = generateStackTraceString(poCause);
	}

	/**
	 * "@param psMessage This represent the Error Message Code that has to
	 * displayed
	 */
	@SuppressWarnings("rawtypes")
	public HexApplicationException(String psCode, HashMap psMessages, boolean pbIsLoggable) {
		super(psCode);
		isCode = psCode;
		ioMessages = psMessages;
		ibIsLoggable = pbIsLoggable;
	}

	@SuppressWarnings("rawtypes")
	public HexApplicationException(HashMap psMessages, boolean pbIsLoggable) {
		ioMessages = psMessages;
		ibIsLoggable = pbIsLoggable;
	}

	@SuppressWarnings("rawtypes")
	public HexApplicationException(String psCode, HashMap psMessages, Throwable poCause, boolean pbIsLoggable,
			boolean pbIsFormatted) {
		super(psCode);
		ioMessages = psMessages;
		isCode = psCode;
		ioCause = poCause;
		ibIsLoggable = pbIsLoggable;
		ibIsFormatted = pbIsFormatted;
		stackTraceString = generateStackTraceString(poCause);
	}

	/**
	 * "@param psMessage This represent the Error Message Code that has to
	 * displayed
	 */
	@SuppressWarnings("rawtypes")
	public HexApplicationException(String psCode, HashMap psMessages, boolean pbIsLoggable, boolean pbIsFormatted) {
		super(psCode);
		isCode = psCode;
		ioMessages = psMessages;
		ibIsLoggable = pbIsLoggable;
		ibIsFormatted = pbIsFormatted;
	}

	@SuppressWarnings("rawtypes")
	public HexApplicationException(HashMap psMessages, boolean pbIsLoggable, boolean pbIsFormatted) {
		ioMessages = psMessages;
		ibIsLoggable = pbIsLoggable;
		ibIsFormatted = pbIsFormatted;
	}

	/**
	 * "@param poCause This represent the cause for the error
	 */
	public HexApplicationException(Throwable poCause) {
		ioCause = poCause;
		stackTraceString = generateStackTraceString(poCause);
	}

	/**
	 * "@param poCause This represent the cause for the error
	 */
	public HexApplicationException(Exception poCause) {
		ioCause = poCause;
		// isMessage = generateStackTraceString(poCause);
		isMessage = poCause.getMessage();
	}

	/**
	 * This method stores StackTrace Information as a non-transient String for
	 * reuse.
	 *
	 * @param poThrowable
	 */
	public String generateStackTraceString(Exception poThrowable) {

		if (poThrowable == null) {
			StringBuffer lsbMessage = new StringBuffer(csEndLine);
			lsbMessage.append("Exception Is Null");
			lsbMessage.append(csEndLine);
			return lsbMessage.toString();
		}

		StringWriter loTrace = new StringWriter();
		poThrowable.printStackTrace(new PrintWriter(loTrace));
		return loTrace.toString();
	}

	/**
	 * This method stores StackTrace Information as a non-transient String for
	 * reuse.
	 *
	 * @param poThrowable
	 */
	public String generateStackTraceString(Throwable poThrowable) {

		if (poThrowable == null) {
			StringBuffer lsbMessage = new StringBuffer(csEndLine);
			lsbMessage.append("Exception Is Null");
			lsbMessage.append(csEndLine);
			return lsbMessage.toString();
		}

		StringWriter loTrace = new StringWriter();
		poThrowable.printStackTrace(new PrintWriter(loTrace));
		return loTrace.toString();
	}

	/**
	 * This method descends through Linked-List of Nesting Exceptions
	 * Recursively This displays the 'Deepest' StackTrace Last.
	 * 
	 */
	public String getStackTraceString() {

		StringBuffer traceBuffer = new StringBuffer();
		traceBuffer.append(this.generateStackTraceString(this));

		if (ioCause == null) {
			traceBuffer.append(csEndLine);
			return traceBuffer.toString();
		}

		if (ioCause instanceof HexApplicationException) {
			traceBuffer.append(csEndLine);
			traceBuffer.append("____________________NESTED-EXCEPTION___________________");
			traceBuffer.append(csEndLine);
			traceBuffer.append(csEndLine);
			traceBuffer.append(((HexApplicationException) ioCause).getStackTraceString());

		}
		traceBuffer.append(stackTraceString);
		return traceBuffer.toString();
	}

	public void setIsLoggable(boolean pbLog) {
		ibIsLoggable = pbLog;
	}

	public boolean getIsLoggable() {
		return ibIsLoggable;
	}

	public boolean getIsFormatted() {
		return ibIsFormatted;
	}

	public String getMessageCode() {
		return isCode;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getMessages() {
		return ioMessages;
	}

	public Throwable getCause() {
		return ioCause;
	}

	public String getErrorMessage() {
		return (String) ioMessages.get(isCode);
	}

	public String getMessage() {
		return isMessage;
	}
}
