
package com.example.expo.Repository;

import com.example.expo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByMerchant_Id(Integer merchantId);

    Product findProductById(Integer id);

}
