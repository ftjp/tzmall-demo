package com.tzmall.operations.im.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tzmall.operations.im.api.API.AuthApi;
import com.tzmall.operations.im.api.bean.YunXinResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.tzmall.operations.im.support.UrlConst.AppKey;
import static com.tzmall.operations.im.support.UrlConst.AppSecret;

public class ImUtil {
	private static Logger logger = LoggerFactory.getLogger(ImUtil.class);

	public static YunXinResponse postUtil(Object object, String url) throws JsonProcessingException {
		// 后台 参数
		String nonce = UUID.randomUUID().toString();
		String curTime = String.valueOf(System.currentTimeMillis() / 1000);
		String checkSum = getCheckSum(AppSecret, nonce, curTime);
		// 传参
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(object);
		Map paramMap = objectMapper.readValue(json, Map.class);
		// 请求头
		Map<String, String> headers = new HashMap<>();
		headers.put("AppKey", AppKey);
		headers.put("Nonce", nonce);
		headers.put("CurTime", curTime);
		headers.put("CheckSum", checkSum);
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		String result = HttpUtil.createPost(url)
				.addHeaders(headers)
				.form(paramMap)
				.timeout(5000)//超时，毫秒
				.execute().body();
		logger.info("******** 请求链接{},传参{},返回结果{} ****",url,paramMap,result);
		YunXinResponse yunXinResponse = BeanUtil.copyProperties(JSONUtil.parseObj(result), YunXinResponse.class);
		if (!"200".equals(yunXinResponse.getCode())) {
			throw new RuntimeException(String.format("请求失败,链接[%s],传参[%s],返回结果[%s]",url,paramMap,result));
		}
		return yunXinResponse;
	}


	// 计算并获取CheckSum
	public static String getCheckSum(String appSecret, String nonce, String curTime) {
		return encode("sha1", appSecret + nonce + curTime);
	}

	// 计算并获取md5值
	public static String getMD5(String requestBody) {
		return encode("md5", requestBody);
	}

	private static String encode(String algorithm, String value) {
		if (value == null) {
			return null;
		}
		try {
			MessageDigest messageDigest
					= MessageDigest.getInstance(algorithm);
			messageDigest.update(value.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
