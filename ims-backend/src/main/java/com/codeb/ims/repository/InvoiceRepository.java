package com.codeb.ims.repository;

import com.codeb.ims.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByIsActiveTrue();

    @Query("SELECT i FROM Invoice i WHERE i.isActive = true AND " +
           "(CAST(i.invoiceNo AS string) LIKE %:searchTerm% OR " +
           "CAST(i.estimate.estimatedId AS string) LIKE %:searchTerm% OR " +
           "CAST(i.chain.chainId AS string) LIKE %:searchTerm% OR " +
           "LOWER(i.chain.companyName) LIKE LOWER(CONCAT('%', :searchTerm%, '%')))")
    List<Invoice> searchInvoices(@Param("searchTerm") String searchTerm);
}
