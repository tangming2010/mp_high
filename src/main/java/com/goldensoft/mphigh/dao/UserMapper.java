package com.goldensoft.mphigh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.goldensoft.mphigh.entity.User;

/**
 * @author tm
 *
 */
public interface UserMapper extends BaseMapper<User> {

	@SqlParser(filter = true)
	@Select("select * from `user` ${ew.customSqlSegment} and deleted = 0")
	List<User> selectCondition(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
	
	/**
	 * 删除所有
	 * @return
	 */
	int deleteAll();
	
	
	
}
