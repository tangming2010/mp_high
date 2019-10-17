package com.goldensoft.mphigh.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.goldensoft.mphigh.common.Commons;
import com.goldensoft.mphigh.dao.SpiderLogMapper;
import com.goldensoft.mphigh.entity.BusData;
import com.goldensoft.mphigh.entity.Project;
import com.goldensoft.mphigh.entity.SpiderLog;
import com.goldensoft.mphigh.service.BusDataService;
import com.goldensoft.mphigh.service.ConstantService;
import com.goldensoft.mphigh.service.ProjectService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@JobHandler(value = "mainTask")
public class MainTask extends IJobHandler{
	
	@Autowired
	private ConstantService constantService;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BusDataService busDataService;
	
	@Autowired
	private SpiderLogMapper spiderLogMapper;
	
	
	private Map<String, String> data(String searchProjectValue, String searchStartDate, String searchEndDate, String page) {
		String pageSize = constantService.getPageSize();
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
		paramsMap.put("p.page", page);
		paramsMap.put("p.pageSize", pageSize);
		paramsMap.put("struts.token.name", "stoken");
		paramsMap.put("stoken", stoken);
		return paramsMap;
	}
	
	@Override
	@Transactional
	public ReturnT<String> execute(String param) throws Exception {
		String page = constantService.getPage();
		String searchProjectName = constantService.getSearchProjectName();
		log.info("开始去拉【{}】的第{}页数据", searchProjectName, page);
		String complete = constantService.get("complete");
		if (complete.equals("true")) {
			log.info("数据已经全部拉取完毕！");
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
		Map<String, String> paramsMap = data(byName.getProjectValue(), searchStartDate, searchEndDate, page);
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
		// 获取总页码
		String totalPageInfo = document.selectFirst("div.pageNav_right > ul > li").text();
		log.info("页码信息：{}", totalPageInfo);
		String totalPage = StringUtils.getDigits(StringUtils.substringAfter(totalPageInfo, "/"));
		log.info("页码：{}", totalPage);
		String newPage = String.valueOf(Integer.parseInt(page) + 1);
		if (Integer.valueOf(newPage.trim()) > Integer.valueOf(totalPage.trim())) {
			// 开始取下一个查询条件，并初始化page=1
			Project nextProject = projectService.getNextProject(searchProjectName);
			constantService.setPage("1");
			if (nextProject == null) {
				log.info("完成拉去数据！！！！！！");
				constantService.set("complete", "true");
			} else {
				constantService.setSearchProjectName(nextProject.getProjectName());
			}
		} else {
			// 存储newPage
			constantService.setPage(newPage);
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
		List<BusData> datas = document.select("#frm tr").stream().skip(1).map(el -> 
			new BusData(
					el.child(1).text(), 
					el.child(2).text(), 
					el.child(3).text(), 
					el.child(4).text(), 
					el.child(5).text(), 
					el.child(6).text(), 
					el.child(7).text(), 
					el.child(8).text(), 
					el.child(9).text(), 
					el.child(10).text(), 
					el.child(11).text(), 
					el.child(12).text(), 
					el.child(13).text(), 
					el.child(14).text(),
					el.child(15).text(), 
					el.child(16).text(), 
					el.child(17).text(), 
					el.child(18).text(), 
					el.child(19).text(), 
					searchProjectName, 
					searchStartDate, 
					searchEndDate)
		).collect(Collectors.toList());
		spirderLog.setSize(datas.size());
		spiderLogMapper.insert(spirderLog);
		busDataService.saveBatch(datas);
		log.info("当次采集的数据：{}条", datas.size());
		return SUCCESS;
	}

}
