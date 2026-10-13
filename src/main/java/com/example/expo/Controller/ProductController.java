
package com.example.expo.Controller;

import com.example.expo.Model.Product;
import com.example.expo.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/merchant/{merchantId}")
    public List<Product> getProductsByMerchant(
            @PathVariable Integer merchantId) {

        return productService.getProductsByMerchant(merchantId);
    }

    @GetMapping("/{productId}")
    public Product getProductById(
            @PathVariable Integer productId) {

        return productService.getProductById(productId);
    }

    @PostMapping("/add/{merchantId}")
    public void addProduct(
            @PathVariable Integer merchantId,
            @Valid @RequestBody Product product) {

        productService.addProduct(merchantId, product);
    }

    @PutMapping("/update/{productId}")
    public void updateProduct(
            @PathVariable Integer productId,
            @Valid @RequestBody Product product) {

        productService.updateProduct(productId, product);
    }

    @DeleteMapping("/delete/{productId}")
    public void deleteProduct(
            @PathVariable Integer productId) {

        productService.deleteProduct(productId);
    }
}
