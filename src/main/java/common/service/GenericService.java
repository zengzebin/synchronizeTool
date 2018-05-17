package common.service;

 

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import common.page.PageControl;
import common.page.SqlCondGroup;

 
/**
 * @ClassName：GenericService
 * @Description：TODO(GenericService Service层泛型接口，定义基本的Service功能)
 * @param <T>
 *            实体类
 * @param <PK>
 *            主键类，必须实现Serializable接口
 * @author zhengxican
 * @date 2015-08-25
 * @version V1.0
 */
@Transactional(readOnly = false, rollbackFor = Exception.class)
public interface GenericService<T extends Serializable, PK extends Serializable> {

	/**
	 * 按主键取记录
	 * 
	 * @param id
	 *            主键值
	 * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
	 */
	public T get(PK id);

	/**
	 * 按主键取记录
	 * 
	 * @param id
	 *            主键值
	 * @return 记录实体对象，如果没有符合主键条件的记录，则 throw DataAccessException
	 */
	public T load(PK id);

	/**
	 * 获取全部实体
	 * 
	 * @return 返回一个list集合数据
	 */
	public List<T> loadAll();
	/**
	 * 根据实例查询符合条件的记录。
	 * @param entity
	 * @return
	 */
	public List<T> queryByExample(T entity);
	/**
	 * 根据实例查询第一个符合条件的记录。
	 * @param entity
	 * @return
	 */
	public T getFirstByExample(T entity);

	/**
	 * 插入一个实体（在数据库INSERT一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 */
	public PK save(T entity);

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 修改一个实体对象（UPDATE一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void update(T entity);

	/**
	 * 删除指定的实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void delete(T entity);
	
	/**
	 * 根据主键删除记录
	 * @param id
	 */
	public void deleteByKey(PK id);
	
	/**
	 * 删除多个实体
	 * @param entities
	 */
	public void deleteAll(Collection<T> entities);
	
	
	
	//ypx

	/**
	 * 
	 * findByCondition(这里用一句话描述这个方法的作用)<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (这里描述这个方法的注意事项 – 可选)<br/>
	 * @param pageControl
	 * @param conList
	 * @return 
	 *List
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public List findByCondition(PageControl pageControl,List<SqlCondGroup> conList);
	
	/**
	 * 
	 * sql返回按SqlCondGroup条件查询的结果列表<br/>
	 * (这里描述这个方法适用条件 – 可选)<br/>
	 * (这里描述这个方法的执行流程 – 可选)<br/>
	 * (这里描述这个方法的使用方法 – 可选)<br/>
	 * (这里描述这个方法的注意事项 – 可选)<br/>
	 * @param pageControl
	 * @param conList
	 * @param preWhereSql where之前的语句,如"select * from abc"
	 * @return 
	 *List
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public List findByNativeCondition(PageControl pageControl,
			List<SqlCondGroup> conList,String preWhereSql,Class returnType);
	
	/**
	 * 更新或者保存数据集
	 * @param entities
	 */
	public void saveOrUpdateAll(Collection<T> entities);

	
	/**
	 * 
	 * 根据sql条件更新数据
	 * @param updateSql	sql
	 * @param conList	条件列表
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int updateByNativeCondition(String updateSql, List<SqlCondGroup> conList);
	/**
	 * 根据条件更新数据
	 * (注意事项):执行完，需要手动清空有关缓存<br/>
	 * @param updateSql	hql
	 * @param conList	条件列表
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int updateByCondition(String updateSql, List<SqlCondGroup> conList);
	/**
	 * 
	 * 根据条件更新数据
	 * 需要手动清空有关缓存
	 * (注意事项):执行完，需要手动清空有关缓存<br/>
	 * @param setList	设置值列表
	 * @param conList	条件列表
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int updateByCondition(List<SqlCondGroup> setList ,List<SqlCondGroup> conList);
	
}
