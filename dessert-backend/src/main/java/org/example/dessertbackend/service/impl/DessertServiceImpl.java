package org.example.dessertbackend.service.impl;
import org.example.dessertbackend.entity.Dessert;
import org.example.dessertbackend.mapper.DessertMapper;
import org.example.dessertbackend.service.DessertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DessertServiceImpl implements DessertService {
    @Autowired
    DessertMapper dessertMapper;

    @Override
    public Map<String, Object> getList(Integer categoryId, String name, String description, Double minPrice, Double maxPrice, int page, int size) {
        int offset = (page-1)*size;
        List<Dessert> list = dessertMapper.selectList(categoryId, name, description, minPrice, maxPrice, offset, size);
        int total = dessertMapper.count(categoryId, name, description, minPrice, maxPrice);
        Map<String,Object> map = new HashMap<>();
        map.put("records",list);
        map.put("total",total);
        return map;
    }

    @Override
    public int add(Dessert dessert) {return dessertMapper.insert(dessert);}
    @Override
    public int update(Dessert dessert) {return dessertMapper.update(dessert);}
    @Override
    public int delete(Integer id) {return dessertMapper.deleteById(id);}
    @Override
    public int batchDelete(List<Integer> ids) {return dessertMapper.batchDelete(ids);}
}