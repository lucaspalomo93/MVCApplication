package com.challenge.challenge.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.challenge.models.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long>{
    
    Role findByName(String name);
    boolean existsByName(String name);
}
