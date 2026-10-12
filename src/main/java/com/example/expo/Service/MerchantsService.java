package com.example.expo.Service;

import com.example.expo.Api.ApiException;
import com.example.expo.DTO.MerchantRequest;
import com.example.expo.Model.Merchants;
import com.example.expo.Repository.InvoicesRepository;
import com.example.expo.Repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantsService {
    private final MerchantRepository merchantRepository;
    private final InvoicesRepository invoicesRepository;


    public List<Merchants> getAll()
    {
        return merchantRepository.findAll();
    }


    public void addMerchant(MerchantRequest request) {
        Merchants merchant = new Merchants();

        merchant.setBusiness_name(request.getBusiness_name());
        merchant.setEmail(request.getEmail());
        merchant.setPhone(request.getPhone());
        merchant.setPassword(request.getPassword());
        merchant.setCommercial_register(request.getCommercial_register());

        // قيم يحددها النظام.
        merchant.setVerification_status("PENDING");
        merchant.setStatus("ACTIVE");

        LocalDate today = LocalDate.now();
        merchant.setCreated_at(today);
        merchant.setUpdated_at(today);
        merchantRepository.save(merchant);


    }

    public void updateMerchant(Integer merchantId, MerchantRequest request) {
        Merchants oldMerchant =
                merchantRepository.findMerchantById(merchantId);

        if (oldMerchant == null) {
            throw new ApiException("merchant id not found");
        }

        oldMerchant.setBusiness_name(request.getBusiness_name());
        oldMerchant.setEmail(request.getEmail());
        oldMerchant.setPhone(request.getPhone());
        oldMerchant.setPassword(request.getPassword());
        oldMerchant.setCommercial_register(request.getCommercial_register());

        // تغيير بيانات التسجيل يلغي التوثيق السابق.
        if ("VERIFIED".equals(oldMerchant.getVerification_status())) {
            oldMerchant.setVerification_status("PENDING");
        }

        // لا نغيّر created_at أو status من هذا الطلب.
        oldMerchant.setUpdated_at(LocalDate.now());

        merchantRepository.save(oldMerchant);
    }

    public void deleteMerchant(Integer merchantId){
        Merchants oldMerchant= merchantRepository.findMerchantById(merchantId);

        if(oldMerchant==null){
            throw new ApiException("merchant id not found");
        }
        if(invoicesRepository.existsByMerchant_Id(merchantId)){
            throw new ApiException("can not delete merchant with invoices");
        }
        merchantRepository.delete(oldMerchant);
    }



}