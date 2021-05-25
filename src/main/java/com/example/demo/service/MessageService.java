package com.example.demo.service;

import com.example.demo.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageService {
    @Autowired
    public MessageMapper messageMapper;

    public Map selectSMSDataByPhone(String phone) {
        return messageMapper.selectSMSDataByPhone(phone);
    }

    public int updateSMSDataByPhone(Map smsMap) {
        return messageMapper.updateSMSDataByPhone(smsMap);
    }

    public int insert(Map smsMap) {
        return messageMapper.insert(smsMap);
    }
}
