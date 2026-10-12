package com.example.expo.Repository;

import com.example.expo.Model.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoicesRepository extends JpaRepository<Invoices,Integer> {
    boolean existsByMerchant_Id(Integer id); //يتحقق من وجود فاتورة بالاي دي الخاص بالتاجر


    Invoices findInvoiceById(Integer invoiceId);
}