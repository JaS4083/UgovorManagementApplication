package com.omega.software.management.data.entity;

import com.omega.software.management.data.dto.StatusProvider;
import com.omega.software.management.data.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.descriptor.jdbc.EnumJdbcType;

import java.sql.Types;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "artikl")
public class Artikl implements StatusProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String dobavljac;
    private int kolicina;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "ugovor_id")
    private KupoprodajniUgovor ugovor;

}