package com.xyz.apiproxy.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xyz.apiproxy.security.model.Account;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	AccountRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userRepository.findByUsername(username)
				.map(account -> new User(account.getUsername(), bcryptEncoder.encode(account.getPassword()),
						account.getActive(), account.getActive(), account.getActive(), account.getActive(),
						getAuthorities(account)))
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Account user) {
		String[] userRoles = user.getRoles().stream().map((role) -> "ROLE_" + role.getName()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}
}
