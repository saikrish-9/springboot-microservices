package com.project.microservices.inventory.service;

import com.project.microservices.inventory.model.Inventory;
import com.project.microservices.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode, Integer quantity) {
        log.info(" Start -- Received request to check stock for skuCode {}, with quantity {}", skuCode, quantity);
        boolean isInStock = inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
        log.info(" End -- Product with skuCode {}, and quantity {}, is in stock - {}", skuCode, quantity, isInStock);
        return isInStock;
    }

    public void updateInventory(String skuCode, Integer quantity) {
        log.info(" Start -- Reducing inventory for skuCode {}, with quantity {}", skuCode, quantity);

        var inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new IllegalArgumentException("Product with skuCode " + skuCode + " not found"));

        if (inventory.getQuantity() < -1 * quantity) {
            throw new IllegalArgumentException("Insufficient quantity in stock for skuCode " + skuCode);
        }

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
        log.info(" End -- Inventory reduced for skuCode {}, remaining quantity is {}", skuCode, inventory.getQuantity());

    }

    public void addInventory(String skuCode, Integer quantity) {
        inventoryRepository.save(new Inventory(skuCode, quantity));
    }

    public List<String> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(Inventory::getSkuCode)
                .toList();
    }
}
