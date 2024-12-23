package com.example.eproject4.repository;

import com.example.eproject4.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {
    List<Ward> findByDistrictId(int districtId);
}
