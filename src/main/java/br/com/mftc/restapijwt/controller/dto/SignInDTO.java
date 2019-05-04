package br.com.mftc.restapijwt.controller.dto;

import java.io.Serializable;

public class SignInDTO implements Serializable {

	private static final long serialVersionUID = -9214515054250396408L;

	private String email;

	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
