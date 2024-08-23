package com.zane.core.base.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.zane.base.GlobalVariables;
import com.zane.base.handler.DoExecutor;
import com.zane.core.GlobalUrlConfig;
import com.zane.core.base.BaseService;
import com.zane.dao.dto.LoginDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class BaseServiceImpl implements BaseService {

    @Resource
    private DoExecutor doExecutor;

    @Resource
    private GlobalUrlConfig globalUrlConfig;

    @Override
    public String encryptPassword(String password) {
        String encryptedPassword = "";
        String publicKeyStr = this.getPublicKey();
        try {

            PublicKey publicKey = getPubliceKeyFromString(publicKeyStr);
            encryptedPassword = encrypt(password, publicKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.debug("加密后的密码串:{}",encryptedPassword);
        return encryptedPassword;
    }

    @Override
    public void login(LoginDTO loginDTO) {
        String accessToken = "";
        String urlPrefix = globalUrlConfig.getGlobalUrlPrefix() + "ucenter/";
        String loginUrl = urlPrefix + "imile/login";
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("userCode",loginDTO.getUserCode());
        requestMap.put("password",this.encryptPassword(loginDTO.getPassword()));
        requestMap.put("lang",loginDTO.getLang() );
        requestMap.put("timeZone",loginDTO.getTimeZone());
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        String responseJSON = doExecutor.doCommonHttpPostForm(requestMap, headers, loginUrl);
        log.info("登录结果:{}",responseJSON);
        String status = (String) JSONPath.read(responseJSON, "$.status");
        if (Objects.equals(status, "success")) {
             accessToken = (String) JSONPath.read(responseJSON, "$.resultObject.access_token");
             log.info("accessToken:{}",accessToken);
        }
        // 将accessToken赋给全局变量作authotization
        GlobalVariables.AUTHORIZATION_CODE = "Bearer " + accessToken;
        log.info("Authorization = {}",GlobalVariables.AUTHORIZATION_CODE);
    }


    /**
     * RSA加密-公钥
     *
     * @return
     */
    private String getPublicKey() {
        return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrTwxoTL1REQ1vJgrXqVpbG+EdNF+JFvQ/SLSO4MARMnPUXMx320oGklKEchR+JLcuxfIbRariYZAv7M+uhUcEatKhoYhA14zbshMpeEydpv2OFuPIz9t27miis0Qu2/WrZWj2GzZFi0tOdFvUeGT6aztJwomra8WRl925BN5+vwIDAQAB";
    }

    private static PublicKey getPubliceKeyFromString(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密算法
     */
    private static String encrypt(String originalString,PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] encryptBytes = cipher.doFinal(originalString.getBytes());
        return Base64.getEncoder().encodeToString(encryptBytes);
    }


    public static void main(String[] args) {
        BaseServiceImpl baseService = new BaseServiceImpl();
        String s = baseService.encryptPassword("01230234zzh!@#");
        System.out.println(s);
    }
}
