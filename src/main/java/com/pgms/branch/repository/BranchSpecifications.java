package com.pgms.branch.repository;

import com.pgms.branch.repository.entity.Branch;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class BranchSpecifications {

  public static Specification<Branch> byOwner(UUID ownerId) {
    return (root, query, cb) ->
      cb.equal(root.get("ownerId"), ownerId);
  }

  public static Specification<Branch> byBusiness(UUID businessId) {
    return (root, query, cb) ->
      cb.equal(root.get("businessId"), businessId);
  }

  public static Specification<Branch> nameContains(String name) {
    return (root, query, cb) ->
      cb.like(
        cb.lower(root.get("name")),
        "%" + name.toLowerCase() + "%"
      );
  }

  public static Specification<Branch> isActive(Boolean active) {
    return (root, query, cb) ->
      cb.equal(root.get("isActive"), active);
  }

  public static Specification<Branch> cityEquals(String city) {
    return (root, query, cb) ->
      cb.equal(
        cb.lower(root.get("city")),
        city.toLowerCase()
      );
  }

  public static Specification<Branch> stateEquals(String state) {
    return (root, query, cb) ->
      cb.equal(
        cb.lower(root.get("state")),
        state.toLowerCase()
      );
  }
}
