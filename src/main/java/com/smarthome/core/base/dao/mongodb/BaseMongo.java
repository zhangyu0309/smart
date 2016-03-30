package com.smarthome.core.base.dao.mongodb;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


/**
 * <p>Title: MongoDB 操作抽象类</p>
 * <p>Description:</p>
 * <p>Company:R & D Center Of INspur DIgital Media</p>
 * @author ruiming(2014-4-22 下午12:57:46)
 *<p>Editor:()</p>
 * @version:1.1
 **/
public abstract  class BaseMongo<T,PK extends Serializable >{
 
	
	@Autowired
	public MongoTemplate mongoTemplate;
	/**
	 * 查询条件语句
	 */
	public Query query;
	/**
	 * 更新条件语句
	 */
	public Update update;
	/**
	 * 获取实体映射类名称
	 */
	@SuppressWarnings("unchecked")
	private Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	
	public Query getQuery() {
		return query;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	public Update getUpdate() {
		return update;
	}
	public void setUpdate(Update update) {
		this.update = update;
	}
	/**
	 * 判断MongoDB是否是在运行
	 * @return
	 */
	public boolean isMongoRunnig(){
		
		try {
			this.mongoTemplate.getCollectionNames();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public int countByEntity() {
		// TODO Auto-generated method stub
		try {
			return (int) this.mongoTemplate.count(this.query, entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public boolean add(T entity) {
		// TODO Auto-generated method stub
		try {
//			System.out.println("Dao核心添加操作Save-----3");
			this.mongoTemplate.save(entity);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean addByList(List<T> entityList) {
		// TODO Auto-generated method stub
		try {
			this.mongoTemplate.insert(entityList);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteById() {
		// TODO Auto-generated method stub
		try {
			this.mongoTemplate.remove(this.query, entityClass);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteByParam() {
		try {
			this.mongoTemplate.remove(this.query, entityClass);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		try {
			this.mongoTemplate.dropCollection(entityClass);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean updateById() {
		try {
			this.mongoTemplate.updateFirst(this.query, this.update, entityClass);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public boolean update() {
		// TODO Auto-generated method stub
		try {
			this.mongoTemplate.updateMulti(this.query, this.update, entityClass);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public T getById(PK id) {
		// TODO Auto-generated method stub
		 try {
			return mongoTemplate.findById(id, entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public List<T> getAll() {
		// TODO Auto-generated method stub
		try {
			return this.mongoTemplate.findAll(entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public List<T> getByEntity() {
		// TODO Auto-generated method stub
		try {
			return this.mongoTemplate.find(this.query, entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public T getAndModify() {
		// TODO Auto-generated method stub
		try {
			return this.mongoTemplate.findAndModify(this.getQuery(),this.getUpdate(), entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public T getAndRemove() {
		// TODO Auto-generated method stub
		try {
			return this.mongoTemplate.findAndRemove(this.getQuery(), entityClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	


}
