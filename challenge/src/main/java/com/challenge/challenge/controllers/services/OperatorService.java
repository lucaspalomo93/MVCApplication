package com.challenge.challenge.controllers.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.challenge.challenge.models.repositories.IOperatorRepository;
import com.challenge.challenge.models.repositories.IRoleRepository;
import com.challenge.challenge.models.Operator;
import com.challenge.challenge.models.Role;

@Service
public class OperatorService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private IOperatorRepository operatorRepository;


    @Autowired
    private IRoleRepository roleRepository;

    public List<Operator> findAll(){
        return operatorRepository.findAll();
    }

    public Optional<Operator> findById(Long id){
        if(operatorRepository.existsById(id)){
            return operatorRepository.findById(id);
        }
        return null;
    }

    public Operator save(Operator operator){



        if(operator != null){
            if(!operatorRepository.existsByUserName(operator.getUserName())){
                operator.setCreationDate(new Date(System.currentTimeMillis()));
                operator.setStatus(1);
                operator.setPassword(passwordEncoder.encode(operator.getPassword()));


                Role role = new Role();
                role.setName("ROLE_USER");
        
                roleRepository.save(role);
        
                List<Role> roles = new ArrayList<>();
                roles.add(role);

                operator.setRoles(roles);

                operatorRepository.save(operator);
                return operator;
            }
        }
        return null;
    }

    public Operator update(Long id, Operator operator){
        if(operatorRepository.existsById(id)){
            Operator oldOp = operatorRepository.findById(id).get();
            System.out.println(oldOp);
            oldOp.setName(operator.getName());
            oldOp.setSurname(operator.getSurname());
            oldOp.setUserName(operator.getUserName());
            oldOp.setPassword(passwordEncoder.encode(operator.getPassword()));
            oldOp.setStatus(operator.getStatus());
            oldOp.setLastLoginDate(oldOp.getLastLoginDate());
            operatorRepository.save(oldOp);
            return oldOp;
        }
        
        return null;
    }

    public boolean deleteById(Long id){
        if(operatorRepository.existsById(id)){
            Operator op = operatorRepository.findById(id).get();

            List<Role> roles = op.getRoles();

            roles.forEach(role ->{
                roleRepository.delete(role);
            });

            operatorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Operator findByusername(String username){
        if(operatorRepository.existsByUserName(username)){
            return operatorRepository.findByUserName(username);
        }
        return null;
    }

    public Operator updateLastLoginDate(Operator operator){
        if(operatorRepository.existsById(operator.getId())){
            Operator oldOp = operatorRepository.findById(operator.getId()).get();

            oldOp.setName(operator.getName());
            oldOp.setSurname(operator.getSurname());
            oldOp.setUserName(operator.getUserName());
            oldOp.setPassword((operator.getPassword()));
            oldOp.setStatus(operator.getStatus());
            oldOp.setLastLoginDate(new Date(System.currentTimeMillis()));

            operatorRepository.save(oldOp);
            return oldOp;
        }
        return null;
    }

    public Operator upgrade(Long id){
        
        boolean flag = false;

        Operator oldOp = operatorRepository.findById(id).get();

        oldOp.setName(oldOp.getName());
        oldOp.setSurname(oldOp.getSurname());
        oldOp.setUserName(oldOp.getUserName());
        oldOp.setPassword((oldOp.getPassword()));
        oldOp.setStatus(oldOp.getStatus());
        oldOp.setLastLoginDate(oldOp.getLastLoginDate());


        List<Role> roles = oldOp.getRoles();

        for(Role role : oldOp.getRoles()){
            if(role.getName().toString().equals("ROLE_ADMIN")){
                flag = true;
            }
        }

        if(!flag){
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);

            roles.add(role);

            oldOp.setRoles(roles);

            operatorRepository.save(oldOp);

            return oldOp;
        }

        return null;
    }

    public Operator demote(Long id){


        Operator oldOp = operatorRepository.findById(id).get();

        oldOp.setName(oldOp.getName());
        oldOp.setSurname(oldOp.getSurname());
        oldOp.setUserName(oldOp.getUserName());
        oldOp.setPassword((oldOp.getPassword()));
        oldOp.setStatus(oldOp.getStatus());
        oldOp.setLastLoginDate(oldOp.getLastLoginDate());


        for(Role role : oldOp.getRoles()){
            if(role.getName().toString().equals("ROLE_ADMIN")){
                roleRepository.delete(role);
                return oldOp;
            }
        }

        return null;
    }
    
}