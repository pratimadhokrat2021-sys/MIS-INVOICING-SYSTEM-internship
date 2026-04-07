package com.codeb.ims.controller;

import com.codeb.ims.entity.Chain;
import com.codeb.ims.entity.Estimate;
import com.codeb.ims.payload.request.EstimateRequest;
import com.codeb.ims.payload.response.EstimateResponse;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.repository.ChainRepository;
import com.codeb.ims.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

    @Autowired
    EstimateRepository estimateRepository;

    @Autowired
    ChainRepository chainRepository;

    @GetMapping
    public List<EstimateResponse> getAllEstimates() {
        return estimateRepository.findAll().stream().map(estimate -> {
            EstimateResponse response = new EstimateResponse();
            response.setEstimatedId(estimate.getEstimatedId());
            response.setChainId(estimate.getChain().getChainId());
            response.setCompanyName(estimate.getChain().getCompanyName());
            response.setGroupName(estimate.getGroupName());
            response.setBrandName(estimate.getBrandName());
            response.setZoneName(estimate.getZoneName());
            response.setServiceDetails(estimate.getServiceDetails());
            response.setQty(estimate.getQty());
            response.setCostPerUnit(estimate.getCostPerUnit());
            response.setTotalCost(estimate.getTotalCost());
            response.setDeliveryDate(estimate.getDeliveryDate());
            response.setDeliveryDetails(estimate.getDeliveryDetails());
            response.setIsActive(estimate.getIsActive());
            response.setCreatedAt(estimate.getCreatedAt());
            response.setUpdatedAt(estimate.getUpdatedAt());
            return response;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createEstimate(@RequestBody EstimateRequest request) {
        if (request.getChainId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Chain ID is required!"));
        }

        Optional<Chain> chainOpt = chainRepository.findById(request.getChainId());
        if (!chainOpt.isPresent() || !chainOpt.get().getIsActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Chain (Company) not found or inactive!"));
        }

        Estimate estimate = new Estimate();
        estimate.setChain(chainOpt.get());
        estimate.setGroupName(request.getGroupName());
        estimate.setBrandName(request.getBrandName());
        estimate.setZoneName(request.getZoneName());
        estimate.setServiceDetails(request.getServiceDetails());
        estimate.setQty(request.getQty());
        estimate.setCostPerUnit(request.getCostPerUnit());
        estimate.setTotalCost(request.getTotalCost());
        estimate.setDeliveryDate(request.getDeliveryDate());
        estimate.setDeliveryDetails(request.getDeliveryDetails());
        estimate.setIsActive(true);

        estimateRepository.save(estimate);
        return ResponseEntity.ok(new MessageResponse("Estimate created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEstimate(@PathVariable Long id, @RequestBody EstimateRequest request) {
        Optional<Estimate> estimateData = estimateRepository.findById(id);

        if (estimateData.isPresent()) {
            Estimate estimate = estimateData.get();

            if (request.getChainId() != null && (!request.getChainId().equals(estimate.getChain().getChainId()))) {
                Optional<Chain> chainOpt = chainRepository.findById(request.getChainId());
                if (chainOpt.isPresent() && chainOpt.get().getIsActive()) {
                    estimate.setChain(chainOpt.get());
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Chain (Company) not found or inactive!"));
                }
            }

            if (request.getGroupName() != null) estimate.setGroupName(request.getGroupName());
            if (request.getBrandName() != null) estimate.setBrandName(request.getBrandName());
            if (request.getZoneName() != null) estimate.setZoneName(request.getZoneName());
            if (request.getServiceDetails() != null) estimate.setServiceDetails(request.getServiceDetails());
            if (request.getQty() != null) estimate.setQty(request.getQty());
            if (request.getCostPerUnit() != null) estimate.setCostPerUnit(request.getCostPerUnit());
            if (request.getTotalCost() != null) estimate.setTotalCost(request.getTotalCost());
            if (request.getDeliveryDate() != null) estimate.setDeliveryDate(request.getDeliveryDate());
            if (request.getDeliveryDetails() != null) estimate.setDeliveryDetails(request.getDeliveryDetails());
            if (request.getIsActive() != null) estimate.setIsActive(request.getIsActive());

            estimateRepository.save(estimate);
            return ResponseEntity.ok(new MessageResponse("Estimate updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstimate(@PathVariable Long id) {
        Optional<Estimate> estimateData = estimateRepository.findById(id);

        if (estimateData.isPresent()) {
            Estimate estimate = estimateData.get();
            estimate.setIsActive(false); // Soft delete
            estimateRepository.save(estimate);
            return ResponseEntity.ok(new MessageResponse("Estimate deleted (soft delete) successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
