package com.pgms.shared.idempotency.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.shared.exception.BusinessValidationException;
import com.pgms.shared.idempotency.repository.IdempotencyRepository;
import com.pgms.shared.idempotency.repository.entity.IdempotencyKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

  private final IdempotencyRepository repository;
  private final ObjectMapper objectMapper;

  @Transactional
  public <T, R> R execute(
    String idempotencyKey,
    String endpoint,
    UUID ownerId,
    T requestBody,
    Supplier<R> supplier,
    Class<R> responseType) {

    if (idempotencyKey == null || idempotencyKey.isBlank()) {
      throw new BusinessValidationException("Missing Idempotency-Key header");
    }

    String requestHash = hash(requestBody);

    Optional<IdempotencyKey> existing =
      repository.findByIdempotencyKeyAndEndpointAndOwnerId(
        idempotencyKey,
        endpoint,
        ownerId
      );

    if (existing.isPresent()) {

      IdempotencyKey record = existing.get();

      if (!record.getRequestHash().equals(requestHash)) {
        throw new BusinessValidationException(
          "Idempotency key reused with different payload");
      }

      return deserialize(record.getResponseBody(), responseType);
    }

    // Execute business logic
    R response = supplier.get();

    IdempotencyKey record = new IdempotencyKey();
    record.setId(UUID.randomUUID());
    record.setIdempotencyKey(idempotencyKey);
    record.setOwnerId(ownerId);
    record.setEndpoint(endpoint);
    record.setRequestHash(requestHash);
    record.setResponseBody(serialize(response));
    record.setStatusCode(200);
    record.setCreatedAt(LocalDateTime.now());

    try {
      repository.save(record);
    } catch (DataIntegrityViolationException e) {
      // Another thread inserted same key concurrently
      IdempotencyKey existingRecord =
        repository.findByIdempotencyKeyAndEndpointAndOwnerId(
          idempotencyKey,
          endpoint,
          ownerId
        ).orElseThrow();

      return deserialize(existingRecord.getResponseBody(), responseType);
    }

    return response;
  }

  private <T> String hash(T body) {
    try {
      String json = objectMapper.writeValueAsString(body);

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(json.getBytes(StandardCharsets.UTF_8));

      return Base64.getEncoder().encodeToString(hashBytes);

    } catch (Exception e) {
      throw new RuntimeException("Failed to hash request", e);
    }
  }

  private <R> String serialize(R response) {
    try {
      return objectMapper.writeValueAsString(response);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize response", e);
    }
  }

  private <R> R deserialize(String json, Class<R> type) {
    try {
      return objectMapper.readValue(json, type);
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize response", e);
    }
  }
}
