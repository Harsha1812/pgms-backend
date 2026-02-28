package com.pgms.business.repository;

import com.pgms.business.repository.entity.Business;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class BusinessSpecifications {

  public static Specification<Business> byOwner(UUID ownerId) {
    return (root, query, cb) ->
      cb.equal(root.get("ownerId"), ownerId);
  }

  public static Specification<Business> nameContains(String name) {
    return (root, query, cb) ->
      cb.like(
        cb.lower(root.get("name")),
        "%" + name.toLowerCase() + "%"
      );
  }

  public static Specification<Business> isActive(Boolean active) {
    return (root, query, cb) ->
      cb.equal(root.get("isActive"), active);
  }
}