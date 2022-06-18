package com.challenge.challenge.controllers.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.challenge.challenge.models.Operator;
import com.challenge.challenge.models.Role;

@Service
public class DisplayService {

    /*
     * We check a list of Operators lookinf for the Operator with Super Admin Role
     * We Remove it from the list.
     * Finally we return the list without the Super Admin.
     */

    public List<Operator> showAllWithoutSuperAdmin(List<Operator> operators) {

        for (Operator op : operators) {
            List<Role> roles = op.getRoles();

            boolean flag = false;
            for (Role role : roles) {
                if (role.getName().equals("ROLE_SUPER_ADMIN")) {
                    operators.remove(op);
                    flag = true;
                }
            }
            if (flag) {
                break;
            }
        }

        return operators;
    }

}
