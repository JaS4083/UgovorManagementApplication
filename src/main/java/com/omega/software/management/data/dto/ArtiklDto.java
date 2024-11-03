package com.omega.software.management.data.dto;

import com.omega.software.management.data.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtiklDto implements StatusProvider {
    private Long id;
    private String naziv;
    private String dobavljac;
    private int kolicina;
    private Status status;
}
