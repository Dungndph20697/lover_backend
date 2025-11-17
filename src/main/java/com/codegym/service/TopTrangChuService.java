package com.codegym.service;

import com.codegym.dto.CcdvProfileHomeDTO;
import com.codegym.dto.ServiceTypeDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.ServiceType;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopTrangChuService {
    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    // Lấy 4 nam, 8 nữ top thuê nhiều nhất
    public List<CcdvProfile> getTopCcdvByGender(String gender, int limit) {
        List<CcdvProfile> list = ccdvProfileRepository.findTopByGenderOrderByHireCountDesc(gender);
        return list.size() > limit ? list.subList(0, limit) : list;
    }

    // Lấy random 3 dịch vụ
    public List<ServiceTypeDTO> getRandomServices(int count) {
        List<ServiceType> allServices = serviceTypeRepository.findAll();
        Collections.shuffle(allServices);
        return allServices.stream()
                .limit(count)
                .map(s -> new ServiceTypeDTO(s.getName(), s.getPricePerHour()))
                .collect(Collectors.toList());
    }

    // Chuyển sang DTO
    public CcdvProfileHomeDTO toDTO(CcdvProfile ccdv) {
        System.out.println(">>> PROFILE: " + ccdv.getId() + " | " + ccdv.getFullName() + " | hireCount=" + ccdv.getHireCount());
        return new CcdvProfileHomeDTO(
                ccdv.getId(),
                ccdv.getFullName(),
                ccdv.getAvatar(),
                ccdv.getDescription(),
                ccdv.getGender(),
                getRandomServices(3),
                ccdv.getHireCount()
        );
    }

    // Lấy top 12 CCDV (4 nam, 8 nữ) dưới dạng DTO
    public List<CcdvProfileHomeDTO> getTopCcdvDTO() {
        List<CcdvProfileHomeDTO> result = new ArrayList<>();
        getTopCcdvByGender("Nam", 4).forEach(c -> result.add(toDTO(c)));
        getTopCcdvByGender("Nữ", 8).forEach(c -> result.add(toDTO(c)));
        return result;
    }
}