package com.example.mis.repository;

import com.example.mis.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    List<Group> findByIsActiveTrue();

    boolean existsByGroupName(String groupName);
}
