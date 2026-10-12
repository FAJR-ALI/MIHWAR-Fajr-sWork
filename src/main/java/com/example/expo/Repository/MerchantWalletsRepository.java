package com.example.expo.Repository;

import com.example.expo.Model.MerchantWallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantWalletsRepository extends JpaRepository<MerchantWallets ,Integer> {
    MerchantWallets findMerchantWalletsById(Integer id);
    boolean existsByMerchant_Id(Integer merchantId);

}