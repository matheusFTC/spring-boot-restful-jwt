package br.com.mftc.restapijwt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.com.mftc.restapijwt.model.dto.PhoneDTO;
import br.com.mftc.restapijwt.model.dto.UserDTO;
import br.com.mftc.restapijwt.repository.UserRepository;
import br.com.mftc.restapijwt.security.jwt.JwtTokenProvider;

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
	public ResponseEntity<?> signup(@RequestBody UserDTO userDto) {
		User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), encoder.encode(userDto.getPassword()));
		
		if (userDto.getPhones() != null && userDto.getPhones().size() > 0) {			
			List<Phone> phones = new ArrayList<Phone>();
			
			for (PhoneDTO phoneDto : userDto.getPhones()) {
				phones.add(new Phone(phoneDto.getNumber(), phoneDto.getAreaCode(), phoneDto.getCountryCode()));
			}
			
			user.setPhones(phones);
		}
		
		userRepository.save(user);
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
        
        Map<Object, Object> model = new HashMap<>();
        
        model.put("token", token);
        
        return ResponseEntity.ok(model);
	}
}
