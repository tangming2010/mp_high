package com.goldensoft.mphigh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.goldensoft.mphigh.dao.UserMapper;
import com.goldensoft.mphigh.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void deleteById() {
		int rows = userMapper.deleteById(1094592041087729666L);
		System.out.println("影响行数：" + rows);
	}
	
	
	@Test
	public void select() {
		userMapper.selectList(null).forEach(System.out::println);
	}
	
	
	@Test
	public void update() {
		User user = new User();
		user.setAge(26);
		LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery().le(User::getAge, 26);
		userMapper.update(user, wrapper);
	}
	
	
	@Test
	public void selectCondition() {
		List<User> list = userMapper.selectCondition(Wrappers.<User>lambdaQuery().gt(User::getAge, 25));
		list.forEach(System.out::println);
	}
	
	@Test
	public void save() {
		User user = new User();
		user.setName("刘民生");
		user.setAge(31);
		user.setEmail("lms@baomidou.com");
		user.setManagerId(1094592041087729666L);
		int rows = userMapper.insert(user);
		System.out.println("影响行数：" + rows);
	}
	
	@Test
	public void updateEntity() {
		User user = new User();
		user.setAge(32);
		user.setId(1174964365109686273L);
		int rows = userMapper.updateById(user);
		System.out.println("影响行数：" + rows);
	}
	
	@Test
	public void updateById() {
		User user = userMapper.selectById(1174964365109686273L);
		user.setEmail("lmss@baomidou.com");
		user.setVersion(user.getVersion());
		int rows = userMapper.updateById(user);
		System.out.println("影响行数：" + rows);
	}
	
	@Test
	public void testJsoup() throws Exception {
		String ua = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";
		String firstURl = "http://58.213.112.252:8008/jsjg/struts2BF/orderItemQueryAction";
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("searchProjectName", "85");
		paramsMap.put("searchRegionName", "");
		paramsMap.put("searchStatus", "");
		paramsMap.put("searchKeyWordName", "");
		paramsMap.put("searchStartDate", "2018-06-01");
		paramsMap.put("searchEndDate", "2019-10-10");
		paramsMap.put("selectId", "");
		paramsMap.put("s.sort_f", "");
		paramsMap.put("s.sort_m", "asc");
		paramsMap.put("p.page", "1");
		paramsMap.put("p.pageSize", "1000");
		paramsMap.put("struts.token.name", "stoken");
		paramsMap.put("stoken", "W3Y9UX32DBCET1KNA40YLH2TC6AA4PC");
		Document doc = Jsoup.connect(firstURl)
			 .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
			 .header("Accept-Encoding", "gzip, deflate")
			 .header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,zh-TW;q=0.6")
			 .header("Cache-Control", "no-cache")
			 .header("Connection", "keep-alive")
			 .header("Content-Type", "application/x-www-form-urlencoded")
			 .header("Cookie", "JSESSIONID=76DFD6167646A312D725B89A644491E5;SESSION_COOKIE=webypwsjg01")
			 .header("User-Agent", ua)
			 .data(paramsMap)
			 .post();
		int size = doc.select("tr").size();
		System.out.println(size);
		
		/*
		 * String area = doc.selectFirst("tr.tr_odd").child(2).text();
		 * System.out.println(area);
		 */	
		}
		
	
	
	
	
}
