package com.pgms.branch.service.impl;

import com.pgms.branch.dto.BranchResponse;
import com.pgms.branch.dto.CreateBranchRequest;
import com.pgms.branch.dto.PatchBranchRequest;
import com.pgms.branch.dto.UpdateBranchRequest;
import com.pgms.branch.repository.BranchRepository;
import com.pgms.branch.repository.BranchSpecifications;
import com.pgms.branch.repository.entity.Branch;
import com.pgms.branch.service.BranchService;
import com.pgms.business.repository.BusinessRepository;
import com.pgms.business.repository.entity.Business;
import com.pgms.shared.dto.PageResponse;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.security.OwnerContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pgms.shared.util.Utility.updateIfNotNull;

@Service
public class BranchServiceImpl implements BranchService {

  @Autowired
  private BusinessRepository businessRepository;

  @Autowired
  private BranchRepository branchRepository;

  @Override
  public BranchResponse create(CreateBranchRequest request, UUID businessId) {

    UUID ownerId = OwnerContext.getOwnerId();

    Business business = businessRepository
      .findById(businessId)
      .orElseThrow(() ->
        new BusinessValidationException("Business not found"));

    if (!business.getOwnerId().equals(ownerId)) {
      throw new BusinessValidationException(
        "Business does not belong to owner");
    }

    if (branchRepository.existsByBusinessIdAndCode(
      businessId,
      request.code())) {
      throw new BusinessValidationException(
        "Branch code already exists for this business");
    }

    Branch branch = new Branch();

    branch.setOwnerId(ownerId);
    branch.setBusinessId(businessId);
    branch.setName(request.name());
    branch.setCode(request.code());
    branch.setCity(request.city());
    branch.setState(request.state());
    branch.setCountry(request.country());
    branch.setAddressLine1(request.addressLine1());
    branch.setAddressLine2(request.addressLine2());
    branch.setPincode(request.pincode());
    branch.setLatitude(request.latitude());
    branch.setLongitude(request.longitude());
    branch.setTotalFloors(request.totalFloors());
    branch.setTotalRooms(request.totalRooms());

    branch.setFixedLateFee(request.fixedLateFee());
    branch.setUpfrontDiscountPercentage(request.upfrontDiscountPercentage());
    branch.setUpfrontDiscountMinMonths(request.upfrontDiscountMinMonths());
    branch.setReferralRewardReferrer(request.referralRewardReferrer());
    branch.setReferralRewardReferred(request.referralRewardReferred());

    return new BranchResponse(
      branch.getId(),
      branch.getName(),
      branch.getCode(),
      branch.isActive(),
      branch.getVersion());
  }

  @Override
  public PageResponse<BranchResponse> list(UUID businessId,
                                           String name,
                                           Boolean active,
                                           String city,
                                           String state,
                                           Pageable pageable) {
    UUID ownerId = OwnerContext.getOwnerId();
    System.out.print("OwnerId is : "+ownerId.toString() );

    Specification<Branch> spec =
      Specification.where(BranchSpecifications.byOwner(ownerId))
        .and(BranchSpecifications.byBusiness(businessId));
    if (name != null && !name.isBlank()) {
      spec = spec.and(BranchSpecifications.nameContains(name));
    }

    if (active != null) {
      spec = spec.and(BranchSpecifications.isActive(active));
    }

    if (city != null && !city.isBlank()) {
      spec = spec.and(BranchSpecifications.cityEquals(city));
    }

    if (state != null && !state.isBlank()) {
      spec = spec.and(BranchSpecifications.stateEquals(state));
    }

    Page<Branch> page = branchRepository.findAll(spec, pageable);

    return PageResponse.from(
      page,
      this::toResponse);
  }

  @Override
  public BranchResponse get(UUID id) {
    Branch saved = branchRepository.getReferenceById(id);
    return new BranchResponse(
      saved.getId(),
      saved.getName(),
      saved.getCode(),
      saved.isActive(),
      saved.getVersion()
    );
  }

  @Override
  @Transactional
  public BranchResponse update(
    UUID businessId,
    UUID branchId,
    UpdateBranchRequest request) {

    UUID ownerId = OwnerContext.getOwnerId();

    Branch branch = branchRepository
      .findByIdAndOwnerIdAndBusinessIdAndVersion(
        branchId,
        ownerId,
        businessId,
        request.version()
      )
      .orElseThrow(() ->
        new ObjectOptimisticLockingFailureException(
          Branch.class,
          branchId
        )
      );

    // 🔥 Critical line — attach client version
    branch.setVersion(request.version());

    branch.setName(request.name());
    branch.setCity(request.city());
    branch.setState(request.state());
    branch.setCountry(request.country());
    branch.setAddressLine1(request.addressLine1());
    branch.setAddressLine2(request.addressLine2());
    branch.setPincode(request.pincode());
    branch.setTotalFloors(request.totalFloors());
    branch.setTotalRooms(request.totalRooms());
    branch.setFixedLateFee(request.fixedLateFee());
    branch.setUpfrontDiscountPercentage(request.upfrontDiscountPercentage());
    branch.setUpfrontDiscountMinMonths(request.upfrontDiscountMinMonths());
    branch.setReferralRewardReferrer(request.referralRewardReferrer());
    branch.setReferralRewardReferred(request.referralRewardReferred());

    return new BranchResponse(
      branch.getId(),
      branch.getName(),
      branch.getCode(),
      branch.isActive(),
      branch.getVersion()
    );
  }

  @Transactional
  @Override
  public BranchResponse patch(
    UUID businessId,
    UUID branchId,
    PatchBranchRequest request) {

    UUID ownerId = OwnerContext.getOwnerId();

    Branch branch = branchRepository
      .findByIdAndOwnerIdAndBusinessIdAndVersion(
        branchId,
        ownerId,
        businessId,
        request.version()
      )
      .orElseThrow(() ->
        new BusinessValidationException("Branch was modified by another user")
      );

    updateIfNotNull(request.name(), branch::setName);
    updateIfNotNull(request.city(), branch::setCity);
    updateIfNotNull(request.state(), branch::setState);
    updateIfNotNull(request.country(), branch::setCountry);
    updateIfNotNull(request.addressLine1(), branch::setAddressLine1);
    updateIfNotNull(request.addressLine2(), branch::setAddressLine2);
    updateIfNotNull(request.pincode(), branch::setPincode);
    updateIfNotNull(request.totalFloors(), branch::setTotalFloors);
    updateIfNotNull(request.totalRooms(), branch::setTotalRooms);
    updateIfNotNull(request.fixedLateFee(), branch::setFixedLateFee);
    updateIfNotNull(request.upfrontDiscountPercentage(), branch::setUpfrontDiscountPercentage);
    updateIfNotNull(request.upfrontDiscountMinMonths(), branch::setUpfrontDiscountMinMonths);
    updateIfNotNull(request.referralRewardReferrer(), branch::setReferralRewardReferrer);
    updateIfNotNull(request.referralRewardReferred(), branch::setReferralRewardReferred);

    return toResponse(branch);
  }

  private BranchResponse toResponse(Branch branch) {
    return new BranchResponse(
      branch.getId(),
      branch.getName(),
      branch.getCode(),
      branch.isActive(),
      branch.getVersion());
  }

}
