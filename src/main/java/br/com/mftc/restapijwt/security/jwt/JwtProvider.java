package br.com.mftc.restapijwt.security.jwt;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.mftc.restapijwt.security.service.UserPrinciple;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

	private String secret = "KJHfjhgfaFJHGafhgasdwyiuyGJHGJFJHjgfhgfdhsafdhag";

	private int expiration = 86400000;

	public String generate(Authentication authentication) {
		UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

		return generate(userPrincipal.getUsername());
	}
	
	public String generate(String username) {
		final Date now = new Date();

		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(now.getTime() + expiration)).signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public boolean validate(String authToken) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);

			return true;
		} catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException
				| IllegalArgumentException exception) {
			return false;
		}
	}

	public String getEmail(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}

}
