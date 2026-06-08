package org.example.dessertbackend.service;

import org.example.dessertbackend.entity.LoginRecord;

import java.util.List;
import java.util.Map;

public interface LoginRecordService {
    Map<String, Object> getList();
    int addRecord(LoginRecord record);
}