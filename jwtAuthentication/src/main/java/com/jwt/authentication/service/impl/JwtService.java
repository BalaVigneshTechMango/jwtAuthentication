package com.jwt.authentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jwt.authentication.entity.User;
import com.jwt.authentication.repository.UserRepository;
import com.jwt.authentication.request.TokenRefreshRequest;
import com.jwt.authentication.response.JwtResponse;
import com.jwt.authentication.response.TokenRefreshResponse;
import com.jwt.authentication.util.JwtUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService implements UserDetailsService {
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userDao;

	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtResponse signinToken(User user) throws Exception {
		String userName = user.getUserName();
		String userPassword = user.getUserPassword();
		authenticate(userName, userPassword);

		UserDetails userDetails = loadUserByUsername(userName);
		String newGeneratedToken = jwtUtil.generateToken(userDetails);

		user = userDao.findByUserName(userName);
		return new JwtResponse(user, newGeneratedToken);
	}
	
	public TokenRefreshResponse getNewAccessToken(TokenRefreshRequest request) {
		String newAccessToken=jwtUtil.getNewAccessToken(request);
		return new TokenRefreshResponse(newAccessToken);	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUserName(username);

		if (user != null) {
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
					getAuthorities(user));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities = Arrays.stream(user.getAuthenticaterole().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		return authorities;
	}

	private void authenticate(String userName, String userPassword) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
