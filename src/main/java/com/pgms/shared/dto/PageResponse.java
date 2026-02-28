package com.pgms.shared.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(
  List<T> items,
  int page,
  int size,
  long totalElements,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious
) {
  public static <E, R> PageResponse<R> from(
    Page<E> page,
    Function<E, R> mapper
  ) {

    List<R> mappedItems = page.getContent()
      .stream()
      .map(mapper)
      .toList();

    return new PageResponse<>(
      mappedItems,
      page.getNumber(),
      page.getSize(),
      page.getTotalElements(),
      page.getTotalPages(),
      page.hasNext(),
      page.hasPrevious()
    );
  }

}