package com.goldensoft.mphigh.component;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		if (metaObject.hasSetter("createTime")) {
			setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Object val = getFieldValByName("updateTime", metaObject);
		if (metaObject.hasSetter("updateTime") && val == null) {
			setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		}
	}

}
