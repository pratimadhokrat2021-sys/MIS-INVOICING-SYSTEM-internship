package com.codeb.ims.controller;

import com.codeb.ims.entity.Chain;
import com.codeb.ims.entity.Estimate;
import com.codeb.ims.entity.Invoice;
import com.codeb.ims.payload.request.InvoiceRequest;
import com.codeb.ims.payload.response.InvoiceResponse;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.repository.ChainRepository;
import com.codeb.ims.repository.EstimateRepository;
import com.codeb.ims.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    EstimateRepository estimateRepository;

    @Autowired
    ChainRepository chainRepository;

    @GetMapping
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findByIsActiveTrue().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<InvoiceResponse> searchInvoices(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllInvoices();
        }
        return invoiceRepository.searchInvoices(query).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {
        if (request.getEstimatedId() == null || request.getChainId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Estimated ID and Chain ID are required!"));
        }

        Optional<Estimate> estimateOpt = estimateRepository.findById(request.getEstimatedId());
        Optional<Chain> chainOpt = chainRepository.findById(request.getChainId());

        if (!estimateOpt.isPresent() || !chainOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Estimate or Chain not found!"));
        }

        Invoice invoice = new Invoice();
        // Generate random 4-digit invoice no
        invoice.setInvoiceNo(1000 + new Random().nextInt(9000));
        
        invoice.setEstimate(estimateOpt.get());
        invoice.setChain(chainOpt.get());
        invoice.setServiceDetails(request.getServiceDetails());
        invoice.setQty(request.getQty());
        invoice.setCostPerQty(request.getCostPerQty());
        invoice.setAmountPayable(request.getAmountPayable());
        
        // Compute balance based on amount paid
        Double paid = request.getAmountPaid() != null ? request.getAmountPaid() : 0.0;
        Double payable = request.getAmountPayable() != null ? request.getAmountPayable() : 0.0;
        invoice.setBalance(payable - paid);
        invoice.setDateOfPayment(LocalDateTime.now());
        
        invoice.setDateOfService(request.getDateOfService());
        invoice.setDeliveryDetails(request.getDeliveryDetails());
        invoice.setEmailId(request.getEmailId());
        invoice.setIsActive(true);

        invoiceRepository.save(invoice);
        return ResponseEntity.ok(new MessageResponse("Invoice generated successfully! (PDF and Email simulated)"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequest request) {
        Optional<Invoice> invoiceData = invoiceRepository.findById(id);

        if (invoiceData.isPresent()) {
            Invoice invoice = invoiceData.get();
            // Only emailId is editable as per requirements for update invoice
            if (request.getEmailId() != null) {
                invoice.setEmailId(request.getEmailId());
            }

            invoiceRepository.save(invoice);
            return ResponseEntity.ok(new MessageResponse("Invoice updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        Optional<Invoice> invoiceData = invoiceRepository.findById(id);

        if (invoiceData.isPresent()) {
            Invoice invoice = invoiceData.get();
            invoice.setIsActive(false); // Soft delete
            invoiceRepository.save(invoice);
            return ResponseEntity.ok(new MessageResponse("Invoice deleted successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceNo(invoice.getInvoiceNo());
        response.setEstimatedId(invoice.getEstimate().getEstimatedId());
        response.setChainId(invoice.getChain().getChainId());
        response.setCompanyName(invoice.getChain().getCompanyName());
        response.setServiceDetails(invoice.getServiceDetails());
        response.setQty(invoice.getQty());
        response.setCostPerQty(invoice.getCostPerQty());
        response.setAmountPayable(invoice.getAmountPayable());
        response.setBalance(invoice.getBalance());
        
        // Calculate abstract amountPaid back for the UI
        if(invoice.getAmountPayable() != null && invoice.getBalance() != null) {
            response.setAmountPaid(invoice.getAmountPayable() - invoice.getBalance());
        }

        response.setDateOfPayment(invoice.getDateOfPayment());
        response.setDateOfService(invoice.getDateOfService());
        response.setDeliveryDetails(invoice.getDeliveryDetails());
        response.setEmailId(invoice.getEmailId());
        response.setIsActive(invoice.getIsActive());
        return response;
    }
}
