package org.example.dessertbackend.service;

import org.example.dessertbackend.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectAll();
    
    List<Category> selectByCondition(String name, String description);
    
    Category selectById(Integer id);
    
    int insert(Category category);
    
    int update(Category category);
    
    int deleteById(Integer id);
    
    int batchDelete(List<Integer> ids);
}