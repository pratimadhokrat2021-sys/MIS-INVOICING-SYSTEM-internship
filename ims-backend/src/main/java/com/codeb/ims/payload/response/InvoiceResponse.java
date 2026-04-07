package com.codeb.ims.payload.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvoiceResponse {
    private Long id;
    private Integer invoiceNo;
    private Long estimatedId;
    private Integer chainId;
    private String companyName;
    private String serviceDetails;
    private Integer qty;
    private Double costPerQty;
    private Double amountPayable;
    private Double balance;
    private Double amountPaid;
    private LocalDateTime dateOfPayment;
    private LocalDate dateOfService;
    private String deliveryDetails;
    private String emailId;
    private Boolean isActive;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(Integer invoiceNo) { this.invoiceNo = invoiceNo; }

    public Long getEstimatedId() { return estimatedId; }
    public void setEstimatedId(Long estimatedId) { this.estimatedId = estimatedId; }

    public Integer getChainId() { return chainId; }
    public void setChainId(Integer chainId) { this.chainId = chainId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Double getCostPerQty() { return costPerQty; }
    public void setCostPerQty(Double costPerQty) { this.costPerQty = costPerQty; }

    public Double getAmountPayable() { return amountPayable; }
    public void setAmountPayable(Double amountPayable) { this.amountPayable = amountPayable; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public LocalDateTime getDateOfPayment() { return dateOfPayment; }
    public void setDateOfPayment(LocalDateTime dateOfPayment) { this.dateOfPayment = dateOfPayment; }

    public LocalDate getDateOfService() { return dateOfService; }
    public void setDateOfService(LocalDate dateOfService) { this.dateOfService = dateOfService; }

    public String getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(String deliveryDetails) { this.deliveryDetails = deliveryDetails; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
