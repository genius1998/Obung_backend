package com.lion.CalPick.service;

import com.lion.CalPick.domain.Bung;
import com.lion.CalPick.dto.BungCreateRequest;
import com.lion.CalPick.dto.BungResponse;
import com.lion.CalPick.repository.BungRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BungService {

    private final BungRepository bungRepository;

    public BungService(BungRepository bungRepository) {
        this.bungRepository = bungRepository;
    }

    @Transactional
    public BungResponse createBung(BungCreateRequest request) {
        Bung bung = new Bung();
        bung.setTitle(request.getTitle());
        bung.setLocation(request.getLocation());
        bung.setStore(request.getStore());
        bung.setTotalParticipants(request.getTotalParticipants());
        bung.setOrganizerUserId(request.getOrganizerUserId());

        Bung savedBung = bungRepository.save(bung);
        return new BungResponse(
                savedBung.getId(),
                savedBung.getTitle(),
                savedBung.getLocation(),
                savedBung.getStore(),
                savedBung.getTotalParticipants(),
                savedBung.getOrganizerUserId()
        );
    }

    @Transactional(readOnly = true)
    public List<BungResponse> getAllBungs() {
        return bungRepository.findAll().stream()
                .map(bung -> new BungResponse(
                        bung.getId(),
                        bung.getTitle(),
                        bung.getLocation(),
                        bung.getStore(),
                        bung.getTotalParticipants(),
                        bung.getOrganizerUserId()
                ))
                .collect(Collectors.toList());
    }
}
