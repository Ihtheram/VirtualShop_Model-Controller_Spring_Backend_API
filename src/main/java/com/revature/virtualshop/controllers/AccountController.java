package com.revature.virtualshop.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.virtualshop.models.Account;
import com.revature.virtualshop.services.AccountService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;

import static org.springframework.http.HttpStatus.*;


@RestController
@RequestMapping("accounts")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
public class AccountController {

    private final AccountService accSvc;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accSvc = accountService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerNewUserHandler(@RequestBody Account submittedAccInfo){
        Account newAccount;
        try{
            newAccount = accSvc.createNewAccount(submittedAccInfo);
        } catch (EntityExistsException e){
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }
        return new ResponseEntity<>(newAccount, CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> loginHandler(@RequestBody Account loginAccInfo, HttpSession session) {
        Account account;
        try {
            account = accSvc.logIntoAccount(loginAccInfo);
            session.setAttribute("account", account); // Store the user in the session
        } catch (FailedLoginException e) {
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
        return new ResponseEntity<>(account, OK);
    }

    @GetMapping("logout")
    public ResponseEntity<?> logoutHandler(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("Logged out successfully!", OK);
    }


    // Validate Session
    @GetMapping("session")
    public ResponseEntity<?> validateSession(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        System.out.println(account);
        if (account == null) {
            return new ResponseEntity<>("Not logged in!", UNAUTHORIZED);
        }
        return new ResponseEntity<>(account, OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return new ResponseEntity<>(accSvc.getAllAccounts(account), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long idOfAccToBeUpdated, @RequestBody JsonNode accInfoToBeUpdated, HttpSession session) {
        Account requesterAcc = (Account) session.getAttribute("account");
        Account updatedAcc;
        try {
            updatedAcc = accSvc.updateAccount(requesterAcc, idOfAccToBeUpdated, accInfoToBeUpdated);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), OK);
        }
        return new ResponseEntity<>(updatedAcc, OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable("id") long idOfAccToBeDeleted, HttpSession session) {
        Account requesterAcc = (Account) session.getAttribute("account");
        try {
            accSvc.deleteAccount(requesterAcc, idOfAccToBeDeleted);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), OK);
        }
        return new ResponseEntity<>("Account deleted successfully", OK);
    }

}
