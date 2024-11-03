package com.omega.software.management.data.repository;

import com.omega.software.management.data.entity.KupoprodajniUgovor;
import com.omega.software.management.data.enums.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class KupoprodajniUgovorSpecifications {
    public static Specification<KupoprodajniUgovor> filterBy(String kupac, Status status, Boolean isActive) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (kupac != null) {
                predicates.add(builder.like(root.get("kupac"), "%" + kupac + "%"));
            }

            if (status != null) {
                predicates.add(builder.equal(root.get("status"),  status));
            }

            if (isActive != null) {
                predicates.add(builder.equal(root.get("isActive"), isActive));
            }

            predicates.add(builder.equal(root.get("isDeleted"), Boolean.FALSE));

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}