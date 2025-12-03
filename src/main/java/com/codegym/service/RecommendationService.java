package com.codegym.service;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.dto.CcdvSuggestGenderDTO;
import com.codegym.dto.ServiceVipDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.ServiceType;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvServiceDetailRepository;
import com.codegym.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository repository;

    @Autowired
    private CcdvServiceDetailRepository serviceDetailRepo;

    public List<CcdvSuggestGenderDTO> suggestProviders(String gender) {
        int LIMIT = 12;

        List<CcdvProfile> profiles;

        if (gender != null && !gender.isBlank()) {
            profiles = repository.findByGenderAndStatusOrderByJoinDateDesc(
                    gender, ProfileStatus.ACTIVE, PageRequest.of(0, LIMIT)
            );
        } else {
            profiles = repository.findByStatusOrderByJoinDateDesc(
                    ProfileStatus.ACTIVE, PageRequest.of(0, LIMIT)
            );
        }

        List<CcdvSuggestGenderDTO> result = new ArrayList<>();

        for (CcdvProfile p : profiles) {

            Long userId = p.getUser().getId();

            List<CcdvServiceDetail> services = serviceDetailRepo.findByUser_Id(userId);

            // random 3 services
            Collections.shuffle(services);
            List<CcdvServiceDetail> picked = services.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            // convert DTO
            List<ServiceVipDTO> serviceDtos = picked.stream()
                    .filter(s -> s.getServiceType() != null && s.getServiceType().getPricePerHour() != null)
                    .map(s -> new ServiceVipDTO(
                            s.getServiceType().getId(),
                            s.getServiceType().getName(),
                            s.getServiceType().getPricePerHour()
                    ))
                    .collect(Collectors.toList());

            // min price
            Optional<BigDecimal> minPrice = services.stream()
                    .map(CcdvServiceDetail::getServiceType)
                    .filter(Objects::nonNull)
                    .map(ServiceType::getPricePerHour)
                    .filter(Objects::nonNull)
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .min(BigDecimal::compareTo);

            // total price (sum 3)
            BigDecimal totalPrice = picked.stream()
                    .map(CcdvServiceDetail::getServiceType)
                    .filter(Objects::nonNull)
                    .map(ServiceType::getPricePerHour)
                    .filter(Objects::nonNull)
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);


            CcdvSuggestGenderDTO dto = new CcdvSuggestGenderDTO();
            dto.setProfileId(p.getId());
            dto.setUserId(userId);
            dto.setName(p.getFullName());
            dto.setAvatar(p.getAvatar());
            dto.setDescription(p.getDescription());
            dto.setGender(p.getGender());
            dto.setServices(serviceDtos);
            dto.setStartingPricePerHour(minPrice.orElse(null));
            dto.setTotalPrice(totalPrice);
            dto.setHireCount(p.getHireCount());

            result.add(dto);
        }

        return result;
    }

    public List<CcdvProfile> filterProfiles(CcdvFilterRequest request) {

        String name = request.getName();
        Integer minAge = request.getMinAge();
        Integer maxAge = request.getMaxAge();
        String gender = request.getGender();
        String address = request.getAddress();

        return repository.filterProfiles(name, minAge, maxAge, gender, address);
    }
}
