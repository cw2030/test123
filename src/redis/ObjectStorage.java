/*package redis;

import hbec.platform.commons.exceptions.HbecJedisException;
import hbec.platform.commons.services.IObjectStorage;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class ObjectStorage implements IObjectStorage {
	private JedisPool jedisPool;
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final Gson gson = new GsonBuilder().setDateFormat(
			DATE_FORMAT).create();

	ObjectStorage(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public void create(String category, String key, Object value)
			throws HbecJedisException {
		Jedis jedis = jedisPool.getResource();
		try {
			if (value == null) {
				throw new HbecJedisException(HbecJedisException.JEDIS_NULLPOINT);
			} else {
				String globalKey = category + "/" + key;
				if (value instanceof List) {
					for (Object item : ((List<?>) value)) {
						String valueStr = "";
						if (item != null && item instanceof String) {
							valueStr = item.toString();
						} else {
							valueStr = gson.toJson(item);
						}
						jedis.lpush(globalKey, valueStr);
					}
				} else if (value instanceof Map) {
					Iterator<?> items = ((Map<?, ?>) value).entrySet()
							.iterator();
					while (items.hasNext()) {
						Entry<?, ?> item = (Entry<?, ?>) items.next();
						String field = item.getKey().toString();
						Object itemValue = item.getValue();
						String valueStr = "";
						if (item != null && itemValue instanceof String) {
							valueStr = itemValue.toString();
						} else {
							valueStr = gson.toJson(itemValue);
						}
						jedis.hset(globalKey, field, valueStr);
					}
				} else if (value instanceof String) {
					String valueStr = value.toString();
					jedis.lpush(globalKey, valueStr);
				} else {
					String valueStr = gson.toJson(value);
					jedis.lpush(globalKey, valueStr);
				}
			}
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public <T> T getObject(String category, String key, Class<T> returnCls) {
		Jedis jedis = jedisPool.getResource();
		try {
			String globalKey = category + "/" + key;
			List<String> tmpList = jedis.lrange(globalKey, 0, 0);
			if (tmpList.size() > 0) {
				String tmpStr = tmpList.get(0);
				if (String.class.isAssignableFrom(returnCls)) {
					return returnCls.cast(tmpStr);
				} else {
					return gson.fromJson(tmpStr, returnCls);
				}
			} else {
				return null;
			}
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public <T> List<T> getList(String category, String key, Class<T> returnCls) {
		List<T> lt = new ArrayList<T>();
		Jedis jedis = jedisPool.getResource();
		try {
			String globalKey = category + "/" + key;
			List<String> tmpList = jedis.lrange(globalKey, 0, -1);
			for (int i = tmpList.size() - 1; i >= 0; i--) {
				String tmpStr = tmpList.get(i);
				T t;
				if (String.class.isAssignableFrom(returnCls)) {
					t = returnCls.cast(tmpStr);
				} else {
					t = gson.fromJson(tmpStr, returnCls);
				}
				lt.add(t);
			}
			return lt;
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public <T> Map<String, T> getMap(String category, String key,
			Class<T> returnCls) {
		Map<String, T> lt = new HashMap<String, T>();
		Jedis jedis = jedisPool.getResource();
		try {
			String globalKey = category + "/" + key;
			Map<String, String> tmpMap = jedis.hgetAll(globalKey);
			Iterator<Entry<String, String>> tmpIter = tmpMap.entrySet()
					.iterator();
			while (tmpIter.hasNext()) {
				Entry<String, String> tmpEntry = tmpIter.next();
				T t;
				if (String.class.isAssignableFrom(returnCls)) {
					t = returnCls.cast(tmpEntry.getValue());
				} else {
					t = gson.fromJson(tmpEntry.getValue(), returnCls);
				}
				lt.put(tmpEntry.getKey(), t);
			}
			return lt;
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public void delete(String category, String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(category + "/" + key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long zadd(String key, double score, String member) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zadd(key, score, member);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public double zincrby(String key, double score, String member) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zincrby(key, score, member);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.exists(key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrevrange(key, start, end);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long zcard(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zcard(key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long del(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.del(key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long zrem(String key, String... members) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrem(key, members);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public double zscore(String key, String member) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zscore(key, member);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long lrem(String category, String key, String value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.lrem(key, 0, value);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hmset(key, hash);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public List<String> hmget(String key, String... fields) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hmget(key, fields);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	*//**
	 * 将类型为Map<String, List<T>>的数据存入缓存服务器中
	 * 
	 * @param key
	 *            String 唯一key
	 * @param map
	 *            Map<String, List<T>> 集合数据
	 * @return
	 *//*
	@Override
	public <T> String setMap(String key, Map<String, List<T>> map) {
		Jedis jedis = jedisPool.getResource();
		try {
			Map<String, String> inMap = null;
			// 将map集合中的value转换成Json格式存入
			if (MapUtils.isNotEmpty(map)) {
				for (String keyString : map.keySet()) {
					List<?> list = map.get(keyString);
					String listJson = gson.toJson(list);
					if (inMap == null) {
						inMap = new HashMap<String, String>();
					}
					inMap.put(keyString, listJson);
				}
			}
			return jedis.hmset(key, inMap);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	*//**
	 * 根据Redis的key和map的key获取map集合中对应key的value集合
	 * 
	 * @param key
	 *            Redis的key
	 * @param mapKey
	 *            map的key
	 * @param type
	 *            返回list集合中的对象类型 如：new TypeToken<ArrayList<User>>(){}.getType()
	 * @return
	 *//*
	@Override
	public <T> List<T> getListByMapKey(String key, String mapKey, Type type) {
		Jedis jedis = jedisPool.getResource();
		List<T> resultList = null;
		try {
			List<String> list = jedis.hmget(key, mapKey);
			if (CollectionUtils.isNotEmpty(list)) {
				for (String string : list) {
					if (resultList == null) {
						resultList = new ArrayList<>();
					}
					resultList = gson.fromJson(string, type);
				}
			}
			return resultList;
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long hdel(String key, String... fields) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hdel(key, fields);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public long hlen(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hlen(key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public Set<String> hkeys(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.hkeys(key);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public Long addSortedSet(String key, Set<String> set) {
		Long successCnt = 0l;
		if (set != null && set.size() > 0) {
			Jedis jedis = jedisPool.getResource();
			try {
				Iterator<String> it = set.iterator();
				while (it.hasNext()) {
					String member = it.next();
					if (jedis.zscore(key, member) == null) {
						successCnt += jedis.zadd(key, jedis.zcard(key) + 1,
								member);
					}
					successCnt++;
				}
			} catch (Throwable t) {
				jedisPool.returnBrokenResource(jedis);
				throw t;
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
		return successCnt;
	}

	@Override
	public Set<String> getSortedSet(String key) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.zrange(key, 0, -1);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public Page<String> generatePage(String key) {
		return generatePage(key, Page.DEFAULT_PAGE_SIZE, String.class);
	}

	@Override
	public Page<String> generatePage(String key, int pageSize) {
		return generatePage(key, pageSize, String.class);
	}

	@Override
	public <T> Page<T> generatePage(String key, Class<T> classType) {
		return generatePage(key, Page.DEFAULT_PAGE_SIZE, classType);
	}

	@Override
	public <T> Page<T> generatePage(String key, int pageSize, Class<T> classType) {
		return new PageLImpl<T>(key, pageSize, classType, new PageCallable() {
			@Override
			public Collection<String> doJedis(Jedis jedis, String key,
					long start, long stop) {
				return jedis.lrange(key, start, stop);
			}

			@Override
			public Long getLength(Jedis jedis, String key) {
				return jedis.llen(key);
			}
		});
	}

	@Override
	public PageSortedSet generatePageSet(String key) {
		return generatePageSet(key, Page.DEFAULT_PAGE_SIZE);
	}

	@Override
	public PageSortedSet generatePageSet(String key, int pageSize) {
		return new PageSSImpl(key, pageSize, new PageCallable() {
			@Override
			public Collection<String> doJedis(Jedis jedis, String key,
					long start, long stop) {
				return jedis.zrange(key, start, stop);
			}

			@Override
			public Long getLength(Jedis jedis, String key) {
				return jedis.zcard(key);
			}
		});
	}

	interface PageCallable {
		Long getLength(Jedis jedis, String key);

		Collection<String> doJedis(Jedis jedis, String key, long start,
				long stop);
	}

	abstract class PageImpl<T> implements PageBase<T> {
		protected String key;
		protected int pageSize;
		protected long total;
		protected Class<T> classType;
		protected long totalPages;
		private PageCallable call;

		public PageImpl(String key, int pageSize, Class<T> classType,
				PageCallable call) {
			this.key = key;
			this.pageSize = pageSize;
			this.classType = classType;
			this.call = call;

			Jedis jedis = jedisPool.getResource();
			try {
				total = call.getLength(jedis, key);
				totalPages = total / pageSize;
				if (total % pageSize != 0) {
					totalPages++;
				}
			} catch (Throwable t) {
				jedisPool.returnBrokenResource(jedis);
				throw t;
			} finally {
				jedisPool.returnResource(jedis);
			}
		}

		@Override
		public int getPageSize() {
			return this.pageSize;
		}

		@Override
		public long getTotal() {
			return this.total;
		}

		@Override
		public long getTotalPages() {
			return this.totalPages;
		}

		protected Collection<String> getString(int page) {
			Jedis jedis = jedisPool.getResource();
			Collection<String> ret = new ArrayList<String>();
			try {
				// get page info
				// total = call.getLength(jedis, key);
				// totalPages = total / pageSize;
				// if (total % pageSize != 0) {
				// totalPages++;
				// }
				// check page info
				if (page < 1) {
					page = 1;
				}
				if (page <= totalPages) {
					long start = (page - 1) * pageSize;
					long stop = start + pageSize - 1;
					ret = call.doJedis(jedis, key, start, stop);
				}
				return ret;
			} catch (Throwable t) {
				jedisPool.returnBrokenResource(jedis);
				throw t;
			} finally {
				jedisPool.returnResource(jedis);
			}
		}
	}

	class PageLImpl<T> extends PageImpl<T> implements Page<T> {
		public PageLImpl(String key, int pageSize, Class<T> classType,
				PageCallable call) {
			super(key, pageSize, classType, call);
		}

		@Override
		public List<T> get(int page) {
			List<String> col = (List<String>) getString(page);
			List<T> lt = new ArrayList<T>();
			for (int i = col.size() - 1; i >= 0; i--) {
				String tmpStr = col.get(i);
				T t;
				if (String.class.isAssignableFrom(classType)) {
					t = classType.cast(tmpStr);
				} else {
					t = gson.fromJson(tmpStr, classType);
				}
				lt.add(t);
			}
			return lt;
		}
	}

	class PageSSImpl extends PageImpl<String> implements PageSortedSet {
		public PageSSImpl(String key, int pageSize, PageCallable call) {
			super(key, pageSize, String.class, call);
		}

		@Override
		public Set<String> get(int page) {
			return (Set<String>) getString(page);
		}
	}

	@Override
	public String setList(String key, List<?> value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.set(key, gson.toJson(value));
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public <T> List<T> getList(String key, final Class<T> clazz) {
		List<T> result = null;
		Jedis jedis = jedisPool.getResource();
		try {
			String strResult = jedis.get(key);
			if (strResult != null) {
				result = gson.fromJson(strResult, new ParameterizedType() {
					public Type getRawType() {
						return List.class;
					}

					public Type[] getActualTypeArguments() {
						Type[] type = new Type[1];
						type[0] = clazz;
						return type;
					}

					public Type getOwnerType() {
						return null;
					}
				});
			}
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@Override
	public String setObj(String key, Object value) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.set(key, gson.toJson(value));
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public <T> T getObj(String key, final Class<T> clazz) {
		T result = null;
		Jedis jedis = jedisPool.getResource();
		try {
			String strResult = jedis.get(key);
			if (strResult != null) {
				result = gson.fromJson(strResult, clazz);
			}
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	@Override
	public void hsetPipeline(String category,
			Map<String, Map<String, String>> map) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Pipeline p = jedis.pipelined();
			Iterator<Map.Entry<String, Map<String, String>>> datas = map
					.entrySet().iterator();
			while (datas.hasNext()) {
				Map.Entry<String, Map<String, String>> entry = datas.next();
				p.hset(category + "/" + entry.getValue().get("keys"),
						entry.getKey(), entry.getValue().get("value"));
			}
			p.sync();
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public Map<String, Map<String, String>> hgettPipeline(String... keys) {
		Jedis jedis = null;
		Map<String, Map<String, String>> returnMaps = null;
		Map<String, Response<Map<String, String>>> respMaps = null;
		try {
			jedis = jedisPool.getResource();
			Pipeline p = jedis.pipelined();
			respMaps = new HashMap<String, Response<Map<String, String>>>();
			for (String key : keys) {
				respMaps.put(key, p.hgetAll(key));
			}
			p.sync();
			returnMaps = new HashMap<String, Map<String, String>>();
			for (String key : respMaps.keySet()) {
				returnMaps.put(key, respMaps.get(key).get());
			}
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
		return returnMaps;
	}

	@Override
	public long incrBy(String key, long integer) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.incrBy(key, integer);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public String watch(String... keys) {
		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.watch(keys);
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	@Override
	public List<Object> multiExec(JedisTransationCall call) {
		List<Object> result = null;
		Jedis jedis = jedisPool.getResource();
		try {
			Transaction transaction = jedis.multi();
			call.execute(new HbJedisTransactionImpl(transaction));
			result = transaction.exec();
		} catch (Throwable t) {
			jedisPool.returnBrokenResource(jedis);
			throw t;
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	class HbJedisTransactionImpl implements HbJedisTransaction {
		private Transaction transaction;
		public HbJedisTransactionImpl(Transaction transaction) {
			this.transaction = transaction;
		}

		@Override
		public void setObj(String key, Object value) {
			transaction.set(key, gson.toJson(value));
		}
	}
}
*/