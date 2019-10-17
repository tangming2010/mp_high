package com.goldensoft.mphigh.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class SpiderLog {

	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String header;
	
	
	private String queryParams;
	
	private Integer size;
	
	private LocalDateTime createTime;
	
}
