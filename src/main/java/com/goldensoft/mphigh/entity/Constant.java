package com.goldensoft.mphigh.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Constant {
	
	@TableId
	private String dataKey;
	
	private String dataValue;
	
}
