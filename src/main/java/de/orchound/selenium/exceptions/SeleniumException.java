package de.orchound.selenium.exceptions;

/**
 * Created by kevin on 13.05.16.
 */
public class SeleniumException extends RuntimeException {
	public SeleniumException() {
		super();
	}

	public SeleniumException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SeleniumException(String message) {
		super(message);
	}

	public SeleniumException(Throwable throwable) {
		super(throwable);
	}
}
