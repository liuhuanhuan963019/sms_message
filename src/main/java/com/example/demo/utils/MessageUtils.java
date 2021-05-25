package com.example.demo.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class MessageUtils {
    @Autowired
    RestTemplate restTemplate;

    @Value("${sms.default.connect.timeout}")
    private String DEFAULT_CONNECT_TIMEOUT;

    @Value("${sms.default.read.timeout}")
    private String DEFAULT_READ_TIMEOUT;

    @Value("${sms.timeout}")
    private String SMS_TIMEOUT;

    @Value("${sms.product}")
    private String SMS_PRODUCT;

    @Value("${sms.domain}")
    private String SMS_DOMAIN;

    @Value("${sms.access.key.id}")
    private String SMS_ACCESSKEYID;

    @Value("${sms.access.key.secret}")
    private String SMS_ACCESSKEYSECRET;

    @Value("${sms.template.code}")
    private String TEMPLATE_CODE;

    private static String code;//code对应你短信目标里面的参数

    public Map getPhoneMsg(String phone) {
        if (phone == null || phone == "") {
            System.out.println("手机号为空");
            return null;
        }
        // 设置超时时间-可自行调整
        System.setProperty(DEFAULT_CONNECT_TIMEOUT, SMS_TIMEOUT);
        System.setProperty(DEFAULT_READ_TIMEOUT, SMS_TIMEOUT);
        // 初始化ascClient需要的几个参数
        final String product = SMS_PRODUCT;
        final String domain = SMS_DOMAIN;
        // 替换成你的AK
        final String accessKeyId = SMS_ACCESSKEYID;
        final String accessKeySecret = SMS_ACCESSKEYSECRET;
        // 初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                accessKeyId, accessKeySecret);

        Map map = new HashMap();
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product,
                    domain);

            //获取验证码
            code = vcode();
            IAcsClient acsClient = new DefaultAcsClient(profile);
            // 组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            // 使用post提交
            request.setMethod(MethodType.POST);
            // 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
            request.setPhoneNumbers(phone);
            // 必填:短信签名-可在短信控制台中找到
            request.setSignName("java学习");
            // 必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(TEMPLATE_CODE);
            // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            // 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{ \"code\":\"" + code + "\"}");
            // 可选-上行短信扩展码(无特殊需求用户请忽略此字段)
            // request.setSmsUpExtendCode("90997");
            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");
            // 请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            map.put("status", sendSmsResponse.getCode());
            if (sendSmsResponse.getCode() != null
                    && sendSmsResponse.getCode().equals("OK")) {
                // 请求成功
                map.put("msg", code);
            } else {
                //如果验证码出错，会输出错误码告诉你具体原因
                map.put("msg", sendSmsResponse.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "FAIL");
            map.put("msg", "获取短信验证码失败");
        }
        return map;
    }

    /**
     * 生成6位随机数验证码
     *
     * @return
     */
    public static String vcode() {
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }

}
