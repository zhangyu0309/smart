package com.smarthome.core.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 分页工具类  
 * @author RM
 *
 */
public class PageBean {
	
	/**
	 * Mysql  开始页
	 */
	public int start;
	/**
	 * Mysql  每页数目
	 */
	private int pageSize;
	/**
	 * 总记录数目
	 */
	private int totalItems;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 主要用于构建分页查询时的开始索引号<begin>及其偏移量<end>
	 */
	private Map<String,Integer> pageMap;
	
	/**
	 * mongoDB 开始索引号  mongoDB
	 */
	private Integer skip;
	/**
	 * mongoDB 每页的限制数
	 */
	private Integer limit  ;

	/**
	 * 构造函数 初始化分页参数
	 * @param start 开始页号
	 * @param limit 每页显示的数目
	 */
	public PageBean(int start, int limit) {
		this.pageSize = Integer.valueOf(limit);
		this.start = Integer.valueOf(start);
		this.pageMap = new HashMap<String,Integer>();
		if(this.start==0||this.start==1){
			pageMap.put("start",0);
			this.skip = 0;
			this.start = 0;
		}else{
			pageMap.put("start",this.pageSize*(this.start-1));
			this.skip = this.pageSize*(this.start-1);
		}
		pageMap.put("end", this.pageSize);
		this.limit = this.pageSize;
	}
	/**
	 * 获取总页数
	 * @return
	 */
	public int getAllPageTotal(){
		int tempTotalPage = this.totalItems/this.pageSize;
		int tempTotalPageNext = this.totalItems%this.pageSize;
		if(tempTotalPageNext==0){
			return tempTotalPage;
		}else{
			return (tempTotalPage+1);
		}
	}
	
	

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = Integer.valueOf(pageSize);
	}

	
	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = Integer.valueOf(totalItems);
	}

	public Map<String, Integer> getPageMap() {
		return this.pageMap;
	}

	public void setPageMap(Map<String, Integer> pageMap) {
		this.pageMap = pageMap;
	}

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public Integer getTotalPage() {
		int tempTotalPage = this.totalItems/this.pageSize;
		int tempTotalPageNext = this.totalItems%this.pageSize;
		if(tempTotalPageNext==0){
			return tempTotalPage;
		}else{
			return (tempTotalPage+1);
		}
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}