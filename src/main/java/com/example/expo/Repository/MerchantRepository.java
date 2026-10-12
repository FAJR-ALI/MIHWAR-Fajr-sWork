package com.example.expo.Repository;

import com.example.expo.Model.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchants, Integer> {
    Merchants findMerchantById(Integer id);
}