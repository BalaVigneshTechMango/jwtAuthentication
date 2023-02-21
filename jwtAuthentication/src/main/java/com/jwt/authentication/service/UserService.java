package com.jwt.authentication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.authentication.entity.User;
import com.jwt.authentication.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userDao.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    public List<User> getAllUserDetails(){
    	return userDao.findAll();
    }
    
    public User findUserDetailsById(int userId) {
    	return userDao.findById(userId).get();
    }
    
    public User updateUserDetailsById(int userId,User user) {
    	User findUser=userDao.findById(userId).get();
    	findUser.setUserName(user.getUserName());
    	findUser.setUserFirstName(user.getUserFirstName());
    	findUser.setUserLastName(user.getUserLastName());
    	findUser.setUserPassword(getEncodedPassword(user.getUserPassword()) );
    	findUser.setAuthenticaterole(user.getAuthenticaterole());
    	User updateUser=userDao.save(findUser);
    	return updateUser;
    }
    
    public void deletUserDetailsById(int userId) {
    	userDao.deleteById(userId);
    }
}
