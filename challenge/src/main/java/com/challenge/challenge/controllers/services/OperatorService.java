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

    // *************** AUTOWIREDS *******************//
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IOperatorRepository operatorRepository;

    @Autowired
    private IRoleRepository roleRepository;

    // ************ FIND ALL OPERATORS***********//
    public List<Operator> findAll() {
        return operatorRepository.findAll();
    }

    // ************ FIND OPERATOR BY ID ***********//
    public Optional<Operator> findById(Long id) {
        if (operatorRepository.existsById(id)) {
            return operatorRepository.findById(id);
        }
        return null;
    }

    // ************ SAVE OPERATOR ***********//
    public Operator save(Operator operator) {

        /*
         * If the operator is not null:
         * Set the Creation Date, Set the Status to 1, Encode the password and
         * Capitalize the name and surname.
         * Then create a new Role, and save that role in a list. Set the list of roles
         * of the Operator.
         * Finally, save the new operator.
         */
        if (operator != null) {
            if (!operatorRepository.existsByUserName(operator.getUserName())) {
                operator.setCreationDate(new Date(System.currentTimeMillis()));
                operator.setStatus(1);
                operator.setPassword(passwordEncoder.encode(operator.getPassword()));
                operator.setName(operator.getName().substring(0, 1).toUpperCase() + operator.getName().substring(1));
                operator.setSurname(
                        operator.getSurname().substring(0, 1).toUpperCase() + operator.getSurname().substring(1));

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

    // ************ UPDATE OPERATOR ***********//
    public Operator update(Long id, Operator operator) {

        /*
         * If the Id exists:
         * Get the Operator by Id
         * Set every field with the Operator passed by parameter.
         * Encode the password.
         * Save the updated Operator.
         */

        if (operatorRepository.existsById(id)) {
            Operator oldOp = operatorRepository.findById(id).get();
            oldOp.setName(operator.getName().substring(0, 1).toUpperCase() + operator.getName().substring(1));
            oldOp.setSurname(operator.getSurname().substring(0, 1).toUpperCase() + operator.getSurname().substring(1));
            oldOp.setUserName(operator.getUserName());
            oldOp.setPassword(passwordEncoder.encode(operator.getPassword()));
            oldOp.setStatus(operator.getStatus());
            oldOp.setLastLoginDate(oldOp.getLastLoginDate());
            operatorRepository.save(oldOp);
            return oldOp;
        }

        return null;
    }

    // ************ DELETE OPERATOR BY ID ***********//
    public boolean deleteById(Long id) {

        /*
         * If the Id exists:
         * Get the Operator by id and delete the roles.
         * Finally, delete the Operator.
         */

        if (operatorRepository.existsById(id)) {
            Operator op = operatorRepository.findById(id).get();

            List<Role> roles = op.getRoles();

            roles.forEach(role -> {
                roleRepository.delete(role);
            });

            operatorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ************ FIND BY USERNAME ***********//
    public Operator findByusername(String username) {
        if (operatorRepository.existsByUserName(username)) {
            return operatorRepository.findByUserName(username);
        }
        return null;
    }

    // ************ UPDATE LAST LOGIN DATE ***********//
    public Operator updateLastLoginDate(Operator operator) {

        /*
         * If the Id exists:
         * Get the operator by id.
         * Set the Last Login Date to current time.
         * Finally, save the updated Operator.
         */

        if (operatorRepository.existsById(operator.getId())) {
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

    // ************ UPGRADE OPERATOR TO ADMIN ***********//
    public Operator upgrade(Long id) {

        /*
         * Get the Operator by Id.
         * If the Operator doesnt have Admin Role we mark the flag as false.
         * If the flag is false, we create a new Admin Role, save it. And add it to the
         * Operator.
         * Finally, we save the Operator.
         */

        boolean flag = false;

        Operator oldOp = operatorRepository.findById(id).get();

        oldOp.setName(oldOp.getName());
        oldOp.setSurname(oldOp.getSurname());
        oldOp.setUserName(oldOp.getUserName());
        oldOp.setPassword((oldOp.getPassword()));
        oldOp.setStatus(oldOp.getStatus());
        oldOp.setLastLoginDate(oldOp.getLastLoginDate());

        List<Role> roles = oldOp.getRoles();

        for (Role role : oldOp.getRoles()) {
            if (role.getName().toString().equals("ROLE_ADMIN")) {
                flag = true;
            }
        }

        if (!flag) {
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

    // ************ DEMOTE OPERATOR TO USER ***********//
    public Operator demote(Long id) {

        /*
         * Find Operator by Id
         * We look if the Operator has the Role Admin.
         * If the role exists, we delete it.
         */

        Operator oldOp = operatorRepository.findById(id).get();

        oldOp.setName(oldOp.getName());
        oldOp.setSurname(oldOp.getSurname());
        oldOp.setUserName(oldOp.getUserName());
        oldOp.setPassword((oldOp.getPassword()));
        oldOp.setStatus(oldOp.getStatus());
        oldOp.setLastLoginDate(oldOp.getLastLoginDate());

        for (Role role : oldOp.getRoles()) {
            if (role.getName().toString().equals("ROLE_ADMIN")) {
                roleRepository.delete(role);
                return oldOp;
            }
        }

        return null;
    }

    // ************ CHECK THE OPERATOR ROLE ***********//
    public boolean checkRole(Operator operator, String roleName) {

        /*
         * Checks if the Operator has an specific role.
         * Then returns true or false.
         */

        boolean flag = false;
        for (Role role : operator.getRoles()) {
            if (role.getName().toString().equals(roleName)) {
                flag = true;
            }
        }

        if (flag) {
            return true;

        } else {
            return false;
        }
    }

}