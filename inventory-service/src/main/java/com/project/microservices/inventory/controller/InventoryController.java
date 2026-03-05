package com.project.microservices.inventory.controller;

import com.project.microservices.inventory.model.Inventory;
import com.project.microservices.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addInventory(@RequestParam String skuCode, @RequestParam Integer quantity) {
        inventoryService.addInventory(skuCode, quantity);
     }


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateInventory(@RequestParam String skuCode, @RequestParam Integer quantity) {
         inventoryService.updateInventory(skuCode, quantity);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllInventory() {
        return inventoryService.getAllInventory();
    }
}