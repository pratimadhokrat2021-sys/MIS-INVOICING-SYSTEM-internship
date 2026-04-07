package com.codeb.ims.controller;

import com.codeb.ims.entity.Group;
import com.codeb.ims.payload.response.MessageResponse;
import com.codeb.ims.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        if (groupRepository.existsByGroupName(group.getGroupName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Group name already exists!"));
        }
        if (group.getGroupName() == null || group.getGroupName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Group name cannot be empty!"));
        }
        group.setIsActive(true);
        groupRepository.save(group);
        return ResponseEntity.ok(new MessageResponse("Group created successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable Integer id, @RequestBody Group groupDetails) {
        Optional<Group> groupData = groupRepository.findById(id);

        if (groupData.isPresent()) {
            Group _group = groupData.get();
            
            // Check uniqueness if name changed
            if (groupDetails.getGroupName() != null && !_group.getGroupName().equals(groupDetails.getGroupName())) {
                if (groupRepository.existsByGroupName(groupDetails.getGroupName())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Group name already exists!"));
                }
                if (groupDetails.getGroupName().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Error: Group name cannot be empty!"));
                }
                _group.setGroupName(groupDetails.getGroupName());
            }
            
            // Toggle active status capability
            if (groupDetails.getIsActive() != null) {
                _group.setIsActive(groupDetails.getIsActive());
            }
            
            groupRepository.save(_group);
            return ResponseEntity.ok(new MessageResponse("Group updated successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteGroup(@PathVariable Integer id) {
        Optional<Group> groupData = groupRepository.findById(id);

        if (groupData.isPresent()) {
            Group _group = groupData.get();
            _group.setIsActive(false); // Soft Delete
            groupRepository.save(_group);
            return ResponseEntity.ok(new MessageResponse("Group deleted (deactivated) successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
