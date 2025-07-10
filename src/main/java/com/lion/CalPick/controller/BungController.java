package com.lion.CalPick.controller;

import com.lion.CalPick.dto.BungCreateRequest;
import com.lion.CalPick.dto.BungResponse;
import com.lion.CalPick.service.BungService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/bungs")
public class BungController {

    private final BungService bungService;
    private static final Logger logger = LoggerFactory.getLogger(BungController.class);

    public BungController(BungService bungService) {
        this.bungService = bungService;
    }

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<BungResponse> createBung(@RequestBody BungCreateRequest request) {
        BungResponse newBung = bungService.createBung(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBung);
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<BungResponse>> getAllBungs() {
        List<BungResponse> bungs = bungService.getAllBungs();
        logger.info("📦 전체 벙 목록 반환 [{}건]:", bungs.size());
        for (BungResponse bung : bungs) {
            logger.info("➡️ {}", bung);
        }
        return ResponseEntity.ok(bungs);
    }
}
