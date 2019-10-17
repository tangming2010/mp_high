package com.goldensoft.mphigh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldensoft.mphigh.dao.ConstantMapper;
import com.goldensoft.mphigh.entity.Constant;

@Service
public class ConstantService {

	private static final String P_PAGE = "p.page";
	private static final String SEARCH_PROJECT_NAME = "searchProjectName";
	private static final String COOKIE = "cookie";
	private static final String PAGE_SIZE = "page_size";
	
	@Autowired
	private ConstantMapper constantMapper;
	

	public String getCookie() {
		return get(COOKIE);
	}
	
	
	public void setCookie(String cookie) {
		set(COOKIE, cookie);
	}

	
	public String getSearchProjectName() {
		return get(SEARCH_PROJECT_NAME);
	}
	
	
	public void setSearchProjectName(String searchProjectName) {
		set(SEARCH_PROJECT_NAME, searchProjectName);
	}
	
	public String getPage() {
		return get(P_PAGE);
	}
	
	public String getPageSize() {
		return get(PAGE_SIZE);
	}
	
	public void setPage(String page) {
		set(P_PAGE, page);
	}
	

	public String get(String key) {
		Constant entity = constantMapper.selectById(key);
		if (entity != null) {
			return entity.getDataValue();
		} else {
			return null;
		}
	}
	
	
	public void set(String key, String value) {
		Constant entity = constantMapper.selectById(key);
		if (entity == null) {
			entity = new Constant();
			entity.setDataKey(key);
			entity.setDataValue(value);
			constantMapper.insert(entity);
		} else {
			entity.setDataValue(value);
			constantMapper.updateById(entity);
		}
	}
	
	public String getUsername() {
		return get("username");
	}
	
	
	public String getPassword() {
		return get("password");
	}
	
	public String getQueryUrl() {
		return get("query_url");
	}
	
	public String getLoginUrl() {
		return get("login_url");
	}
	
	
	public String getSearchEndDate() {
		return get("searchEndDate");
	}
	
	
	public String getSearchStartDate() {
		return get("searchStartDate");
	}
	
	public String getStoken() {
		return get("stoken");
	}
	
}

