package org.das.springmvc.dto;

import java.util.List;

public interface Mapper<E, T> {
    T toDto(E e);
    List<T> toDto(List<E> e);
    E toEntity(T t);
}
