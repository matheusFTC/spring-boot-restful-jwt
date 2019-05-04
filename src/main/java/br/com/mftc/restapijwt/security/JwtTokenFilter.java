package br.com.mftc.restapijwt.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mftc.restapijwt.controller.dto.ErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
    	
    	String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
    	
    	ObjectMapper mapper = new ObjectMapper();

    	HttpServletResponse unauthorizedResponse = (HttpServletResponse) response;
    	
    	response.setContentType("application/javascript");
    	unauthorizedResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    	
    	final String url = ((HttpServletRequest) request).getRequestURL().toString();
        
        try {
            Authentication auth = jwtTokenProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (IllegalArgumentException exception) {
        	if (!url.contains("/rest/signup") && !url.contains("/rest/signin")) unauthorizedResponse.getWriter().write(mapper.writeValueAsString(new ErrorDTO(5, "Unauthorized")));
		} catch (SignatureException exception) {
			unauthorizedResponse.getWriter().write(mapper.writeValueAsString(new ErrorDTO(6, "Unauthorized - invalid token")));
        } catch (ExpiredJwtException exception) {
        	unauthorizedResponse.getWriter().write(mapper.writeValueAsString(new ErrorDTO(7, "Unauthorized - invalid session")));
		} catch (UsernameNotFoundException exception) {
			unauthorizedResponse.getWriter().write(mapper.writeValueAsString(new ErrorDTO(8, "Unauthorized - unrealized authentication")));
		}
        
        filterChain.doFilter(request, response);
    }
}
