package de.orchound.selenium.exceptions;

public class UnsupportedServerException extends RuntimeException {
	public UnsupportedServerException(String server) {
		super("Unsupported server: " + server);
	}
}
