package com.objectville.userservice.services;

import com.objectville.userservice.models.Token;
import com.objectville.userservice.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.objectville.userservice.repositories.UserRepository;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    UserService (UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }




    public User signup(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);


//        save the user obj to the database
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        return null;
    }

    public void logout(String token) {

    }

    public User validateToken(String token) {
        return null;
    }
}
