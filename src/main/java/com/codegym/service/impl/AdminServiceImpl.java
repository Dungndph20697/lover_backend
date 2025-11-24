package com.codegym.service.impl;

import com.codegym.dto.CcdvDetailAdminDTO;
import com.codegym.dto.UserListDTO;
import com.codegym.repository.CcdvProfileRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CcdvProfileRepository ccdvProfileRepository;


    @Override
    public List<UserListDTO> getAllUsersExceptAdmin() {
        return userRepository.getAllUsersExceptAdmin();
    }

    @Override
    public List<UserListDTO> filterUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public CcdvDetailAdminDTO getCcdvDetail(Long userId) {
        return ccdvProfileRepository.getCcdvDetail(userId);
    }

    @Override
    public void lockUser(Long userId) {
        userRepository.updateStatus(userId, "BANNED");
    }

    @Override
    public void unlockUser(Long userId) {
        userRepository.updateStatus(userId, "ACTIVE");
    }
}
