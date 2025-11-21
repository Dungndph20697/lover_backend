package com.codegym.service.vip_user;

import com.codegym.dto.CcdvSuggestionDTO;
import com.codegym.dto.ServiceVipDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.ServiceType;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CcdvSuggestionServiceImpl implements CcdvSuggestionService {
    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;


    @Override
    @Transactional(readOnly = true)
    public List<CcdvSuggestionDTO> getVipSuggestions(int limit) {
        List<CcdvProfile> vipProfiles = ccdvProfileRepository.findVipProfilesByStatusOrderByVipStartTimeDesc(
                ProfileStatus.ACTIVE, PageRequest.of(0, limit)
        );

        List<CcdvSuggestionDTO> result = new ArrayList<>();

        for (CcdvProfile p : vipProfiles) {
            Long userId = p.getUser().getId();
            List<ServiceType> services = serviceTypeRepository.findByCcdvId(userId);
            // shuffle to pick random 3
            Collections.shuffle(services, new Random(System.currentTimeMillis()));
            List<ServiceType> picked = services.stream().limit(3).collect(Collectors.toList());

            List<ServiceVipDTO> serviceDtos = picked.stream()
                    .map(s -> new ServiceVipDTO(s.getId(), s.getName(), s.getPricePerHour()))
                    .collect(Collectors.toList());

            // compute starting price (min of pricePerHour), null-safe
            Optional<Double> minPrice = services.stream()
                    .map(ServiceType::getPricePerHour)
                    .filter(Objects::nonNull)
                    .map(BigDecimal -> BigDecimal.doubleValue())
                    .min(Double::compareTo);

            CcdvSuggestionDTO dto = new CcdvSuggestionDTO();
            dto.setProfileId(p.getId());
            dto.setUserId(userId);
            dto.setName(p.getFullName());
            dto.setAvatar(p.getAvatar());
            dto.setDescription(p.getDescription());
            dto.setServices(serviceDtos);
            dto.setStartingPricePerHour(minPrice.map(d -> new java.math.BigDecimal(d)).orElse(null));

            result.add(dto);
        }

        return result;
    }
}
