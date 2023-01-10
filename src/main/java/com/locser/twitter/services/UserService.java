package com.locser.twitter.services;

import com.locser.twitter.exception.EmailAlreadyTakenException;
import com.locser.twitter.exception.EmailFailToSendException;
import com.locser.twitter.exception.IncorrectVerificationCodeException;
import com.locser.twitter.exception.UserDoesNotExistException;
import com.locser.twitter.models.ApplicationUser;
import com.locser.twitter.models.RegisterObject;
import com.locser.twitter.models.Role;
import com.locser.twitter.repositories.RoleRepository;
import com.locser.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser registerUser(RegisterObject registerObject) {

        ApplicationUser user = new ApplicationUser();

        user.setFirstName(registerObject.getFirstName());
        user.setLastName(registerObject.getLastName());
        user.setEmail(registerObject.getEmail());
        user.setDateOfBirth(registerObject.getDob());

        String name = user.getFirstName() + user.getLastName();

        boolean nameTaken = true;

        String tempName = "";

        while (nameTaken) {
            tempName = generateUserName(name);

            if (userRepo.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }
        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();

        roles.add(roleRepo.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }

    }

    public ApplicationUser getUserByUserName(String userName) {
        return userRepo.findByUsername(userName).orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            throw new UserDoesNotExistException();
        }
    }


    private String generateUserName(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name + generatedNumber;
    }

    public void generateEmailVerification(String username) {

        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        user.setVerification(generateVerificationNumber());
        try {

            mailService.sendEmail(user.getEmail(), "Your vertification code", "Here is your verification code: " + user.getVerification());
            userRepo.save(user);
        } catch (Exception e) {
            throw new EmailFailToSendException();
        }

        userRepo.save(user);
    }

    private Long generateVerificationNumber() {
        return (long) Math.floor(Math.random() * 100_000_000);
    }

    public ApplicationUser verifyEmail(String username, Long code) {
        //get user  with username
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        if (code.equals(user.getVerification())) {
            user.setEnable(true);
            user.setVerification(null);
            return userRepo.save(user);
        } else {
            throw new IncorrectVerificationCodeException();
        }
    }

    public ApplicationUser setPassword(String username, String password) {
        ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

        String encodedPassword = passwordEncoder.encode(password);

        user.setPassword(encodedPassword);

        return userRepo.save(user);

    }
}
