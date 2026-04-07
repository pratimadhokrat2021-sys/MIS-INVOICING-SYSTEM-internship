package com.codeb.ims.payload.request;

import java.time.LocalDate;

public class InvoiceRequest {
    private Long estimatedId;
    private Integer chainId;
    private String serviceDetails;
    private Integer qty;
    private Double costPerQty;
    private Double amountPayable;
    private Double amountPaid; // Derived field to compute balance
    private String deliveryDetails;
    private String emailId;
    private LocalDate dateOfService;

    public Long getEstimatedId() { return estimatedId; }
    public void setEstimatedId(Long estimatedId) { this.estimatedId = estimatedId; }

    public Integer getChainId() { return chainId; }
    public void setChainId(Integer chainId) { this.chainId = chainId; }

    public String getServiceDetails() { return serviceDetails; }
    public void setServiceDetails(String serviceDetails) { this.serviceDetails = serviceDetails; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public Double getCostPerQty() { return costPerQty; }
    public void setCostPerQty(Double costPerQty) { this.costPerQty = costPerQty; }

    public Double getAmountPayable() { return amountPayable; }
    public void setAmountPayable(Double amountPayable) { this.amountPayable = amountPayable; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public String getDeliveryDetails() { return deliveryDetails; }
    public void setDeliveryDetails(String deliveryDetails) { this.deliveryDetails = deliveryDetails; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public LocalDate getDateOfService() { return dateOfService; }
    public void setDateOfService(LocalDate dateOfService) { this.dateOfService = dateOfService; }
}
