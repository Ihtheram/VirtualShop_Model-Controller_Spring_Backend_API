package com.revature.virtualshop.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.virtualshop.models.Account;
import com.revature.virtualshop.repositories.AccountDAO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

@Service
public class AccountService {

    private final AccountDAO accDao;
    private Object updatedRole;
    private Object updatedRole1;

    @Autowired
    public AccountService(AccountDAO accountDAO) {
        this.accDao = accountDAO;
    }

    public Account createNewAccount(Account newAccInfo) {
        accDao.getReferenceByUsername(newAccInfo.getUsername())
                .ifPresent(account -> {
                    throw new EntityExistsException("Username already exists");
                });
        newAccInfo.setRole(accDao.findAll().isEmpty() ? "ADMIN" : "USER");
        return accDao.save(newAccInfo);
    }

    public Account logIntoAccount(Account loginAccInfo) throws FailedLoginException {
        return accDao.getReferenceByUsername(loginAccInfo.getUsername())
                .filter(account -> account.getPassword().equals(loginAccInfo.getPassword()))
                .orElseThrow(() -> new FailedLoginException("Invalid username or password"));
    }

    public List<Account> getAllAccounts(Account account) throws Exception {

        if(account != null && account.getRole().equals("ADMIN")){
            return accDao.findAll();
        } else {
            throw new Exception("You are not authorized to view this page");
        }
    }

    public Account updateAccount(Account requesterAcc, long idOfAccToBeUpdated, JsonNode accInfoToBeUpdated) throws Exception {
        Account accountToBeUpdated = accDao.findById(idOfAccToBeUpdated)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        if(requesterAcc.getRole().equals("ADMIN")) {
            if(accInfoToBeUpdated.has("role")) {
                accountToBeUpdated.setRole(accInfoToBeUpdated.get("role").asText());
            }
            if(accInfoToBeUpdated.has("status")) {
                accountToBeUpdated.setStatus(accInfoToBeUpdated.get("status").asText());
            }
            return accDao.save(accountToBeUpdated);

        } else if(requesterAcc.getId() == accountToBeUpdated.getId()){
            if(accInfoToBeUpdated.has("username")) {
                accountToBeUpdated.setUsername(accInfoToBeUpdated.get("username").asText());
            }if(accInfoToBeUpdated.has("password")) {
                accountToBeUpdated.setPassword(accInfoToBeUpdated.get("password").asText());
            }if(accInfoToBeUpdated.has("email")) {
                accountToBeUpdated.setEmail(accInfoToBeUpdated.get("email").asText());
            }if(accInfoToBeUpdated.has("image")) {
                accountToBeUpdated.setImage(accInfoToBeUpdated.get("image").asText());
            }if(accInfoToBeUpdated.has("status")) {
                if (accInfoToBeUpdated.get("status").asText().equals("ACTIVE") || accInfoToBeUpdated.get("status").asText().equals("DEACTIVATED")) {
                    accountToBeUpdated.setStatus(accInfoToBeUpdated.get("status").asText());
                }
            }
            return accDao.save(accountToBeUpdated);
        } else {
            throw new Exception("You are not authorized to update this account");
        }
    }

    public void deleteAccount(Account requesterAcc, long idOfAccToBeDeleted) throws Exception {
        Account accountToBeDeleted = accDao.findById(idOfAccToBeDeleted)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        if (requesterAcc.getRole().equals("ADMIN") || requesterAcc.getId() == accountToBeDeleted.getId()) {
            accDao.delete(accountToBeDeleted);
        } else {
            throw new Exception("You are not authorized to delete this account");
        }
    }

}
