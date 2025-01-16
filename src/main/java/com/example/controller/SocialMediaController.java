package com.example.controller;

import com.example.service.MessageService;
import com.example.service.AccountService;
import com.example.entity.Account;
import com.example.entity.Message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){           //dependency injection
        this.accountService = accountService;
        this.messageService = messageService;
    }

/*## 1: Our API should be able to process new User registrations.
As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.  */

    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        
        return accountService.createAccount(account);

    }

/*## 2: Our API should be able to process User logins.
As a user, I should be able to verify my login on the endpoint POST localhost:8080/login.  */

    @PostMapping("/login")
    public ResponseEntity<Account> verifyLogin(@RequestBody Account account){
        
        return accountService.verifyLogin(account);
        
    }

/*## 3: Our API should be able to process the creation of new messages.
As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages.*/

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        
        return messageService.createMessage(message);

    }

/* ## 4: Our API should be able to retrieve all messages.
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
*/

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);

    }

/*## 5: Our API should be able to retrieve a message by its ID.
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId} */

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        
        return messageService.getMessageById(messageId);

    }

/*## 6: Our API should be able to delete a message identified by a message ID.
As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}. */

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Object> deleteMessage(@PathVariable Integer messageId) {
        
        return messageService.deleteMessageById(messageId);

    }

/*## 7: Our API should be able to update a message text identified by a message ID.
As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. */

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Object> updateMessageById(@PathVariable Integer messageId, @RequestBody Message newMessageText) {
        
        return messageService.updateMessageById(messageId, newMessageText.getMessageText());

    }

/*## 8: Our API should be able to retrieve all messages written by a particular user.
As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages. */

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId){
        
        return messageService.getAllMessagesByAccountId(accountId);

    }

}     //last curly bracket