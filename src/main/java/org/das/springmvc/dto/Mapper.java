package org.das.springmvc.dto;

//todo export in main branch
public interface Mapper<E, T> {
    T toDto(E e);
    E toEntity(T t);
}
