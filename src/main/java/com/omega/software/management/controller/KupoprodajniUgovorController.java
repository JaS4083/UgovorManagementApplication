package com.omega.software.management.controller;

import com.omega.software.management.data.dto.KupoprodajniUgovorDto;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.enums.Status;
import com.omega.software.management.service.KupoprodajniUgovorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ugovori")
@RequiredArgsConstructor
public class KupoprodajniUgovorController {
    private final KupoprodajniUgovorService ugovorService;

    @Operation(summary = "Get all active Kupoprodajni Ugovori with possibility to filter by kupac name or/and by status",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('VIEWER', 'MANAGER','MODERATOR')")
    @GetMapping
    public List<KupoprodajniUgovorDto> getAllUgovori(
            @RequestParam(required = false) String kupac,
            @RequestParam(required = false) Status status) {
        return ugovorService.getAllUgovori(kupac, status);
    }

    @Operation(summary = "Get Kupoprodajni Ugovor by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('VIEWER', 'MANAGER','MODERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<KupoprodajniUgovorDto> getUgovorById(@PathVariable Long id) {
        return ResponseEntity.ok(ugovorService.getUgovorById(id));
    }

    @Operation(summary = "Creates new Kupoprodajni Ugovor", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('MANAGER','MODERATOR')")
    @PostMapping
    public ResponseEntity<KupoprodajniUgovorDto> createUgovor(@RequestBody KupoprodajniUgovorDto ugovor) {
        return ResponseEntity.ok(ugovorService.createUgovor(ugovor));
    }

    @Operation(summary = "Updates Kupoprodajni Ugovor", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('MANAGER','MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<KupoprodajniUgovorDto> updateUgovor(@PathVariable Long id,
                                                              @RequestBody KupoprodajniUgovorDto ugovor) {
        return ResponseEntity.ok(ugovorService.updateUgovor(id, ugovor));
    }

    @Operation(summary = "Delete of Kupoprodajni Ugovor", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('MANAGER','MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteUgovor(@PathVariable Long id) {
        ugovorService.softDeleteUgovor(id);
        return ResponseEntity.accepted().build();
    }
}
