package com.goldensoft.mphigh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goldensoft.mphigh.dao.ProjectMapper;
import com.goldensoft.mphigh.entity.Project;
import com.goldensoft.mphigh.exception.MyException;

@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {

	public void cleanProject() {
		getBaseMapper().truncate();
	} 
	
	
	public Project getByValue(String value) {
		LambdaQueryWrapper<Project> lambdaQuery = Wrappers.<Project>lambdaQuery();
		Project one = getOne(lambdaQuery.eq(Project::getProjectValue, value));
		return one;
	}
	
	public Project getByName(String name) {
		LambdaQueryWrapper<Project> lambdaQuery = Wrappers.<Project>lambdaQuery();
		Project one = getOne(lambdaQuery.eq(Project::getProjectName, name));
		return one;
	}
	
	public Project getNextProject(String projectName) {
		Project project = getByName(projectName);
		if (project != null) {
			LambdaQueryWrapper<Project> lambdaQuery = Wrappers.<Project>lambdaQuery();
			List<Project> list = list(lambdaQuery.gt(Project::getId, project.getId()).orderByAsc(Project::getId));
			if (CollectionUtils.isNotEmpty(list)) {
				return list.get(0);
			} else {
				return null;
			}
		} else {
			throw new MyException("查询条件不存在");
		}
	}
	
	
	
}
