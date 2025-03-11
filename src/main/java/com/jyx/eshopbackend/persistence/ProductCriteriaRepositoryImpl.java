package com.jyx.eshopbackend.persistence;

import com.jyx.eshopbackend.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository{

    @PersistenceContext
    private final EntityManager entityManager;

    public ProductCriteriaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> root = cq.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            // name like %name%
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }

        if (minPrice != null) {
            // price >= minPrice
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            // price <= minPrice
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
