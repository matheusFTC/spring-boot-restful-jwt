package br.com.mftc.restapijwt.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mftc.restapijwt.controller.dto.ErrorDTO;
import br.com.mftc.restapijwt.controller.dto.PhoneDTO;
import br.com.mftc.restapijwt.controller.dto.SignInDTO;
import br.com.mftc.restapijwt.controller.dto.UserDTO;
import br.com.mftc.restapijwt.model.Phone;
import br.com.mftc.restapijwt.model.User;
import br.com.mftc.restapijwt.repository.UserRepository;
import br.com.mftc.restapijwt.security.JwtTokenProvider;

@RestController
@RequestMapping("/rest")
public class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody UserDTO userDto) {
		ResponseEntity<?> response;

		if (StringUtils.isBlank(userDto.getFirstName())
				|| StringUtils.isBlank(userDto.getLastName())
				|| StringUtils.isBlank(userDto.getEmail())
				|| StringUtils.isBlank(userDto.getPassword())) {
			response = ResponseEntity.badRequest().body(new ErrorDTO(1, "Missing fields"));
		} else if (!userDto.validateEmail()) {
			response = ResponseEntity.badRequest().body(new ErrorDTO(2, "Invalid fields"));
		} else if (userRepository.existsByEmail(userDto.getEmail())) {
			response = ResponseEntity.badRequest().body(new ErrorDTO(3, "E-mail already exists"));
		} else {
			User user = new User(userDto.getFirstName()
					, userDto.getLastName()
					, userDto.getEmail()
					, encoder.encode(userDto.getPassword()));

			if (userDto.getPhones() != null) {				
				List<Phone> phones = userDto.getPhones().stream().map(phoneDto ->
					new Phone(phoneDto.getNumber()
							, phoneDto.getAreaCode()
							, phoneDto.getCountryCode())).collect(Collectors.toList());
				
				user.setPhones(phones);
			}
			
			user.setCreatedAt(new Date());

			userRepository.save(user);

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			
			String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

			Map<Object, Object> model = new HashMap<>();

			model.put("token", token);

			response = ResponseEntity.ok(model);
		}

		return response;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDto) {
		ResponseEntity<?> response;
		
		if (StringUtils.isBlank(signInDto.getEmail())
				|| StringUtils.isBlank(signInDto.getPassword())) {
			response = ResponseEntity.badRequest().body(new ErrorDTO(1, "Missing fields"));
		} else {
			try {
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword()));
				
				User user = userRepository.findByEmail(signInDto.getEmail()).get();
				
				user.setLastLogin(new Date());

				userRepository.save(user);
				
				String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
				
				Map<Object, Object> model = new HashMap<>();
				
				model.put("token", token);
				
				response = ResponseEntity.ok(model);
			} catch (BadCredentialsException exception) {				
				response = ResponseEntity.badRequest().body(new ErrorDTO(4, "Invalid e-mail or password"));
			}
		}
		
		return response;
	}
	
	@GetMapping("/me")
    public ResponseEntity<?> me() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User user = (User) principal;
		
		UserDTO dto = new UserDTO(user.getFirstName()
				, user.getLastName()
				, user.getEmail()
				, null
				, user.getPhones().stream().map(phone ->
					new PhoneDTO(phone.getNumber()
							, phone.getAreaCode()
							, phone.getCountryCode())).collect(Collectors.toList())
				, user.getCreatedAt()
				, user.getLastLogin());
		
		return ResponseEntity.ok(dto);
    }
}
