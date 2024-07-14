package com.objectville.userservice.services;

import com.objectville.userservice.exceptions.UserNotFoundException;
import com.objectville.userservice.models.Token;
import com.objectville.userservice.models.User;
import com.objectville.userservice.repositories.TokenRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.objectville.userservice.repositories.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    UserService(UserRepository userRepository,
                BCryptPasswordEncoder bCryptPasswordEncoder,
                TokenRepository tokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public User signup(String name, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);
        return userRepository.save(user);
    }

    public Token login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with email id: " + email);
        }

        User user = optionalUser.get();
        if (!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new UserNotFoundException("Invalid password");
        }

        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);
        return token;
    }

    public void logout(String tokenValue) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeleted(tokenValue, false);
        if (optionalToken.isEmpty()) {
            throw new UserNotFoundException("Invalid token");
        }

        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
    }

    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false,new Date());
        if (optionalToken.isEmpty()) {
            throw new UserNotFoundException("Invalid token");
        }
        return optionalToken.get().getUser();
    }
}
