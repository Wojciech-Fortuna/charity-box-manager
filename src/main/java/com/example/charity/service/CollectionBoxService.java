package com.example.charity.service;

import com.example.charity.model.CollectionBox;
import com.example.charity.model.FundraisingEvent;
import com.example.charity.repository.CollectionBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final MoneyService moneyService;

    public CollectionBox registerNewBox(CollectionBox box) {
        if (collectionBoxRepository.existsByIdentifier(box.getIdentifier())) {
            throw new IllegalArgumentException("Box with this identifier already exists.");
        }
        return collectionBoxRepository.save(box);
    }

    public List<CollectionBox> listAllBoxes() {
        return collectionBoxRepository.findAll();
    }

    public void unregisterBox(Long boxId) {
        CollectionBox box = getBox(boxId);
        moneyService.deleteByBox(box);
        collectionBoxRepository.deleteById(boxId);
    }

    public CollectionBox getBox(Long id) {
        return collectionBoxRepository.findById(id).orElseThrow(() -> new RuntimeException("Box not found"));
    }

    public void assignToEvent(CollectionBox box, FundraisingEvent event) {
        if (!box.isEmpty()) {
            throw new IllegalStateException("Box must be empty before assignment.");
        }
        box.setEvent(event);
        collectionBoxRepository.save(box);
    }
}
