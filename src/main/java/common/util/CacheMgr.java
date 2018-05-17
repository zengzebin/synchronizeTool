package common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ssxt.entity.FileInfo;

/**
 * 缓存管理类
 * 
 * @author Administrator
 * 
 */
public class CacheMgr {

	private static Map<String, FileInfo> cacheMap = new HashMap<String, FileInfo>();
	private static Map cacheConfMap = new HashMap();

	private static CacheMgr cm = null;

	// 构造方法
	private CacheMgr() {
	}

	public static Map<String, FileInfo> getMap() {

		return cacheMap;
	}

	public static CacheMgr getInstance() {
		if (cm == null) {
			cm = new CacheMgr();
		}
		return cm;
	}

	/**
	 * 增加缓存
	 * 
	 * @param key
	 * @param value
	 * @param ccm
	 *            缓存对象
	 * @return
	 */
	public boolean addCache(String key, FileInfo value) {
//		System.out.println("开始增加缓存－－－－－－－－－－－－－");
		boolean flag = false;
		try {
			cacheMap.put(key, value);
//			System.out.println("增加缓存结束－－－－－－－－－－－－－");
//			System.out.println("now addcache==" + cacheMap.size());
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 获取缓存实体
	 */
	public FileInfo getValue(String key) {
		FileInfo ob = cacheMap.get(key);
		if (ob != null) {
			return ob;
		} else {
			return null;
		}
	}

	/**
	 * 获取缓存数据的数量
	 * 
	 * @return
	 */
	public int getSize() {
		return cacheMap.size();
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 * @return
	 */
	public boolean removeCache(Object key) {
		boolean flag = false;
		try {
			cacheMap.remove(key);
			cacheConfMap.remove(key);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

}
