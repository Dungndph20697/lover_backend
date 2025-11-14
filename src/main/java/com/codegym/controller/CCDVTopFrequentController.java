package com.codegym.controller;

import com.codegym.model.CcdvProfile;
import com.codegym.service.ITopFrequent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ccdv")
public class CCDVTopFrequentController {
    @Autowired
    private ITopFrequent topFrequentService;

    @GetMapping("/top-frequent/{ccdvId}")
    public ResponseEntity<?> getTopFrequent(@PathVariable Long ccdvId) {
        return ResponseEntity.ok(topFrequentService.getTopFrequentCustomers(ccdvId));
    }

    @GetMapping("/top-recent/{ccdvId}")
    public ResponseEntity<?> getTopRecent(@PathVariable Long ccdvId) {
        return ResponseEntity.ok(topFrequentService.getTopRecentCustomers(ccdvId));
    }

    @GetMapping("/getfullinfouser/{userId}")
    public ResponseEntity<?> getFullinfoUser(@PathVariable Long userId) {
        CcdvProfile profile = topFrequentService.findFullInfoUser(userId);
        return ResponseEntity.ok(profile);
    }

}
