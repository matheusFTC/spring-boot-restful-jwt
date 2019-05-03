package br.com.mftc.restapijwt.controller.response;

public class JwtResponse {

	private String type = "Bearer";

	private String token;
   
    private String username;
    
    public JwtResponse(String accessToken, String username) {
        this.token = accessToken;
        this.username = username;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
}
