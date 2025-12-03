package com.codegym.service;

import com.codegym.model.User;
import com.codegym.model.Wallet;
import com.codegym.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWalletByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseGet(() -> walletRepository.save(new Wallet(null, user, 0.0)));
    }

    public Wallet topUp(User user, Double amount) {
        Wallet wallet = getWalletByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);
        return walletRepository.save(wallet);
    }
}
