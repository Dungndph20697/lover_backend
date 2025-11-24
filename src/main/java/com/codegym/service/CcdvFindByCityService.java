package com.codegym.service;

import com.codegym.dto.CcdvFindByCity;
import com.codegym.dto.ServiceTypeDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.ServiceType;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvFindByCityRepository;
import com.codegym.repository.ServiceTypeFindByCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CcdvFindByCityService {
    @Autowired
    private CcdvFindByCityRepository ccdvRepository;

    @Autowired
    private ServiceTypeFindByCityRepository serviceTypeRepository;

    private final Random random = new Random();

    public List<CcdvFindByCity> getAllActiveCcdv() {
        List<CcdvProfile> profiles = ccdvRepository.findByStatus(ProfileStatus.ACTIVE);

        return profiles.stream().map(profile -> {
            List<ServiceType> services = serviceTypeRepository.findByCcdv(profile.getUser());
            Collections.shuffle(services, random);
            List<ServiceTypeDTO> serviceDTOs = services.stream()
                    .limit(3)
                    .map(s -> new ServiceTypeDTO(s.getName(), s.getPricePerHour()))
                    .collect(Collectors.toList());

            return new CcdvFindByCity(
                    profile.getId(),
                    profile.getFullName(),
                    profile.getAvatar(),
                    profile.getDescription(),
                    profile.getCity(),
                    profile.getHireCount()
            );
        }).collect(Collectors.toList());
    }
}
