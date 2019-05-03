package br.com.mftc.restapijwt.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 4913924865650984215L;

	public InvalidJwtAuthenticationException(String exception) {
        super(exception);
    }
}
