package com.example.mis.controller;

import com.example.mis.entity.Group;
import com.example.mis.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class GroupController {

    @Autowired
    private GroupRepository repo;

    // LOGIN (DUMMY)
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> data) {
        if (data.get("email").equals("admin@gmail.com") &&
            data.get("password").equals("1234")) {
            return "Login Success";
        }
        return "Invalid Credentials";
    }

    // ADD GROUP
    @PostMapping("/group")
    public String addGroup(@RequestBody Group g) {

        if (g.getGroupName() == null || g.getGroupName().isEmpty()) {
            return "Group name required";
        }

        if (repo.existsByGroupName(g.getGroupName())) {
            return "Group already exists";
        }

        repo.save(g);
        return "Group added successfully";
    }

    // VIEW GROUP
    @GetMapping("/group")
    public List<Group> getGroups() {
        return repo.findByIsActiveTrue();
    }
}
