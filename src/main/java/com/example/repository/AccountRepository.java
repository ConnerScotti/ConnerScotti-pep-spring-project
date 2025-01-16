package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByAccountId(Integer id);
    Account searchByUsernameAndPassword(String username, String password);

    public Boolean existsByUsername(String username);
    public Optional<Account> findOneByUsername(String username);

}      //last curly bracket
