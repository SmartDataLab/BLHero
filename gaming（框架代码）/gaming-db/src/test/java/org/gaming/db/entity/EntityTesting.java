/**
 * 
 */
package org.gaming.db.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.gaming.db.mysql.database.DBConfig;
import org.gaming.db.repository.QueryOptions;
import org.gaming.db.usecase.SlimDao;

/**
 * @author YY
 *
 */
public class EntityTesting {
	
	public static void main(String[] args) {
		System.out.println("adsadasd");
		List<Class<?>> classList = new ArrayList<>();
		classList.add(DefaultOneEntity.class);
		classList.add(DefaultManyEntity.class);
		classList.add(TimeOneEntity.class);
		classList.add(TimeManyEntity.class);
		
		
		DBConfig gameDbConfig = new DBConfig();
		gameDbConfig.setId(1);
		gameDbConfig.setAlias("game");
		gameDbConfig.setIpPort("127.0.0.1:3306");
		gameDbConfig.setDbName("game1");
		gameDbConfig.setUser("root");
		gameDbConfig.setPassword("123456");
		
		
		DBConfig logDbConfig = new DBConfig();
		logDbConfig.setId(1);
		logDbConfig.setAlias("log");
		logDbConfig.setIpPort("127.0.0.1:3306");
		logDbConfig.setDbName("log1");
		logDbConfig.setUser("root");
		logDbConfig.setPassword("123456");
		
		List<DBConfig> dbConfigs = new ArrayList<>();
		dbConfigs.add(gameDbConfig);
		dbConfigs.add(logDbConfig);
		
		SlimDao.build(dbConfigs, classList);
		
		DefaultOneEntity oneEntity = new DefaultOneEntity();
//		SlimDaoManager.getDao(1, DefaultOneEntity.class).insert(oneEntity);
		oneEntity.setIntList(new ArrayList<>());
		oneEntity.getIntList().add(157);
		oneEntity.setIntMap(new HashMap<>());
		oneEntity.getIntMap().put(1, 9888l);
		oneEntity.setCreateTime(new Date());
		SlimDao.getRepository(1, DefaultOneEntity.class).insert(oneEntity);
		
		
		
		System.out.println(oneEntity);
		
		QueryOptions options = new QueryOptions();
		options.put("id", oneEntity.getId());
		DefaultOneEntity oneEntity0 = SlimDao.getDao(1, DefaultOneEntity.class).query(oneEntity.getId());//.get(options);
		System.out.println(oneEntity0 + " " + oneEntity0.getIntList() + " " + oneEntity0.getIntMap() + " " + oneEntity0.getCreateTime() + " " + oneEntity0.getNumber());
		
		
		DefaultManyEntity manyEntity = new DefaultManyEntity();
		SlimDao.getDao(1, DefaultManyEntity.class).insert(manyEntity);
		
		
		TimeOneEntity oneTimeEntity = new TimeOneEntity();
		SlimDao.getRepository(1, TimeOneEntity.class).insert(oneTimeEntity);
		
		
		oneTimeEntity.setName("AAA");
		SlimDao.getDao(1, TimeOneEntity.class).update(oneTimeEntity);
		
		TimeManyEntity manyTimeEntity = new TimeManyEntity();
		SlimDao.getRepository(1, TimeManyEntity.class).insert(manyTimeEntity);
		//TODO 以下代码会报错
//		TimeManyEntity manyTimeEntity2 = new TimeManyEntity();
//		SlimDaoManager.getRepository(1, TimeManyEntity.class).insert(manyTimeEntity2);
		
		
//		OriginDao<TimeManyEntity> originDao = SlimDaoManager.getDao(1, TimeManyEntity.class);
		
		System.out.println("asdasd");
	}
}
