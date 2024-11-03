package com.omega.software.management.unittests;

import com.omega.software.management.data.dto.KupoprodajniUgovorDto;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.enums.Status;
import com.omega.software.management.data.exception.BadRequestException;
import com.omega.software.management.data.validation.UgovorValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest(classes = UgovorValidator.class)
public class UgovorValidatorTest {

    @Autowired
    private UgovorValidator validator;

    @Test
    public void statusNarucenoDifferentDeliveryDateThrowsException() {
        var entity = getDefaultEntity();
        entity.setStatus(Status.NARUCENO);
        var dto = getDefaultDto();
        dto.setStatus(Status.NARUCENO);
        dto.setRokIsporuke(LocalDate.now().plusDays(1));

        Assertions.assertThrows(BadRequestException.class, () -> validator.validateDeliveryDate(dto, entity));
    }

    @Test
    public void statusKreiranoToIsporuchenoThrowsException() {
        var entity = getDefaultEntity();
        entity.setStatus(Status.KREIRANO);
        var dto = getDefaultDto();
        dto.setStatus(Status.ISPORUCENO);
        dto.setRokIsporuke(LocalDate.now().plusDays(1));

        Assertions.assertThrows(BadRequestException.class, () -> validator.validateUpdateStatus(dto, entity));
    }

    @Test
    public void statusNarucenoToKreiranoThrowsException() {
        var entity = getDefaultEntity();
        entity.setStatus(Status.NARUCENO);
        var dto = getDefaultDto();
        dto.setStatus(Status.KREIRANO);
        dto.setRokIsporuke(LocalDate.now().plusDays(1));

        Assertions.assertThrows(BadRequestException.class, () -> validator.validateUpdateStatus(dto, entity));
    }

    private KupoprodajniUgovor getDefaultEntity(){
        return KupoprodajniUgovor.builder()
                .id(1L)
                .kupac("kupac")
                .brojUgovora("0001")
                .datumAkontacije(LocalDate.now())
                .rokIsporuke(LocalDate.now())
                .status(Status.NARUCENO)
                .artiklis(Set.of()).build();
    }
    private KupoprodajniUgovorDto getDefaultDto(){
        return KupoprodajniUgovorDto.builder()
                .id(1L)
                .kupac("kupac")
                .brojUgovora("0001")
                .datumAkontacije(LocalDate.now())
                .rokIsporuke(LocalDate.now())
                .status(Status.NARUCENO)
                .artiklis(List.of()).build();
    }
}
