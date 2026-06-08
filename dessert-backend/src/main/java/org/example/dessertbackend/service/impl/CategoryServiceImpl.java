package org.example.dessertbackend.service.impl;

import org.example.dessertbackend.entity.Category;
import org.example.dessertbackend.mapper.CategoryMapper;
import org.example.dessertbackend.mapper.DessertMapper;
import org.example.dessertbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private DessertMapper dessertMapper;
    
    @Override
    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }
    
    @Override
    public List<Category> selectByCondition(String name, String description) {
        return categoryMapper.selectByCondition(name, description);
    }
    
    @Override
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }
    
    @Override
    public int insert(Category category) {
        Integer firstMissingId = categoryMapper.findFirstMissingId();
        category.setId(firstMissingId);
        return categoryMapper.insert(category);
    }
    
    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }
    
    @Override
    public int deleteById(Integer id) {
        dessertMapper.updateCategoryIdToNull(id);
        return categoryMapper.deleteById(id);
    }
    
    @Override
    public int batchDelete(List<Integer> ids) {
        for (Integer id : ids) {
            dessertMapper.updateCategoryIdToNull(id);
        }
        return categoryMapper.batchDelete(ids);
    }
}