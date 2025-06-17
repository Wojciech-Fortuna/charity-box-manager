package com.example.charity.repository;

import com.example.charity.model.CollectionBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionBoxRepository extends JpaRepository<CollectionBox, Long> {
    Optional<CollectionBox> findByIdentifier(String identifier);
    boolean existsByIdentifier(String identifier);
}
