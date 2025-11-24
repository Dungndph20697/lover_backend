package com.codegym.service;

import com.codegym.dto.CcdvFilterRequest;
import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import com.codegym.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository repository;

    public List<CcdvProfile> suggestProviders(String gender) {

        int LIMIT = 12;

        List<CcdvProfile> profiles;

        if (gender != null && !gender.isBlank()) {
            profiles = repository.findByGenderAndStatusOrderByJoinDateDesc(
                    gender,
                    ProfileStatus.ACTIVE,
                    PageRequest.of(0, LIMIT)
            );
        } else {
            profiles = repository.findByStatusOrderByJoinDateDesc(
                    ProfileStatus.ACTIVE,
                    PageRequest.of(0, LIMIT)
            );
        }

        // Gắn thêm 3 service random
        profiles.forEach(p -> {
            List<String> services = repository.findRandomServicesByUserId(p.getUser().getId());
            p.setHobbies(String.join(", ", services));  // bạn có thể tạo DTO riêng thay vì nhét vào hobbies
        });

        return profiles;
    }

    public List<CcdvProfile> filterProfiles(CcdvFilterRequest request) {

        String name = request.getName();
        Integer minAge = request.getMinAge();
        Integer maxAge = request.getMaxAge();
        String gender = request.getGender();
        String address = request.getAddress();

        return repository.filterProfiles(name, minAge, maxAge, gender, address);
    }

    public List<CcdvProfile> getAllActiveProviders() {
        List<CcdvProfile> profiles = repository.findByStatusOrderByJoinDateDesc(ProfileStatus.ACTIVE, Pageable.unpaged());

        // Gắn thêm 3 service random cho mỗi profile
        profiles.forEach(p -> {
            List<String> services = repository.findRandomServicesByUserId(p.getUser().getId());
            p.setHobbies(String.join(", ", services));
        });

        return profiles;
    }
}
