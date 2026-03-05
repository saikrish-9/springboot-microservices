package com.project.microservices.product.service;

import com.project.microservices.product.dto.ProductRequest;
import com.project.microservices.product.dto.ProductResponse;
import com.project.microservices.product.model.Product;
import com.project.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .skuCode(productRequest.skuCode())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product created successfully");
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getSkuCode(),
                product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                        product.getSkuCode(),
                        product.getPrice()))
                .toList();
    }

    public ProductResponse getProductBySkuCode(String skuCode) {
        Product product = productRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> {
                    log.error("Product with SKU code {} not found", skuCode);
                    return new RuntimeException("Product with SKU code " + skuCode + " not found");
                });
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getSkuCode(),
                product.getPrice());
    }


    public ProductResponse updateProductBySkuCode(String skuCode, ProductRequest productRequest) {
        Product product = productRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> {
                    log.error("Product with SKU code {} not found", skuCode);
                    return new RuntimeException("Product with SKU code " + skuCode + " not found");
                });
        product.setName(productRequest.name());
        product.setDescription(productRequest.description());
        product.setSkuCode(productRequest.skuCode());
        product.setPrice(productRequest.price());
        productRepository.save(product);
        log.info("Product with SKU code {} updated successfully", skuCode);
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getSkuCode(),
                product.getPrice());
    }

    public Map<String, Object> deleteProductBySkuCode(String skuCode) {
        Product product = productRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> {
                    log.error("Product with SKU {} not found", skuCode);
                    return new RuntimeException("Product with SKU " + skuCode + " not found");
                });

        productRepository.delete(product); // deletes by _id internally
        log.info("Product with SKU {} deleted successfully (id={})", skuCode, product.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Product deleted successfully");
        response.put("deletedProductSku", skuCode);
        response.put("deletedProductId", product.getId());
        return response;
    }
}
