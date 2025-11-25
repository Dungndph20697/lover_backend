package com.codegym.service;

import com.codegym.dto.CcdvFindByCity;
import com.codegym.dto.CcdvFindByCity;
import com.codegym.dto.ServiceTypeDTO;
import com.codegym.dto.ServiceVipDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.ServiceType;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvFindByCityRepository;
import com.codegym.repository.ServiceTypeFindByCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CcdvFindByCityService {
    @Autowired
    private CcdvFindByCityRepository ccdvRepository;

    @Autowired
    private ServiceTypeFindByCityRepository serviceTypeRepository;

    private final Random random = new Random();

    //find all
    public List<CcdvFindByCity> getAllActiveCcdv(String city) {
        List<CcdvProfile> profiles;
        List<CcdvFindByCity> result = new ArrayList<>();
        int LIMIT = 12;

        //city ko đc null
        if (city != null && !city.isBlank()) {
            profiles = ccdvRepository.findTop12ByCityAndStatusOrderByJoinDateDesc(
                    city,
                    ProfileStatus.ACTIVE,
                    PageRequest.of(0, LIMIT)
            );
        } else {
            profiles = ccdvRepository.findTop12ByStatusOrderByJoinDateDesc(
                    ProfileStatus.ACTIVE,
                    PageRequest.of(0, LIMIT)
            );
        }

        for (CcdvProfile p : profiles) {

            Long userId = p.getUser().getId();
            List<CcdvServiceDetail> services = serviceTypeRepository.findByUser(userId);

            // Random 3
            Collections.shuffle(services, new Random(System.currentTimeMillis()));
            List<CcdvServiceDetail> picked = services.stream()
                    .limit(3)
                    .collect(Collectors.toList());

            // Convert DTO
            List<ServiceVipDTO> serviceDtos = picked.stream()
                    .filter(s -> s.getServiceType() != null)
                    .map(s -> new ServiceVipDTO(
                            s.getServiceType().getId(),
                            s.getServiceType().getName(),
                            s.getServiceType().getPricePerHour()
                    ))
                    .collect(Collectors.toList());

            // Giá thấp nhất
            Optional<BigDecimal> minPrice = services.stream()
                    .map(CcdvServiceDetail::getServiceType)
                    .filter(Objects::nonNull)
                    .map(ServiceType::getPricePerHour)
                    .filter(Objects::nonNull)
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .min(BigDecimal::compareTo);

            // Tổng 3 giá
            BigDecimal totalPrice = picked.stream()
                    .map(s -> s.getServiceType().getPricePerHour())
                    .filter(Objects::nonNull)
                    .filter(price -> price.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Build DTO
            CcdvFindByCity dto = new CcdvFindByCity();
            dto.setProfileId(p.getId());
            dto.setUserId(userId);
            dto.setName(p.getFullName());
            dto.setAvatar(p.getAvatar());
            dto.setDescription(p.getDescription());
            dto.setCity(p.getCity());
            dto.setServices(serviceDtos);
            dto.setStartingPricePerHour(minPrice.orElse(null));
            dto.setTotalPrice(totalPrice);
            dto.setHireCount(p.getHireCount());

            result.add(dto);
        }

        return result;
    }
}
