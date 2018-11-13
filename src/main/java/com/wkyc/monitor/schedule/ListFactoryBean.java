package com.wkyc.monitor.schedule;

import org.springframework.beans.factory.FactoryBean;

import java.util.List;

/**
 * 在xml配置一个list
 * @author cxy
 *
 */
public class ListFactoryBean implements FactoryBean<List<?>> {

	private List<?> list;
	
	public void setList(List<?> list) {
		this.list = list;
	}

	@Override
	public List<?> getObject() throws Exception {
		return list;
	}

	@Override
	public Class<?> getObjectType() {
		return List.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
