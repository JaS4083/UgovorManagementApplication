package com.omega.software.management.data.repository;

import com.omega.software.management.data.entity.KupoprodajniUgovor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KupoprodajniUgovorRepository extends JpaRepository<KupoprodajniUgovor, Long>,
        JpaSpecificationExecutor<KupoprodajniUgovor> {

    Optional<KupoprodajniUgovor> findByIdAndIsDeletedFalse(Long id);

    @Query(value = "SELECT * FROM kupoprodajni_ugovor ku WHERE " +
            "(:kupac IS NULL OR ku.kupac LIKE %:kupac%) AND " +
            "(:status IS NULL OR ku.status = CAST(:status AS contract_status)) AND " +
            "(:isActive IS NULL OR ku.is_active = :isActive) AND " +
            "ku.is_deleted = false", nativeQuery = true)
    List<KupoprodajniUgovor> findByOptionalFilters(
            @Param("kupac") String kupac,
            @Param("status") String status,
            @Param("isActive") Boolean isActive);
}
