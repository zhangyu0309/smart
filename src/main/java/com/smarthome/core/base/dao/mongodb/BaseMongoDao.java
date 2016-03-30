package com.smarthome.core.base.dao.mongodb;

import java.io.Serializable;
import java.util.List;


/**
 * <p>Title: MongoDB  操作对象接口</p>
 * <p>Description:</p>
 * <p>Company:R & D Center Of INspur DIgital Media</p>
 * @author ruiming(2014-4-22 下午1:05:12)
 *<p>Editor:()</p>
 * @version:1.1
 **/

public interface BaseMongoDao<T,PK extends Serializable> {
	
	/**
	 * 根据实体对象条件获取符合条件的对象总数
	 * @return
	 */
	public int countByEntity();
	/**
	 * 单个添加对象
	 * @param entity  实体对象
	 * @return
	 */
	public boolean add(T entity);
	/**
	 * 批量添加实体对象
	 * @param entityList 对象队列
	 * @return
	 */
	public boolean addByList(List<T> entityList);
	/**
	 * 根据主键删除对象
	 * @param id  主键值
	 * @return
	 */
	public boolean deleteById();
	/**
	 * 根据实体对象条件删除
	 * @param entity  实体对象条件
	 * @return
	 */
	public boolean deleteByParam();
	/**
	 * 删除所有
	 * @return
	 */
	public boolean deleteAll();
	/**
	 * 根据对象Id号更新实体对象信息
	 * @param entity  实体对象信息
	 * @return
	 */
	public boolean updateById();
	/**
	 * 根据实体对象条件更新实体对象
	 * @param entity 实体对象条件
	 * @return
	 */
	public boolean update();
	/**
	 * 根据实体对象ID号查找对象
	 * @param id ID号
	 * @return
	 */
	public T getById(PK id);
	/**
	 * 获取全部的对象
	 * @return
	 */
	public List<T> getAll();
	/**
	 * 按条件分页查找
	 * @param entity 条件实体对象
	 * @param skip   开始索引
	 * @param limit  限制条件
	 * @return
	 */
	public List<T> getByEntity();
	/**
	 * 根据条件查询出来，再根据值进行修改
	 * @param entity
	 * @param entityValue
	 * @return
	 */
	public T getAndModify();
	/**
	 * 根据条件查询出来并且删除
	 * @param entity
	 * @return
	 */
	public T getAndRemove();
	

}
