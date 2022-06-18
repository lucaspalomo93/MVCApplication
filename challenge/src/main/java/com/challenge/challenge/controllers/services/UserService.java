package com.challenge.challenge.controllers.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.challenge.models.Operator;
import com.challenge.challenge.models.Role;
import com.challenge.challenge.models.repositories.IOperatorRepository;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private IOperatorRepository operatorRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Operator operator = operatorRepository.findByUserName(username);

        if (operator == null) {
            throw new UsernameNotFoundException(username);
        }

        var roles = new ArrayList<GrantedAuthority>();

        for (Role role : operator.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new User(operator.getUserName(), operator.getPassword(), roles);
    }

}
