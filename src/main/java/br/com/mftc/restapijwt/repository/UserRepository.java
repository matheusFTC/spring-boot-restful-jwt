package br.com.mftc.restapijwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mftc.restapijwt.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	User findByEmailAndPassword(String email, String password);
	
	Boolean existsByEmail(String email);
}
