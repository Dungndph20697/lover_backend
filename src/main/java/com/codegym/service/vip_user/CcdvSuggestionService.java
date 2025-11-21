package com.codegym.service.vip_user;

import com.codegym.dto.CcdvSuggestionDTO;

import java.util.List;

public interface CcdvSuggestionService {
    List<CcdvSuggestionDTO> getVipSuggestions(int limit);
}
