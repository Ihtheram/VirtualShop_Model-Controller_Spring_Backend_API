package com.revature.virtualshop.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.virtualshop.models.Account;
import com.revature.virtualshop.models.Product;
import com.revature.virtualshop.repositories.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Service
public class ProductService {

    private ProductDAO prodDao;

    @Autowired
    public ProductService(ProductDAO productDAO) {
        this.prodDao = productDAO;
    }
    public Product createNewProduct(Account requesterAcc, Product newProdInfo) throws Exception {
        if(requesterAcc.getRole().equals("ADMIN")) {
            return prodDao.save(newProdInfo);
        } else {
            throw new Exception("You are not authorized to create a product");
        }
    }

    public List<Product> getAllProducts(Account account) throws Exception {

        if(account != null && account.getRole().equals("ADMIN")){
            return prodDao.findAll();
        } else {
            throw new Exception("You are not authorized to view this page");
        }
    }


    public Product updateProduct(Account requesterAcc, long idOfProdToBeUpdated, JsonNode prodInfoToBeUpdated) {

        Product productToBeUpdated = prodDao.findById(idOfProdToBeUpdated)
                .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));

        if(requesterAcc.getRole().equals("ADMIN")) {
            if(prodInfoToBeUpdated.has("name")) {
                productToBeUpdated.setName(prodInfoToBeUpdated.get("name").asText());
            }
            if(prodInfoToBeUpdated.has("description")) {
                productToBeUpdated.setDescription(prodInfoToBeUpdated.get("description").asText());
            }
            if(prodInfoToBeUpdated.has("price")) {
                productToBeUpdated.setPrice(prodInfoToBeUpdated.get("price").asDouble());
            }
            if(prodInfoToBeUpdated.has("stock")) {
                productToBeUpdated.setStock(prodInfoToBeUpdated.get("stock").asInt());
            }
            if(prodInfoToBeUpdated.has("image")) {
                productToBeUpdated.setImage(prodInfoToBeUpdated.get("image").asText());
            }
        }
        return prodDao.save(productToBeUpdated);
    }

    public void deleteProduct(Account requesterAcc, long idOfProdToBeDeleted) throws Exception {
            Product productToBeDeleted = prodDao.findById(idOfProdToBeDeleted)
                    .orElseThrow(() -> new EntityNotFoundException("Product does not exist"));

            if (requesterAcc.getRole().equals("ADMIN")) {
                prodDao.delete(productToBeDeleted);
            } else {
                throw new Exception("You are not authorized to delete this account");
            }
    }
}
