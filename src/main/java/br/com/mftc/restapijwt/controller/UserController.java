package br.com.mftc.restapijwt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mftc.restapijwt.controller.response.JwtResponse;
import br.com.mftc.restapijwt.model.Phone;
import br.com.mftc.restapijwt.model.User;
import br.com.mftc.restapijwt.model.dto.PhoneDTO;
import br.com.mftc.restapijwt.model.dto.UserDTO;
import br.com.mftc.restapijwt.repository.UserRepository;
import br.com.mftc.restapijwt.security.jwt.JwtProvider;

@RestController
@RequestMapping("/rest")
public class UserController {

	//@Autowired
    //private AuthenticationManager authenticationManager;
	
	@Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtProvider jwtProvider;
	
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
		
        String jwt = jwtProvider.generate(user.getEmail());

        return ResponseEntity.ok(new JwtResponse(jwt, user.getEmail()));
	}
}
