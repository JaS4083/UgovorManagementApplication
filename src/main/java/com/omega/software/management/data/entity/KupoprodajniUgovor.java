package com.omega.software.management.data.entity;

import com.omega.software.management.data.dto.StatusProvider;
import com.omega.software.management.data.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.EnumJdbcType;

import java.sql.Types;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "kupoprodajni_ugovor")
public class KupoprodajniUgovor implements StatusProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kupac;

    @Column(name = "broj_ugovora")
    private String brojUgovora;
    private LocalDate datumAkontacije;
    private LocalDate rokIsporuke;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "ugovor", cascade = {CascadeType.MERGE, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Artikl> artiklis = new HashSet<>();
}
