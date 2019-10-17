package com.goldensoft.mphigh.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldensoft.mphigh.common.Commons;
import com.goldensoft.mphigh.service.ConstantService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@JobHandler(value = "loginTask")
public class LoginTask extends IJobHandler {

	@Autowired
	private ConstantService constantService;

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		log.info("开始登录系统获取数据");
		String cookie = acquireCookie(); 
		log.info("获取的cookie是：{}", cookie);
		constantService.setCookie(cookie);
		return ReturnT.SUCCESS;
	}

	private Map<String, String> loginParams(String username, String password) {
		Map<String, String> datas = new HashMap<>();
		datas.put("usercode", username);
		datas.put("password", password);
		datas.put("loginflag", "1");
		datas.put("btnLogin", "");
		return datas;
	}

	private String acquireCookie() throws IOException {
		String username = constantService.getUsername();
		String password = constantService.getPassword();
		String loginUrl = constantService.getLoginUrl();
		Map<String, String> cookies = Jsoup.connect(loginUrl).headers(Commons.headers(""))
				.data(loginParams(username, password)).method(Method.POST).execute().cookies();
		List<String> cookieList = cookies.entrySet().stream().map(entity -> entity.getKey() + "=" + entity.getValue())
				.collect(Collectors.toList());
		return StringUtils.join(cookieList, ";");
	}

}
