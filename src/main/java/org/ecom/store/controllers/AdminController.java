package org.ecom.store.controllers;

import lombok.RequiredArgsConstructor;
import org.ecom.store.models.Product;
import org.ecom.store.models.Item;
import org.ecom.store.services.StoreService;
import org.ecom.store.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ADMIN_CONTROLLER)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController extends BaseController{
    private final StoreService storeService;


    @PostMapping(Constants.CREATE_PRODUCT_ENDPOINT)
    public ResponseEntity<Item> create(@RequestBody Item item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(item).orElseThrow(throwsGeneralException));
    }

    @DeleteMapping(Constants.REMOVE_PRODUCT_ENDPOINT)
    public ResponseEntity<Product> remove(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.delete(id).orElseThrow(throwsGeneralException));
    }
}
