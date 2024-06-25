package com.revature.virtualshop.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.virtualshop.models.Account;
import com.revature.virtualshop.models.Product;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.revature.virtualshop.services.ProductService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
public class ProductController {

    private final ProductService prodSvc;

    @Autowired
    public ProductController(ProductService productService) {
        this.prodSvc = productService;
    }

    @PostMapping
    public ResponseEntity<?> createNewProduct(@RequestBody Product newProdInfo, HttpSession session) {
        Account requesterAcc = (Account) session.getAttribute("account");
        try {
            return new ResponseEntity<>(prodSvc
                    .createNewProduct(requesterAcc, newProdInfo), CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>( e.getMessage(), UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(HttpSession session) {
        try {
            Account account = (Account) session.getAttribute("account");
            return new ResponseEntity<>(prodSvc.getAllProducts(account), OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long idOfProdToBeUpdated, @RequestBody JsonNode prodInfoToBeUpdated, HttpSession session) {
        Account requesterAcc = (Account) session.getAttribute("account");
        Product updatedProd;
        try {
            updatedProd = prodSvc.updateProduct(requesterAcc, idOfProdToBeUpdated, prodInfoToBeUpdated);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), OK);
        }
        return new ResponseEntity<>(updatedProd, OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") long idOfProdToBeDeleted, HttpSession session) {
        Account requesterAcc = (Account) session.getAttribute("account");
        try {
            prodSvc.deleteProduct(requesterAcc, idOfProdToBeDeleted);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), OK);
        }
        return new ResponseEntity<>("Product deleted successfully", OK);
    }


}
