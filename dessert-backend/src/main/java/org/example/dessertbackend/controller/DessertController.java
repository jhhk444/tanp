
package org.example.dessertbackend.controller;
import org.example.dessertbackend.entity.Dessert;
import org.example.dessertbackend.service.DessertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dessert")
public class DessertController {
    @Autowired
    DessertService dessertService;

    @GetMapping("/list")
    public Map<String,Object> list(
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return dessertService.getList(categoryId, name, description, minPrice, maxPrice, page, size);
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Dessert dessert){
        dessertService.add(dessert);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Dessert dessert){
        dessertService.update(dessert);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Integer id){
        dessertService.delete(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    @PostMapping("/batchDelete")
    public Map<String, Object> batchDelete(@RequestBody Map<String, List<Integer>> params){
        dessertService.batchDelete(params.get("ids"));
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
}
