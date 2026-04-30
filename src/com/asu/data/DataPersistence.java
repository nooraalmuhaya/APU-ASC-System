package com.asu.data;

    import java.util.List;

public interface DataPersistence<T> {

    void create(T entity);

    List<T> readAll();

    void update(T entity);

    void delete(String id);
}

