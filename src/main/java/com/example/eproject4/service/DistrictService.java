package com.example.eproject4.service;

import com.example.eproject4.dto.DistrictDTO;
import com.example.eproject4.model.District;
import com.example.eproject4.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    public List<DistrictDTO> getDistrictsByProvince(int provinceId) {
        List<District> districts = districtRepository.findByProvinceId(provinceId);
        return districts.stream()
                .map(district -> new DistrictDTO(district.getId(), district.getName()))
                .collect(Collectors.toList());
    }
}
