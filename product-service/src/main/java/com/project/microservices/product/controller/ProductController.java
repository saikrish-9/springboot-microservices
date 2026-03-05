package com.project.microservices.product.controller;

import com.project.microservices.product.dto.ProductRequest;
import com.project.microservices.product.dto.ProductResponse;
import com.project.microservices.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/sku/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductBySkuCode(@PathVariable String skuCode) {
        return productService.getProductBySkuCode(skuCode);
    }

    @PutMapping("/sku/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProductBySkuCode(@PathVariable String skuCode, @RequestBody ProductRequest productRequest) {
        return productService.updateProductBySkuCode(skuCode, productRequest);
    }

    @DeleteMapping("/sku/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> deleteProductBySkuCode(@PathVariable String skuCode) {
        return productService.deleteProductBySkuCode(skuCode);
    }
}