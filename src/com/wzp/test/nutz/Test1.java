package com.wzp.test.nutz;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.wzp.test.bean.Person;

public class Test1 {

	public static void main(String[] args) {
		Log log = Logs.get();
		log.info("abd");
		BasicDataSource d = new BasicDataSource();
		d.setDriverClassName("com.mysql.jdbc.Driver");
		d.setUsername("root");
//		d.setPassword("111111");
		d.setUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8");
		d.setValidationQuery("SELECT 1");
		final int devRatio = 50;
		d.setInitialSize(50 / devRatio); // waiting after initialized
		d.setMinIdle(10 / devRatio); // always keep 10
		d.setMaxIdle(100 / devRatio); // max waiting, control return to waiting
										// or close
		d.setMaxActive(350 / devRatio); // max using (also max holding)
		d.setMaxWait(10 * 1000); // if maxActive reach, wait 10s
		d.setTestWhileIdle(true); // when adjusting pool, close if idle 30m, and
									// also is broken
		d.setTestOnBorrow(false); // to avoid too much SELECT 1
		d.setNumTestsPerEvictionRun(3); // when adjusting pool, check 3
										// connections per run, so max close or
										// create 3
		d.setMinEvictableIdleTimeMillis(30 * 60 * 1000); // when adjusting pool
															// size, idle 30m
															// will be closed
		d.setTimeBetweenEvictionRunsMillis(10 * 1000); // adjust pool size every
														// 10s
		d.setLogAbandoned(true); // to check if app has connection leak, if yes,
									// log it
		d.setRemoveAbandoned(true); // to check if app has connection leak, if
									// yes, force to return it
		d.setRemoveAbandonedTimeout(300); // to check if app has connection
											// leak, 300s no use and no return
											// is leak
		try {
			d.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long ts  = System.nanoTime();
		Dao dao = new NutDao(d);
		Person p = new Person();
		p.setAge(20);
		p.setName("ED5");
		dao.insert(p);
		System.out.println(System.nanoTime() - ts);
		ts = System.nanoTime();
		
		System.out.println(p.getId());
		
		Record rc = new Record();
	}
	
	

}
