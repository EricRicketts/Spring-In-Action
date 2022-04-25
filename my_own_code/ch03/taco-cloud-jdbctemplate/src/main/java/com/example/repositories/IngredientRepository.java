package com.example.repositories;

import com.example.model.Ingredient;

import java.util.Optional;

public interface IngredientRepository {

    public Iterable<Ingredient> findAll();

    public Optional<Ingredient> findById(String id);

    public Ingredient save(Ingredient ingredient);
}
