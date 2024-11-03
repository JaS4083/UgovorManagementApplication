package com.omega.software.management.data.mapper;

import com.omega.software.management.data.dto.ArtiklDto;
import com.omega.software.management.data.dto.KupoprodajniUgovorDto;
import com.omega.software.management.data.entity.Artikl;
import com.omega.software.management.data.entity.KupoprodajniUgovor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KupoprodajniUgovorMapper {

    KupoprodajniUgovorMapper MAPPER = Mappers.getMapper(KupoprodajniUgovorMapper.class);

    KupoprodajniUgovorDto toDto(KupoprodajniUgovor entity);
    KupoprodajniUgovor fromDto(KupoprodajniUgovorDto dto);

    ArtiklDto toDto(Artikl entity);

    @Mapping(target = "ugovor", ignore = true)
    Artikl fromDto(ArtiklDto dto);

    @AfterMapping
    default void linkArtikls(@MappingTarget KupoprodajniUgovor entity) {
        if (entity.getArtiklis() != null) {
            for (Artikl artikl : entity.getArtiklis()) {
                artikl.setUgovor(entity);
            }
        }
    }


}
