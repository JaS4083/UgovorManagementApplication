package com.omega.software.management.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.omega.software.management.data.entity.Artikl;
import com.omega.software.management.data.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KupoprodajniUgovorDto implements StatusProvider {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("kupac")
    private String kupac;

    @JsonProperty("broj_ugovora")
    private String brojUgovora;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("datum_akontacije")
    private LocalDate datumAkontacije;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("rok_isporuke")
    private LocalDate rokIsporuke;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("artiklis")
    private List<ArtiklDto> artiklis;
}
