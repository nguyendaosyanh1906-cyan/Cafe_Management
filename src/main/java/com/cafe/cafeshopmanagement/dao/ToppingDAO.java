package com.cafe.cafeshopmanagement.dao;

import com.cafe.cafeshopmanagement.model.Topping;
import java.util.List;

public interface ToppingDAO {
    List<Topping> getAllToppings();
    List<Topping> getActiveToppings();
    boolean addTopping(Topping t);
    boolean updateTopping(Topping t);
    boolean deleteTopping(int toppingId);
}