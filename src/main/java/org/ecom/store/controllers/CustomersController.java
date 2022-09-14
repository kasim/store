package org.ecom.store.controllers;

import lombok.RequiredArgsConstructor;
import org.ecom.store.models.Basket;
import org.ecom.store.models.Receipt;
import org.ecom.store.models.UserItem;
import org.ecom.store.services.UsersService;
import org.ecom.store.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping(Constants.CUSTOMER_CONTROLLER)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomersController extends BaseController{
    private final UsersService usersService;

    @PostMapping(Constants.ADD_ITEM_ENDPOINT)
    public ResponseEntity<Basket> addItem(@RequestBody UserItem userItem) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.addItem(userItem).orElseThrow(throwsGeneralException));
    }

    @DeleteMapping(Constants.REMOVE_ITEM_ENDPOINT)
    public ResponseEntity<Basket> removeItem(@RequestParam long userId, @RequestParam long productId, @RequestParam BigInteger quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(usersService.removeItem(userId, productId, quantity).orElseThrow(throwsGeneralException));
    }

    @GetMapping(Constants.CHECKOUT_ENDPOINT)
    public ResponseEntity<Receipt> checkout(@RequestParam long userId){
        return ResponseEntity.status(HttpStatus.OK).body(usersService.checkout(userId).orElseThrow(throwsGeneralException));
    }
}
