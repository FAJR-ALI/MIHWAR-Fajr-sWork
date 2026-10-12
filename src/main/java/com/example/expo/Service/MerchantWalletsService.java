package com.example.expo.Service;

import com.example.expo.Api.ApiException;
import com.example.expo.DTO.MerchantWalletResponse;
import com.example.expo.Model.MerchantWallets;
import com.example.expo.Repository.MerchantWalletsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantWalletsService {

    private final MerchantWalletsRepository merchantWalletsRepository;

//get all
    public List<MerchantWalletResponse> getAll() {
        List<MerchantWallets> wallets = merchantWalletsRepository.findAll();
        List<MerchantWalletResponse> responses = new ArrayList<>();

        for (int i = 0; i < wallets.size(); i++) {//مشينا ع كل الميرشنت ولت واضفناها داخل الريسبونس واعدنا فقط القيم الي نحتاجها في الرسيبونس
            responses.add(toResponse(wallets.get(i)));
        }

        return responses;
    }

//اسندت القيم الي بالمحفظة الى القيم بالرسيبونس
    private MerchantWalletResponse toResponse(MerchantWallets wallet) {
        return new MerchantWalletResponse(
                wallet.getId(),
                wallet.getMerchant().getId(),
                wallet.getBalance(),
                wallet.getCurrency(),
                wallet.getStatus()
        );
    }


    public void deleteWallet(Integer walletId) {
        MerchantWallets wallet = merchantWalletsRepository.findMerchantWalletsById(walletId);

        if (wallet == null) {
            throw new ApiException("wallet id not found");
        }
        //bigdecimal.zero يقارن حتى بعدد المنازل وليس فقط الرقم
        if (wallet.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ApiException("cannot delete wallet with a non-zero balance");
        }

        merchantWalletsRepository.delete(wallet);
    }


}