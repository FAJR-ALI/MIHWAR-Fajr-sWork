package com.example.expo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor

//للعرض
public class MerchantWalletResponse {

    private Long id;
    private Integer merchant_id;
    private BigDecimal balance;
    private String currency;
    private String status;
}