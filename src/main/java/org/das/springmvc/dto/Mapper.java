package org.das.springmvc.dto;

public interface Mapper<E, T> {
    T toDto(E e);
    E toEntity(T t);
}
