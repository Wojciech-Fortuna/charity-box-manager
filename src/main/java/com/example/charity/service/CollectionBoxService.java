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

    public CollectionBox registerNewBox(CollectionBox box) {
        return collectionBoxRepository.save(box);
    }

    public List<CollectionBox> listAllBoxes() {
        return collectionBoxRepository.findAll();
    }

    public void unregisterBox(Long boxId) {
        collectionBoxRepository.deleteById(boxId);
    }

    public CollectionBox getBox(Long id) {
        return collectionBoxRepository.findById(id).orElseThrow(() -> new RuntimeException("Box not found"));
    }

    public void assignToEvent(CollectionBox box, FundraisingEvent event) {
        box.setEvent(event);
        collectionBoxRepository.save(box);
    }
}
