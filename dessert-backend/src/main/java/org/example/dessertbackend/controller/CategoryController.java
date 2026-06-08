
package org.example.dessertbackend.controller;

import org.example.dessertbackend.entity.Category;
import org.example.dessertbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/list")
    public List<Category> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description) {
        if ((name == null || name.isEmpty()) && (description == null || description.isEmpty())) {
            return categoryService.selectAll();
        }
        return categoryService.selectByCondition(name, description);
    }
    
    @GetMapping("/get/{id}")
    public Category getById(@PathVariable("id") Integer id) {
        return categoryService.selectById(id);
    }
    
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Category category) {
        categoryService.insert(category);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
    
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Category category) {
        categoryService.update(category);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
    
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        categoryService.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
    
    @PostMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params) {
        categoryService.batchDelete(params.get("ids"));
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
}
