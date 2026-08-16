package com.library.repository;

import com.library.model.LibraryItem;
import com.library.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryItemRepository extends JpaRepository<LibraryItem, Long> {
    Optional<LibraryItem> findByCatalogueId(String catalogueId);
    List<LibraryItem> findByStatus(ItemStatus status);
    long countByStatus(ItemStatus status);
}
