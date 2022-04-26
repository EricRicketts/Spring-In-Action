package com.example.repositories;

import com.example.model.TacoOrder;

import java.util.Optional;

public interface OrderRepository {

    public TacoOrder save(TacoOrder order);

    public Optional<TacoOrder> findById(Long id);
}
