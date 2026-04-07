package com.codeb.ims.controller;

import com.codeb.ims.entity.Brand;
import com.codeb.ims.entity.Chain;
import com.codeb.ims.payload.request.BrandRequest;
import com.codeb.ims.payload.response.BrandResponse;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.repository.BrandRepository;
import com.codeb.ims.repository.ChainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ChainRepository chainRepository;

    @GetMapping
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream().map(brand -> {
            BrandResponse response = new BrandResponse();
            response.setBrandId(brand.getBrandId());
            response.setBrandName(brand.getBrandName());
            response.setIsActive(brand.getIsActive());
            
            if (brand.getChain() != null) {
                response.setChainId(brand.getChain().getChainId());
                response.setCompanyName(brand.getChain().getCompanyName());
                if (brand.getChain().getGroup() != null) {
                    response.setGroupId(brand.getChain().getGroup().getGroupId());
                    response.setGroupName(brand.getChain().getGroup().getGroupName());
                }
            }
            return response;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createBrand(@RequestBody BrandRequest brandRequest) {
        if (brandRequest.getChainId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Must belong to a valid Company Chain!"));
        }

        Optional<Chain> chainOpt = chainRepository.findById(brandRequest.getChainId());
        if (!chainOpt.isPresent() || !chainOpt.get().getIsActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Company Chain not found or inactive!"));
        }

        Brand brand = new Brand();
        brand.setBrandName(brandRequest.getBrandName());
        brand.setChain(chainOpt.get());
        brand.setIsActive(true);

        brandRepository.save(brand);
        return ResponseEntity.ok(new MessageResponse("Brand created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest) {
        Optional<Brand> brandData = brandRepository.findById(id);

        if (brandData.isPresent()) {
            Brand _brand = brandData.get();

            if (brandRequest.getBrandName() != null) {
                _brand.setBrandName(brandRequest.getBrandName());
            }

            if (brandRequest.getIsActive() != null) {
                _brand.setIsActive(brandRequest.getIsActive());
            }
            
            if (brandRequest.getChainId() != null && (_brand.getChain() == null || !brandRequest.getChainId().equals(_brand.getChain().getChainId()))) {
                Optional<Chain> newChain = chainRepository.findById(brandRequest.getChainId());
                if(newChain.isPresent() && newChain.get().getIsActive()) {
                    _brand.setChain(newChain.get());
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Company Chain not found or inactive!"));
                }
            }

            brandRepository.save(_brand);
            return ResponseEntity.ok(new MessageResponse("Brand updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteBrand(@PathVariable Long id) {
        Optional<Brand> brandData = brandRepository.findById(id);

        if (brandData.isPresent()) {
            Brand _brand = brandData.get();
            _brand.setIsActive(false); // Soft Delete
            brandRepository.save(_brand);
            return ResponseEntity.ok(new MessageResponse("Brand deleted (deactivated) successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
