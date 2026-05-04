package com.maxu.maxuadmin.healthdiet.service;

import java.util.List;

public interface RecipeFavouriteService {

    void add(Long userId, Long recipeId);

    void remove(Long userId, Long recipeId);

    boolean isFavorite(Long userId, Long recipeId);

    List<Long> listByUser(Long userId);
}
