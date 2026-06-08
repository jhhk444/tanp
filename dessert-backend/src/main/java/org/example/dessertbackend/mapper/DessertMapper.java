package org.example.dessertbackend.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.dessertbackend.entity.Dessert;
import java.util.List;

@Mapper
public interface DessertMapper {
    List<Dessert> selectList(
            @Param("categoryId") Integer categoryId,
            @Param("name") String name,
            @Param("description") String description,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("offset") int offset, 
            @Param("size") int size);

    int count(
            @Param("categoryId") Integer categoryId,
            @Param("name") String name,
            @Param("description") String description,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

    int insert(Dessert dessert);
    int update(Dessert dessert);
    int deleteById(Integer id);
    int batchDelete(@Param("ids") List<Integer> ids);
    int updateCategoryIdToNull(@Param("categoryId") Integer categoryId);
}