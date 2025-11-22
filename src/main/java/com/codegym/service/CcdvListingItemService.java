package com.codegym.service;

import com.codegym.dto.CcdvListItemServiceDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.CcdvProfileListItemServiceRepository;
import com.codegym.repository.CcdvServiceListItemDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CcdvListingItemService {
    @Autowired
    private CcdvProfileListItemServiceRepository ccdvRepository;

    @Autowired
    private CcdvServiceListItemDetailRepository serviceRepository;

    private final Random random = new Random();

    @Transactional(readOnly = true)
    public Page<CcdvListItemServiceDTO> listCcdvIntimateGesture(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy tất cả profile active
        Page<CcdvProfile> profilesPage = ccdvRepository.findByStatus(ProfileStatus.ACTIVE, pageable);
        List<Long> userIds = profilesPage.getContent().stream()
                .map(p -> p.getUser().getId())
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Lấy tất cả dịch vụ cử chỉ thân mật
        List<CcdvServiceDetail> intimateServices = serviceRepository.findAllByUserIds(userIds);

        // map userId -> list dịch vụ thân mật
        Map<Long, List<CcdvServiceDetail>> servicesByUser = intimateServices.stream()
                .collect(Collectors.groupingBy(sd -> sd.getUser().getId()));

        // chỉ lấy profile có ít nhất 1 dịch vụ thân mật
        List<CcdvProfile> filteredProfiles = profilesPage.getContent().stream()
                .filter(p -> servicesByUser.containsKey(p.getUser().getId()))
                .collect(Collectors.toList());

        // map sang DTO
        List<CcdvListItemServiceDTO> dtos = filteredProfiles.stream().map(p -> {
            Long uid = p.getUser().getId();
            List<CcdvServiceDetail> sList = servicesByUser.getOrDefault(uid, Collections.emptyList());

            // chọn 3 dịch vụ random
            List<CcdvServiceDetail> sampled = sampleRandom(sList, 3);

            List<CcdvListItemServiceDTO.ServicePreview> previews = sampled.stream().map(sd -> {
                CcdvListItemServiceDTO.ServicePreview sp = new CcdvListItemServiceDTO.ServicePreview();
                sp.setServiceId(sd.getId());
                sp.setServiceName(sd.getServiceType().getName());
                sp.setPricePerHour(sd.getTotalPrice());
                return sp;
            }).collect(Collectors.toList());

            // min price thân mật
            BigDecimal minPrice = sList.stream()
                    .map(CcdvServiceDetail::getTotalPrice)
                    .filter(Objects::nonNull)
                    .min(BigDecimal::compareTo)
                    .orElse(null);

            CcdvListItemServiceDTO dto = new CcdvListItemServiceDTO();
            dto.setId(p.getId());
            dto.setFullName(p.getFullName());
            dto.setAvatar(p.getAvatar());
            dto.setDescription(p.getDescription());
            dto.setVip(p.getVip());
            dto.setJoinDate(p.getJoinDate());
            dto.setMinPricePerHour(minPrice);
            dto.setServices(previews);
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    // helper: sample up to n random elements (without replacement)
    private <T> List<T> sampleRandom(List<T> list, int n) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        if (list.size() <= n) return new ArrayList<>(list);
        // reservoir-like but simple shuffle for small lists
        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy, random);
        return copy.subList(0, n);
    }
}
