package edu.matc.persistence;

import java.util.List;

public interface GenericDAO<T> {

    T getById(int id);

    int insert(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> getAll();
}
