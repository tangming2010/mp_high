package com.goldensoft.mphigh.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;

@Data
public class User {
	
	private Long id;
	
	private String name;
	
	private Integer age;
	
	private String email;
	
	private Long managerId;
	
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;
	
	@Version
	private Integer version;
	
	@TableLogic
	@TableField(select = false)
	private Integer deleted;
	
	
}
