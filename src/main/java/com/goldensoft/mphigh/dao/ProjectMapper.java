package com.goldensoft.mphigh.dao;

import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goldensoft.mphigh.entity.Project;

public interface ProjectMapper extends BaseMapper<Project> {

	@Update("truncate table project")
	void truncate();
	
}
