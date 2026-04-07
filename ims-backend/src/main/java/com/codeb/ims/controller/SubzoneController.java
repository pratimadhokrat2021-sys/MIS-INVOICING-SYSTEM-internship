package com.codeb.ims.controller;

import com.codeb.ims.entity.Brand;
import com.codeb.ims.entity.Subzone;
import com.codeb.ims.payload.request.SubzoneRequest;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.payload.response.SubzoneResponse;
import com.codeb.ims.repository.BrandRepository;
import com.codeb.ims.repository.SubzoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/subzones")
public class SubzoneController {

    @Autowired
    SubzoneRepository subzoneRepository;

    @Autowired
    BrandRepository brandRepository;

    @GetMapping
    public List<SubzoneResponse> getAllSubzones() {
        return subzoneRepository.findAll().stream().map(subzone -> {
            SubzoneResponse response = new SubzoneResponse();
            response.setSubzoneId(subzone.getSubzoneId());
            response.setSubzoneName(subzone.getSubzoneName());
            response.setIsActive(subzone.getIsActive());
            
            if (subzone.getBrand() != null) {
                response.setBrandId(subzone.getBrand().getBrandId());
                response.setBrandName(subzone.getBrand().getBrandName());
                if (subzone.getBrand().getChain() != null) {
                    response.setChainId(subzone.getBrand().getChain().getChainId());
                    response.setCompanyName(subzone.getBrand().getChain().getCompanyName());
                    if (subzone.getBrand().getChain().getGroup() != null) {
                        response.setGroupId(subzone.getBrand().getChain().getGroup().getGroupId());
                        response.setGroupName(subzone.getBrand().getChain().getGroup().getGroupName());
                    }
                }
            }
            return response;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createSubzone(@RequestBody SubzoneRequest subzoneRequest) {
        if (subzoneRequest.getBrandId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Must belong to a valid Brand!"));
        }

        Optional<Brand> brandOpt = brandRepository.findById(subzoneRequest.getBrandId());
        if (!brandOpt.isPresent() || !brandOpt.get().getIsActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Brand not found or inactive!"));
        }

        Subzone subzone = new Subzone();
        subzone.setSubzoneName(subzoneRequest.getSubzoneName());
        subzone.setBrand(brandOpt.get());
        subzone.setIsActive(true);

        subzoneRepository.save(subzone);
        return ResponseEntity.ok(new MessageResponse("Zone created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubzone(@PathVariable Long id, @RequestBody SubzoneRequest subzoneRequest) {
        Optional<Subzone> subzoneData = subzoneRepository.findById(id);

        if (subzoneData.isPresent()) {
            Subzone _subzone = subzoneData.get();

            if (subzoneRequest.getSubzoneName() != null) {
                _subzone.setSubzoneName(subzoneRequest.getSubzoneName());
            }

            if (subzoneRequest.getIsActive() != null) {
                _subzone.setIsActive(subzoneRequest.getIsActive());
            }
            
            if (subzoneRequest.getBrandId() != null && (_subzone.getBrand() == null || !subzoneRequest.getBrandId().equals(_subzone.getBrand().getBrandId()))) {
                Optional<Brand> newBrand = brandRepository.findById(subzoneRequest.getBrandId());
                if(newBrand.isPresent() && newBrand.get().getIsActive()) {
                    _subzone.setBrand(newBrand.get());
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Brand not found or inactive!"));
                }
            }

            subzoneRepository.save(_subzone);
            return ResponseEntity.ok(new MessageResponse("Zone updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteSubzone(@PathVariable Long id) {
        Optional<Subzone> subzoneData = subzoneRepository.findById(id);

        if (subzoneData.isPresent()) {
            Subzone _subzone = subzoneData.get();
            _subzone.setIsActive(false); // Soft Delete
            subzoneRepository.save(_subzone);
            return ResponseEntity.ok(new MessageResponse("Zone deleted (deactivated) successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
