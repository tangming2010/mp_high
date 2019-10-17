package com.goldensoft.mphigh.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class ProjectStatis {


	@TableId(type = IdType.AUTO)
	private Long id;
	
	
	private String searchProjectName;
	
	
	private String recordCount;
	
	private LocalDateTime createTime;

	public ProjectStatis() {
		super();
	}

	public ProjectStatis(String searchProjectName, String recordCount, LocalDateTime createTime) {
		super();
		this.searchProjectName = searchProjectName;
		this.recordCount = recordCount;
		this.createTime = createTime;
	}
	
	
	
	
	
}
