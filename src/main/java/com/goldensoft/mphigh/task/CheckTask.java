package com.goldensoft.mphigh.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.goldensoft.mphigh.common.Commons;
import com.goldensoft.mphigh.dao.SpiderLogMapper;
import com.goldensoft.mphigh.entity.Project;
import com.goldensoft.mphigh.entity.ProjectStatis;
import com.goldensoft.mphigh.entity.SpiderLog;
import com.goldensoft.mphigh.service.ConstantService;
import com.goldensoft.mphigh.service.ProjectService;
import com.goldensoft.mphigh.service.ProjectStatisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@JobHandler(value = "checkTask")
public class CheckTask extends IJobHandler {
	
	@Autowired
	private ProjectStatisService projectStatisService;
	
	@Autowired
	private ConstantService constantService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private SpiderLogMapper spiderLogMapper;
	
	
	private Map<String, String> data(String searchProjectValue, String searchStartDate, String searchEndDate) {
		String stoken = constantService.getStoken();
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("searchProjectName", searchProjectValue);
		paramsMap.put("searchRegionName", "");
		paramsMap.put("searchStatus", "");
		paramsMap.put("searchKeyWordName", "");
		paramsMap.put("searchStartDate", searchStartDate);
		paramsMap.put("searchEndDate", searchEndDate);
		paramsMap.put("selectId", "");
		paramsMap.put("s.sort_f", "");
		paramsMap.put("s.sort_m", "asc");
		paramsMap.put("p.page", "1");
		paramsMap.put("p.pageSize", "10");
		paramsMap.put("struts.token.name", "stoken");
		paramsMap.put("stoken", stoken);
		return paramsMap;
	}
	
	
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		String searchProjectName = constantService.getSearchProjectName();
		log.info("开始获取项目【{}】的记录数量", searchProjectName);
		String complete = constantService.get("complete");
		if (complete.equals("true")) {
			log.info("数据已经全部统计完毕！");
			return SUCCESS;
		}
		// 1. 获取查询条件
		String cookie = constantService.getCookie();
		String queryUrl = constantService.getQueryUrl();
		// 2. 构建查询头
		Map<String, String> headers = Commons.headers(cookie);
		// 3. 构建查询参数
		String searchStartDate = constantService.getSearchStartDate();
		String searchEndDate = constantService.getSearchEndDate();
		Project byName = projectService.getByName(searchProjectName);
		Map<String, String> paramsMap = data(byName.getProjectValue(), searchStartDate, searchEndDate);
		// 4. 构建查询日志
		SpiderLog spirderLog = new SpiderLog();
		spirderLog.setCreateTime(LocalDateTime.now());
		spirderLog.setHeader(JSON.toJSONString(headers));
		spirderLog.setQueryParams(JSON.toJSONString(paramsMap));
		// 5. 发起查询请求
		Document document = null;
		try {
			Response response = Jsoup.connect(queryUrl)
				 .headers(headers)
				 .data(paramsMap)
				 .method(Method.POST)
				 .timeout(600 * 1000)
				 .maxBodySize(0)
				 .execute();
			if (response.statusCode() == 200) {
				String body = response.body();
				log.info("获取的body大小是：{}", body.length());
				document = Jsoup.parse(body);
			} else {
				log.error("当前爬数据失败返回码：{}", response.statusCode() );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 6. 更新参数
		Project nextProject = projectService.getNextProject(searchProjectName);
		constantService.setPage("1");
		if (nextProject == null) {
			log.info("完成拉去数据！！！！！！");
			constantService.set("complete", "true");
		} else {
			constantService.setSearchProjectName(nextProject.getProjectName());
		}
		// 更新数据
		String strutsTokenName = document.select("input[name='struts.token.name']").val();
		String stoken = document.select("input[name='stoken']").val();
		if (StringUtils.isNotBlank(strutsTokenName)) {
			constantService.set("struts.token.name", strutsTokenName);
		}
		if (StringUtils.isNoneBlank(stoken)) {
			constantService.set("stoken", stoken);
		}
		// 7. 保存数据
		String totalSizeInfo = document.select("div.pageNav_right > ul > li").get(2).text();
		String totalSize = StringUtils.getDigits(totalSizeInfo);
		spirderLog.setSize(1);
		spiderLogMapper.insert(spirderLog);
		ProjectStatis statis = new ProjectStatis(searchProjectName, totalSize, LocalDateTime.now());
		projectStatisService.save(statis);
		log.info("当次采集的项目【{}】的记录数量：{}条", searchProjectName, totalSize);
		return SUCCESS;
	}

}
