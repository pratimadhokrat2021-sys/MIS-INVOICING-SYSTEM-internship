package com.codeb.ims.controller;

import com.codeb.ims.entity.Chain;
import com.codeb.ims.entity.Group;
import com.codeb.ims.payload.request.ChainRequest;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.repository.ChainRepository;
import com.codeb.ims.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chains")
public class ChainController {

    @Autowired
    ChainRepository chainRepository;

    @Autowired
    GroupRepository groupRepository;

    @GetMapping
    public List<Chain> getAllChains() {
        return chainRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createChain(@RequestBody ChainRequest chainRequest) {
        if (chainRepository.existsByGstnNo(chainRequest.getGstnNo())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: GSTN Number already exists!"));
        }

        if (chainRequest.getGroupId() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Must belong to a valid Group!"));
        }

        Optional<Group> groupOpt = groupRepository.findById(chainRequest.getGroupId());
        if (!groupOpt.isPresent() || !groupOpt.get().getIsActive()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Selected Group not found or inactive!"));
        }

        Chain chain = new Chain();
        chain.setCompanyName(chainRequest.getCompanyName());
        chain.setGstnNo(chainRequest.getGstnNo().toUpperCase());
        chain.setGroup(groupOpt.get());
        chain.setIsActive(true);

        chainRepository.save(chain);
        return ResponseEntity.ok(new MessageResponse("Company Chain created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChain(@PathVariable Integer id, @RequestBody ChainRequest chainRequest) {
        Optional<Chain> chainData = chainRepository.findById(id);

        if (chainData.isPresent()) {
            Chain _chain = chainData.get();

            // Unique GSTN Check
            if (chainRequest.getGstnNo() != null && !_chain.getGstnNo().equalsIgnoreCase(chainRequest.getGstnNo())) {
                if(chainRepository.existsByGstnNo(chainRequest.getGstnNo())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: GSTN Number already exists!"));
                }
                _chain.setGstnNo(chainRequest.getGstnNo().toUpperCase());
            }

            if (chainRequest.getCompanyName() != null) {
                _chain.setCompanyName(chainRequest.getCompanyName());
            }

            if (chainRequest.getIsActive() != null) {
                _chain.setIsActive(chainRequest.getIsActive());
            }
            
            // Allow moving to a different group if needed
            if (chainRequest.getGroupId() != null && !chainRequest.getGroupId().equals(_chain.getGroup().getGroupId())) {
                Optional<Group> newGroup = groupRepository.findById(chainRequest.getGroupId());
                if(newGroup.isPresent()) {
                    _chain.setGroup(newGroup.get());
                }
            }

            chainRepository.save(_chain);
            return ResponseEntity.ok(new MessageResponse("Chain updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteChain(@PathVariable Integer id) {
        Optional<Chain> chainData = chainRepository.findById(id);

        if (chainData.isPresent()) {
            Chain _chain = chainData.get();
            _chain.setIsActive(false); // Soft Delete
            chainRepository.save(_chain);
            return ResponseEntity.ok(new MessageResponse("Chain deleted (deactivated) successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
