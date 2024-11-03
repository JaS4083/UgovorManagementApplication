package com.omega.software.management.service;

import com.omega.software.management.data.dto.KupoprodajniUgovorDto;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.enums.Status;
import com.omega.software.management.data.exception.ResourceNotFoundException;
import com.omega.software.management.data.mapper.KupoprodajniUgovorMapper;
import com.omega.software.management.data.repository.KupoprodajniUgovorRepository;
import com.omega.software.management.data.repository.KupoprodajniUgovorSpecifications;
import com.omega.software.management.data.validation.UgovorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KupoprodajniUgovorService {
    private final KupoprodajniUgovorRepository ugovorRepository;
    private final UgovorValidator validator;

    public List<KupoprodajniUgovorDto> getAllUgovori(String kupac, Status status) {
        final var ugovors = ugovorRepository.findAll(
                KupoprodajniUgovorSpecifications.filterBy(kupac, status, Boolean.TRUE)
        );
        return ugovors.stream().map(KupoprodajniUgovorMapper.MAPPER::toDto).toList();
    }

    public KupoprodajniUgovorDto getUgovorById(Long id) {
        final var byId = ugovorRepository.findByIdAndIsDeletedFalse(id);

        return byId
                .map(KupoprodajniUgovorMapper.MAPPER::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No entity found by id: " + id));
    }

    public KupoprodajniUgovorDto createUgovor(KupoprodajniUgovorDto ugovorDto) {
        KupoprodajniUgovor entity = KupoprodajniUgovorMapper.MAPPER.fromDto(ugovorDto);
        entity.setIsActive(Boolean.TRUE);
        entity.setStatus(Status.KREIRANO);
        entity.setIsDeleted(Boolean.FALSE);

        KupoprodajniUgovor savedEntity = ugovorRepository.save(entity);

        return KupoprodajniUgovorMapper.MAPPER.toDto(savedEntity);
    }

    @Transactional
    public KupoprodajniUgovorDto updateUgovor(Long id, KupoprodajniUgovorDto updateUgovor) {
        KupoprodajniUgovor ugovor = ugovorRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No entity ugovor by id: " + id));

        // validate ugovor update request
        validator.validateDeliveryDate(updateUgovor, ugovor);
        validator.validateUpdateStatus(updateUgovor, ugovor);

        if(Status.ISPORUCENO.equals(updateUgovor.getStatus())) {
            ugovor.setIsActive(Boolean.FALSE);
        }

        ugovor.setKupac(updateUgovor.getKupac());
        ugovor.setBrojUgovora(updateUgovor.getBrojUgovora());
        ugovor.setDatumAkontacije(updateUgovor.getDatumAkontacije());
        ugovor.setRokIsporuke(updateUgovor.getRokIsporuke());
        ugovor.setStatus(updateUgovor.getStatus());
        return KupoprodajniUgovorMapper.MAPPER.toDto(ugovorRepository.save(ugovor));

    }

    @Transactional
    public void softDeleteUgovor(Long id) {
        KupoprodajniUgovor ugovor = ugovorRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("No ugovor found by id: " + id));
        boolean isStatusKREIRANO = Status.KREIRANO.equals(ugovor.getStatus());

        if(!isStatusKREIRANO) {
            throw new ResourceNotFoundException("Moguće je brisati samo kupoprodajne ugovore koji su u statusu „KREIRANO“");
        }
        ugovor.setIsDeleted(Boolean.TRUE);
    }
}
