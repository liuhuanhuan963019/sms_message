package com.example.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@Mapper
public interface MessageMapper {
    @Select("select * from SMS_MESSAGE where PHONE=#{phone}")
    Map selectSMSDataByPhone(String phone);

    @Update("update SMS_MESSAGE set SMS_CODE = #{smsCode} where phone=#{phone}")
    int updateSMSDataByPhone(Map smsMap);

    @Insert("insert into SMS_MESSAGE (PHONE,SMS_CODE) values (#{phone},#{smsCode})")
    int insert(Map smsMap);
}
