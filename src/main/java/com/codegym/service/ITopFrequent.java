package com.codegym.service;

import com.codegym.dto.TopFrequentCustomerDTO;
import com.codegym.dto.TopRecentCustomerDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.User;

import java.util.List;

public interface ITopFrequent {

    List<TopFrequentCustomerDTO> getTopFrequentCustomers(Long ccdvId);

    List<TopRecentCustomerDTO> getTopRecentCustomers(Long ccdvId);

    CcdvProfile findFullInfoUser(Long userId);
}
