package org.example.dessertbackend.service;
import org.example.dessertbackend.entity.Dessert;
import java.util.List;
import java.util.Map;

public interface DessertService {
    Map<String,Object> getList(Integer categoryId, String name, String description, Double minPrice, Double maxPrice, int page, int size);
    int add(Dessert dessert);
    int update(Dessert dessert);
    int delete(Integer id);
    int batchDelete(List<Integer> ids);
}