package com.omega.software.management.controller;

import com.omega.software.management.data.dto.ArtiklDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.omega.software.management.service.ArtiklService;

import java.util.List;

@RestController
@RequestMapping("/api/artikli")
@RequiredArgsConstructor
public class ArtiklController {

    private final ArtiklService artiklService;

    @Operation(summary = "Create an Artikle and assign to Ugovor", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('MANAGER','MODERATOR')")
    @PostMapping("/ugovori/{ugovorId}")
    public ResponseEntity<ArtiklDto> createArtikl(@PathVariable Long ugovorId,  @RequestBody ArtiklDto artiklDto) {
        var createdArtikl = artiklService.createArtikl(ugovorId, artiklDto);
        return ResponseEntity.ok(createdArtikl);
    }

    @Operation(summary = "Get Artikl by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('VIEWER', 'MANAGER','MODERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ArtiklDto> getArtiklById(@PathVariable Long id) {
        return ResponseEntity.ok(artiklService.getArtiklById(id));
    }

    @Operation(summary = "Update Artikl by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('VIEWER', 'MANAGER','MODERATOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ArtiklDto> updateArtikl(@PathVariable Long id, @RequestBody ArtiklDto artiklDto) {
        var existingArtikl = artiklService.updateArtikl(id, artiklDto);
        return ResponseEntity.ok(existingArtikl);
    }

    @Operation(summary = "Delete Artikl by id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('MANAGER','MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtikl(@PathVariable Long id) {
        artiklService.deleteArtikl(id);
            return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get all Artikls by Kupoprodajni Ugovor id", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('VIEWER', 'MANAGER','MODERATOR')")
    @GetMapping("/ugovori/{ugovorId}")
    public ResponseEntity<List<ArtiklDto>> getAllArtiklsByUgovorId(@PathVariable Long ugovorId) {
        final var allArtiklsByUgovorId = artiklService.getAllArtiklsByUgovorId(ugovorId);
        return ResponseEntity.ok(allArtiklsByUgovorId);
    }
}
