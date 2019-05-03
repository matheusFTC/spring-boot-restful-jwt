package br.com.mftc.restapijwt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mftc.restapijwt.model.Phone;
import br.com.mftc.restapijwt.model.User;
import br.com.mftc.restapijwt.model.dto.UserDTO;
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

	public static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public static boolean validateEmail(String email) {
		return EMAIL_REGEX.matcher(email).find();
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserDTO userDto) {
		ResponseEntity<?> response;

		if (StringUtils.isBlank(userDto.getFirstName())
				|| StringUtils.isBlank(userDto.getLastName())
				|| StringUtils.isBlank(userDto.getEmail())
				|| StringUtils.isBlank(userDto.getPassword())) {
			response = ResponseEntity.badRequest().body(new ErrorMessage(1, "Missing fields"));
		} else if (!validateEmail(userDto.getEmail())) {
			response = ResponseEntity.badRequest().body(new ErrorMessage(2, "Invalid fields"));
		} else if (userRepository.existsByEmail(userDto.getEmail())) {
			response = ResponseEntity.badRequest().body(new ErrorMessage(3, "E-mail already exists"));
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

			userRepository.save(user);

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
			String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

			Map<Object, Object> model = new HashMap<>();

			model.put("token", token);

			response = ResponseEntity.ok(model);
		}

		return response;
	}
}
