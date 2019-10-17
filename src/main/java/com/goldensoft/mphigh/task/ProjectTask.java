package com.goldensoft.mphigh.task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldensoft.mphigh.common.Commons;
import com.goldensoft.mphigh.entity.Project;
import com.goldensoft.mphigh.service.ConstantService;
import com.goldensoft.mphigh.service.ProjectService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@JobHandler(value = "projectTask")
public class ProjectTask extends IJobHandler {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ConstantService constantService;
	
	private void initProject() throws IOException {
		String cookie = constantService.getCookie();
		Map<String, String> headers = Commons.headers(cookie);
		String queryUrl = constantService.getQueryUrl();
		Document document = Jsoup.connect(queryUrl).headers(headers)
			 .post();
		List<Project> projects = document.select("#searchProjectName option").stream().map(el -> new Project(el.text(), el.val())).collect(Collectors.toList());
		try {
			projectService.saveBatch(projects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		log.info("初始化项目表");
		projectService.cleanProject();
		initProject();
		return SUCCESS;
	};
	
}
