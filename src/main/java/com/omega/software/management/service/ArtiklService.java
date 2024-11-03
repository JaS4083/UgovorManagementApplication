package com.omega.software.management.service;

import com.omega.software.management.data.dto.ArtiklDto;
import com.omega.software.management.data.entity.Artikl;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.exception.ResourceNotFoundException;
import com.omega.software.management.data.mapper.KupoprodajniUgovorMapper;
import com.omega.software.management.data.repository.ArtiklRepository;
import com.omega.software.management.data.repository.KupoprodajniUgovorRepository;
import com.omega.software.management.data.validation.UgovorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtiklService {

    private final KupoprodajniUgovorRepository ugovorRepository;
    private final ArtiklRepository artiklRepository;
    private final UgovorValidator validator;

    public ArtiklDto createArtikl(Long ugovorId, ArtiklDto artiklDto) {
        final var artikl = KupoprodajniUgovorMapper.MAPPER.fromDto(artiklDto);
        KupoprodajniUgovor ugovor = ugovorRepository.findById(ugovorId).orElseThrow(() -> new ResourceNotFoundException("No entity found by id: " + ugovorId));

        ugovor.getArtiklis().add(artikl);
        artikl.setUgovor(ugovor);

        final var saved = artiklRepository.save(artikl);
        return KupoprodajniUgovorMapper.MAPPER.toDto(saved);
    }

    public ArtiklDto getArtiklById(Long id) {
        final var artikl = artiklRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No entity found by id: " + id));
        return  KupoprodajniUgovorMapper.MAPPER.toDto(artikl);
    }

    public ArtiklDto updateArtikl(Long id, ArtiklDto dto) {
        final var artikl = artiklRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No entity found by id: " + id));

        validator.validateUpdateStatus(dto, artikl);

        artikl.setNaziv(dto.getNaziv());
        artikl.setDobavljac(dto.getDobavljac());
        artikl.setKolicina(dto.getKolicina());
        artikl.setStatus(dto.getStatus());
        final var saved = artiklRepository.save(artikl);
        return KupoprodajniUgovorMapper.MAPPER.toDto(saved);
    }

    public void deleteArtikl(Long id) {
        artiklRepository.deleteById(id);
    }

    public List<ArtiklDto> getAllArtiklsByUgovorId(Long ugovorId) {
        final var ugovor = ugovorRepository.findById(ugovorId).orElseThrow(() -> new ResourceNotFoundException("No entity found by id: " + ugovorId));

        return ugovor.getArtiklis().stream().map(KupoprodajniUgovorMapper.MAPPER::toDto).toList();
    }
}
