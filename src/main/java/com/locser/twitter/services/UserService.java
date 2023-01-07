package com.locser.twitter.services;

import com.locser.twitter.exception.EmailAlreadyTakenException;
import com.locser.twitter.models.ApplicationUser;
import com.locser.twitter.models.RegisterObject;
import com.locser.twitter.models.Role;
import com.locser.twitter.repositories.RoleRepository;
import com.locser.twitter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Autowired
    public UserService(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public ApplicationUser registerUser(RegisterObject registerObject) {

        ApplicationUser user = new ApplicationUser();

        user.setFirstName(registerObject.getFirstName());
        user.setLastName(registerObject.getLastName());
        user.setEmail(registerObject.getEmail());
        user.setDateOfBirth(registerObject.getDob());

        String name = user.getFirstName() + user.getLastName();

        boolean nameTaken =  true;

        String tempName = "";

        while (nameTaken) {
            tempName = generateUserName(name);

            if(userRepo.findByUsername(tempName).isEmpty()){
                nameTaken = false;
            }
        }

        user.setUsername(tempName);

        Set<Role> roles = user.getAuthorities();

        roles.add(roleRepo.findByAuthority("USER").get());
        user.setAuthorities(roles);

        try{
            return userRepo.save(user);
        }catch (Exception e ) {
            throw new EmailAlreadyTakenException();
        }

    }

    private String generateUserName(String name){
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
        return name + generatedNumber;
    }
}
