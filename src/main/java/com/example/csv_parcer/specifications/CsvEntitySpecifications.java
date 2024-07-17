package com.example.csv_parcer.specifications;

import com.example.csv_parcer.Entities.CsvEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author $ {Vladyslav Marii}
 **/
public class CsvEntitySpecifications {
    public static Specification<CsvEntity> containsKeyword(String keyword) {
        return (root, query, builder) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("column1")), likePattern),
                    builder.like(builder.lower(root.get("column2")), likePattern),
                    builder.like(builder.lower(root.get("column3")), likePattern)
            );
        };
    }
}
