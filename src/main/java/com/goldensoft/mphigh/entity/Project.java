package com.goldensoft.mphigh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Project {
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String projectName;
	
	
	private String projectValue;


	public Project() {
		super();
	}


	public Project(String projectName, String projectValue) {
		super();
		this.projectName = projectName;
		this.projectValue = projectValue;
	}
	
	
	
	
}
