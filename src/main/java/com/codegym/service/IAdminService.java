package com.codegym.service;

import com.codegym.dto.CcdvDetailAdminDTO;
import com.codegym.dto.UserListDTO;

import java.util.List;

public interface IAdminService {
    List<UserListDTO> getAllUsersExceptAdmin();

    List<UserListDTO> filterUsersByRole(String role);

    CcdvDetailAdminDTO getCcdvDetail(Long userId);

    void lockUser(Long userId);

    void unlockUser(Long userId);
}
