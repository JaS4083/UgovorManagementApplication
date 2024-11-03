package com.omega.software.management.data.validation;

import com.omega.software.management.data.dto.KupoprodajniUgovorDto;
import com.omega.software.management.data.dto.StatusProvider;
import com.omega.software.management.data.exception.BadRequestException;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.enums.Status;
import org.springframework.stereotype.Component;


@Component
public class UgovorValidator {

    public void validateDeliveryDate(KupoprodajniUgovorDto dto, KupoprodajniUgovor entity) {
        boolean isDeliveryDateSame = entity.getRokIsporuke().equals(dto.getRokIsporuke());
        if(Status.NARUCENO.equals(entity.getStatus()) && !isDeliveryDateSame) {
            throw new BadRequestException("Rok isporuke je moguće mijenjati samo aktivnom kupoprodajnom ugovoru.");
        }
    }

    public <T extends StatusProvider> void validateUpdateStatus(T dto, T entity) {

        if(entity.getStatus().equals(Status.KREIRANO)
                && !(dto.getStatus().equals(Status.KREIRANO) || dto.getStatus().equals(Status.NARUCENO))) {
            throw new BadRequestException("Status „KREIRANO“ može prijeći samo u status „NARUČENO“.");
        }

        if(entity.getStatus().equals(Status.NARUCENO)
                && !(dto.getStatus().equals(Status.NARUCENO) || dto.getStatus().equals(Status.ISPORUCENO))) {
            throw new BadRequestException("Status „NARUČENO“ može prijeći samo u status „ISPORUČENO“.");
        }
    }
}
