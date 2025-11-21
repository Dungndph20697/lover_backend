package com.codegym.service.vip_user;

import com.codegym.dto.CcdvSuggestionDTO;
import com.codegym.dto.ServiceVipDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.ServiceType;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.CcdvServiceDetailRepository;
import com.codegym.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CcdvSuggestionServiceImpl implements CcdvSuggestionService {
    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;

    @Autowired
    private CcdvServiceDetailRepository ccdvServiceTypeRepository;


    @Override
    @Transactional(readOnly = true)
    public List<CcdvSuggestionDTO> getVipSuggestions(int limit) {
        List<CcdvProfile> vipProfiles = ccdvProfileRepository.findVipProfilesByStatusOrderByVipStartTimeDesc(
                ProfileStatus.ACTIVE, PageRequest.of(0, limit)
        );

        List<CcdvSuggestionDTO> result = new ArrayList<>();

        for (CcdvProfile p : vipProfiles) {
            Long userId = p.getUser().getId();
            List<CcdvServiceDetail> services = ccdvServiceTypeRepository.findByCcdvId(userId);

            // shuffle to pick random 3
            Collections.shuffle(services, new Random(System.currentTimeMillis()));
            List<CcdvServiceDetail> picked = services.stream().limit(3).collect(Collectors.toList());

            List<ServiceVipDTO> serviceDtos = picked.stream()
                    .filter(s -> s.getServiceType() != null && s.getServiceType().getPricePerHour() != null)
                    .map(s -> new ServiceVipDTO(
                            s.getServiceType().getId(),
                            s.getServiceType().getName(),
                            s.getServiceType().getPricePerHour())
                    )
                    .collect(Collectors.toList());

            // compute starting price (min of pricePerHour), null-safe
            Optional<BigDecimal> minPrice = services.stream()
                    .map(CcdvServiceDetail::getServiceType)
                    .filter(Objects::nonNull)
                    .map(st -> st.getPricePerHour())
                    .filter(Objects::nonNull)
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .min(BigDecimal::compareTo);

            BigDecimal totalPrice = services.stream()
                    .map(CcdvServiceDetail::getServiceType)
                    .filter(Objects::nonNull)
                    .map(ServiceType::getPricePerHour)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            CcdvSuggestionDTO dto = new CcdvSuggestionDTO();
            dto.setProfileId(p.getId());
            dto.setUserId(userId);
            dto.setName(p.getFullName());
            dto.setAvatar(p.getAvatar());
            dto.setDescription(p.getDescription());
            dto.setServices(serviceDtos);
            dto.setStartingPricePerHour(minPrice.orElse(null));
            dto.setTotalPrice(totalPrice);

            result.add(dto);
        }

        return result;
    }
}
