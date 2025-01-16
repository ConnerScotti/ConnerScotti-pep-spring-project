package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;

    }

/*## 3: Our API should be able to process the creation of new messages.

As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a messageId.

- The creation of the message will be successful if and only if the messageText is not blank, is not over 255 characters, and postedBy refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its messageId. The response status should be 200, which is the default. The new message should be persisted to the database.
- If the creation of the message is not successful, the response status should be 400. (Client error)
 */
    public ResponseEntity<Message> createMessage(Message message){

        String username = getUsernameByAccountId(message.getPostedBy());

        if(message.getMessageText() == null || message.getMessageText().length() > 255 || message.getPostedBy() == null || username == null || !accountRepository.existsByUsername(username) || message.getMessageText().isBlank()){
            return ResponseEntity.status(400).body(null);         //checks for not blank message, length < 255, posted by existing user
        }

        Message savedMessage = messageRepository.save(message);
        return ResponseEntity.status(HttpStatus.OK).body(savedMessage);

    }

    public String getUsernameByAccountId(Integer accountId){
        
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null){
            return account.getUsername();
        } else {
            return null;
        }

    }

 /*
## 4: Our API should be able to retrieve all messages.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

- The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
 */

    public List<Message> getAllMessages() {
        
        return messageRepository.findAll();
    
    }

/*## 5: Our API should be able to retrieve a message by its ID.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.

- The response body should contain a JSON representation of the message identified by the messageId. It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.
 */

    public ResponseEntity<Message> getMessageById(Integer messageId){

        Message message = messageRepository.findById(messageId).orElse(null);
        
        if (message != null){                          //found message
            return ResponseEntity.ok(message);
        } else {                                          //didnt find message
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

    }

/*## 6: Our API should be able to delete a message identified by a message ID.

As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}.

- The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the number of rows updated (1). The response status should be 200, which is the default.
- If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
 */

    public ResponseEntity<Object> deleteMessageById(Integer messageId) {

        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.status(HttpStatus.OK).body("1");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }

    }

/* ## 7: Our API should be able to update a message text identified by a message ID.

As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. The request body should contain a new messageText values to replace the message identified by messageId. The request body can not be guaranteed to contain any other information.

- The update of a message should be successful if and only if the message id already exists and the new messageText is not blank and is not over 255 characters. If the update is successful, the response body should contain the number of rows updated (1), and the response status should be 200, which is the default. The message existing on the database should have the updated messageText.
- If the update of the message is not successful for any reason, the response status should be 400. (Client error)
*/

    public ResponseEntity<Object> updateMessageById(Integer messageId, String newMessageText){

        Optional<Message> messageOptional = messageRepository.findById(messageId);                        
        if (newMessageText == null || newMessageText.length() > 255 || !messageOptional.isPresent() || newMessageText.isBlank()){           //check if message text meets conditions
            return ResponseEntity.status(400).body(null);
        }

        Message message = messageOptional.get();                           //retrive message object
        message.setMessageText(newMessageText);                           //update message text
        messageRepository.save(message);                                 //save the updated message text
        return ResponseEntity.ok().body("1");                      //return response

    }

/*## 8: Our API should be able to retrieve all messages written by a particular user.

As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.

- The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
 */

    public ResponseEntity<List<Message>> getAllMessagesByAccountId(Integer accountId){

        List<Message> messages = messageRepository.findByPostedBy(accountId);
        return ResponseEntity.ok(messages);

    }

}     // last curly bracket