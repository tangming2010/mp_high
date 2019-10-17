package com.goldensoft.mphigh.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@Configuration
public class MyBatisPlusConfig {

	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}
	
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		/*
		List<ISqlParser> sqlParserList = new ArrayList<ISqlParser>();
		TenantSqlParser tenantSqlParser = new TenantSqlParser();
		tenantSqlParser.setTenantHandler(new TenantHandler() {
			@Override
			public String getTenantIdColumn() {
				return "manager_id";
			}
			
			@Override
			public Expression getTenantId(boolean where) {
				return new LongValue(1088248166370832385L);
			}
			
			@Override
			public boolean doTableFilter(String tableName) {
				if (StringUtils.equals(tableName, "role")) {
					return true;
				}
				return false;
			}
		});
		sqlParserList.add(tenantSqlParser);
		paginationInterceptor.setSqlParserList(sqlParserList);
	    */
		paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
			@Override
			public boolean doFilter(MetaObject metaObject) {
				MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
				if (StringUtils.equals("com.goldensoft.mphigh.dao.UserMapper.selectById", ms.getId())) {
					return true;
				}
				return false;
			}
		} );
		
		
		return paginationInterceptor;
	}
	
}
