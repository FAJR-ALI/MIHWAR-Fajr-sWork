
package com.example.expo.Service;

import com.example.expo.Api.ApiException;
import com.example.expo.Model.Merchants;
import com.example.expo.Model.Product;
import com.example.expo.Repository.MerchantRepository;
import com.example.expo.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByMerchant(Integer merchantId) {

        Merchants merchant =
                merchantRepository.findMerchantById(merchantId);

        if (merchant == null) {
            throw new ApiException("Merchant not found");
        }

        return productRepository.findByMerchant_Id(merchantId);
    }

    public Product getProductById(Integer productId) {

        Product product = productRepository.findProductById(productId);

        if (product == null) {
            throw new ApiException("Product not found");
        }

        return product;
    }

    public void addProduct(Integer merchantId, Product product) {

        Merchants merchant =
                merchantRepository.findMerchantById(merchantId);

        if (merchant == null) {
            throw new ApiException("Merchant not found");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new ApiException("Product name is required");
        }

        if (product.getPrice() == null
                || product.getPrice().signum() < 0) {
            throw new ApiException("Product price must be zero or greater");
        }

        product.setId(null);
        product.setMerchant(merchant);

        if (product.getAvailable() == null) {
            product.setAvailable(true);
        }

        productRepository.save(product);
    }

    public void updateProduct(Integer productId, Product request) {

        Product oldProduct = getProductById(productId);

        if (request.getName() == null || request.getName().isBlank()) {
            throw new ApiException("Product name is required");
        }

        if (request.getPrice() == null
                || request.getPrice().signum() < 0) {
            throw new ApiException("Product price must be zero or greater");
        }

        oldProduct.setName(request.getName());
        oldProduct.setDescription(request.getDescription());
        oldProduct.setPrice(request.getPrice());
        oldProduct.setCategory(request.getCategory());
        oldProduct.setImageUrl(request.getImageUrl());

        if (request.getAvailable() != null) {
            oldProduct.setAvailable(request.getAvailable());
        }

        productRepository.save(oldProduct);
    }

    public void deleteProduct(Integer productId) {

        Product product = getProductById(productId);

        productRepository.delete(product);
    }
}
