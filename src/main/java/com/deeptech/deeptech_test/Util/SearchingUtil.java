package com.deeptech.deeptech_test.Util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchingUtil {

    public static <T> Specification<T> build(Object object) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (object == null) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);

                    if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                        continue;
                    }

                    if (value instanceof String) {
                        predicates.add(criteriaBuilder.like(root.get(field.getName()), "%" + value + "%"));
                    }else if (value instanceof LocalDate) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(field.getName()), (LocalDate) value));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(field.getName()), value));
                    }
                } catch (IllegalAccessException e){}
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
