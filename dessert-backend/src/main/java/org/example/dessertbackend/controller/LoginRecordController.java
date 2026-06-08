
package org.example.dessertbackend.controller;

import org.example.dessertbackend.entity.LoginRecord;
import org.example.dessertbackend.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loginRecord")
public class LoginRecordController {
    @Autowired
    LoginRecordService loginRecordService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        return loginRecordService.getList();
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody LoginRecord record) {
        loginRecordService.addRecord(record);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }
}
