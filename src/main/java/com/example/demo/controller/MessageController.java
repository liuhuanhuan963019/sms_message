package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.example.demo.service.MessageService;
import com.example.demo.utils.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(description = "短信接口")
@RequestMapping("/smsLogin")
@RestController
public class MessageController {
    @Autowired
    public MessageService messageService;

    @Autowired
    public MessageUtils messageUtils;

    @ApiOperation(value = "获取短信验证码接口", notes = "获取短信验证码接口")
    @GetMapping("/sendMessage")
    public Map<String, Object> getSMSMessage(String phone) {
        Map<String, Object> map = new HashMap<>();
        if (phone == null || phone == "") {
            map.put("code", "FAIL");
            map.put("msg", "手机号为空");
            return map;
        }
        Map smsMap = messageUtils.getPhoneMsg(phone);
        if("OK".equals(smsMap.get("status"))){
            Map data = messageService.selectSMSDataByPhone(phone);
            map.put("phone", phone);
            map.put("smsCode", smsMap.get("msg"));
            // 将验证码存入数据库  也可以考虑用redis等方式 这里就用数据库做例子
            if (data != null) {
                messageService.updateSMSDataByPhone(map);
            } else {
                messageService.insert(map);
            }
            smsMap.put("msg", "成功");
        }
        return smsMap;
    }

    @ApiOperation(value = "短信校验登录接口", notes = "短信校验登录接口")
    @GetMapping("/login")
    public Map<String, Object> login(String phone, String smsCode) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsCode)) {
            map.put("code", "FAIL");
            map.put("msg", "请检查数据");
            return map;
        }
        // 取出对应的验证码进行比较即可
        Map smsMap = messageService.selectSMSDataByPhone(phone);
        if (smsMap == null) {
            map.put("code", "FAIL");
            map.put("msg", "该手机号未发送验证码");
            return map;
        }
        String code = (String) smsMap.get("sms_code");
        if (!smsCode.equals(code)) {
            map.put("code", "FAIL");
            map.put("msg", "验证码不正确");
            return map;
        }
        map.put("code", "OK");
        map.put("msg", "success");
        return map;
    }
}
