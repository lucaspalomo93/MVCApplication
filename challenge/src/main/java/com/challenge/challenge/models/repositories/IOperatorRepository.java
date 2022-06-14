package com.challenge.challenge.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.challenge.models.Operator;

@Repository
public interface IOperatorRepository extends JpaRepository<Operator, Long>{
    

    public Operator findByUserName(String username);

    public boolean existsByUserName(String username);
}
