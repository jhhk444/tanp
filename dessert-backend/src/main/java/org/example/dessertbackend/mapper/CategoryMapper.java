package org.example.dessertbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.dessertbackend.entity.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectAll();
    
    List<Category> selectByCondition(@Param("name") String name, @Param("description") String description);
    
    Category selectById(@Param("id") Integer id);
    
    int insert(Category category);
    
    int update(Category category);
    
    int deleteById(@Param("id") Integer id);
    
    int batchDelete(@Param("ids") List<Integer> ids);
    
    Integer getMaxId();
    Integer findFirstMissingId();
}