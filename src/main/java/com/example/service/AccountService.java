package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

/*## 1: Our API should be able to process new User registrations.

As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an accountId.

- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
- If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
- If the registration is not successful for some other reason, the response status should be 400. (Client error)
 */

    public ResponseEntity<Account> createAccount(Account account){          //checking for user/pass not null, pass length
        
        if (account.getUsername().equals("") || account.getPassword().equals("") || account.getPassword().length() < 4){
            return ResponseEntity.status(400).body(null);
        }

        if(accountRepository.existsByUsername(account.getUsername())){                    //checking for duplicate username
            return ResponseEntity.status(409).body(null);
        }

        Account savedAccount = accountRepository.save(account);               //creates account
        return ResponseEntity.status(HttpStatus.OK).body(savedAccount);

    }


/*## 2: Our API should be able to process User logins.

As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account.

- The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its accountId. The response status should be 200 OK, which is the default.
- If the login is not successful, the response status should be 401. (Unauthorized)

 */

    public ResponseEntity<Account> verifyLogin(Account account){
        
        String username = account.getUsername();
        String password = account.getPassword();
        Account authenticatedAccount = accountRepository.searchByUsernameAndPassword(username, password);

        if(authenticatedAccount == null || username == null || password == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        
        return ResponseEntity.ok(authenticatedAccount);
        
    }

}     //last curly bracket
