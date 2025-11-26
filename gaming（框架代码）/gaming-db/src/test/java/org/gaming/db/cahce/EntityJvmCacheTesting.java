/**
 * 
 */
package org.gaming.db.cahce;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.gaming.db.orm.DefaultEntityMeta;
import org.gaming.db.repository.cache.EntityJvmCache;

/**
 * @author YY
 * EntityJvmCache的测试用例
 */
public class EntityJvmCacheTesting {

	public static void main(String[] args) {
//		testOne();
//		testMany();
//		testConcurrentOne();
		testConcurrentMany();
	}
	
	public static void testConcurrentOne() {
		DefaultEntityMeta<TestOneEntity> meta = new DefaultEntityMeta<>(TestOneEntity.class);
		EntityJvmCache<TestOneEntity> cache = new EntityJvmCache<>(meta);
		
		int count = 20;
		CountDownLatch cdl = new CountDownLatch(count);
		
		Thread[] threads = new Thread[count];
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					cdl.countDown();
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Random random = new Random(784540168146L);
					for(int j = 0; j < 20; j++) {
						int rand = Math.abs(random.nextInt());
						int id = rand % 20;
						TestOneEntity entity = new TestOneEntity();
						entity.setOwnerId(id + 1);
						cache.addCache(entity);
					}
				}
			});
			threads[i].start();
		}
	}
	
	public static void testConcurrentMany() {
		DefaultEntityMeta<TestMany2Entity> meta = new DefaultEntityMeta<>(TestMany2Entity.class);
		EntityJvmCache<TestMany2Entity> cache = new EntityJvmCache<>(meta);
		
		int count = 20;
		CountDownLatch cdl = new CountDownLatch(count);
		CountDownLatch cdl2 = new CountDownLatch(count);
		
		Thread[] threads = new Thread[count];
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					cdl.countDown();
					try {
						cdl.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Random random = new Random();
					for(int j = 0; j < 20; j++) {
						int rand1 = Math.abs(random.nextInt());
						int ownerId = rand1 % 20;
						
						int rand2 = Math.abs(random.nextInt());
						int hostId = rand2 % 2;
						
						TestMany2Entity entity = new TestMany2Entity();
						entity.setOwnerId(ownerId + 1);
						entity.setHostId(hostId + 1);
						cache.addCache(entity);
					}
					cdl2.countDown();
				}
			});
			threads[i].start();
		}
		
		try {
			System.out.println("await");
			cdl2.await();
			System.out.println(cache.printCache());
			System.out.println(cache.get(11, 1));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public static void testOne() {
		DefaultEntityMeta<TestOneEntity> meta = new DefaultEntityMeta<>(TestOneEntity.class);
		EntityJvmCache<TestOneEntity> cache = new EntityJvmCache<>(meta);
		
		for(int x = 1; x <= 3; x++) {
			TestOneEntity entity = new TestOneEntity();
			entity.setOwnerId(x);
			cache.addCache(entity);
		}

		cache.printCache();
		
		TestOneEntity temp1 = cache.get(1);
		
		System.out.println(temp1);
		
		List<TestOneEntity> list = new ArrayList<>();
		list.add(temp1);
		
		System.out.println("==================================================");
		
		cache.deleteCache(list);
		cache.printCache();
		
		System.out.println("==================================================");
		
		TestOneEntity temp3 = new TestOneEntity();
		temp3.setOwnerId(1);
		
		cache.addCache(temp3);
		cache.printCache();
	}
	
	public static void testMany() {
		DefaultEntityMeta<TestManyEntity> meta = new DefaultEntityMeta<>(TestManyEntity.class);
		EntityJvmCache<TestManyEntity> cache = new EntityJvmCache<>(meta);
		
		for(int x = 1; x <= 3; x++) {
			for(int y= 101; y <= 102; y++) {
				for(int z = 10001; z <= 10002; z++) {
					for(int r = 1; r <= 3; r++) {
						TestManyEntity entity = new TestManyEntity();
						entity.setOwnerId(x);
						entity.setHostId(y);
						entity.setType(z);
						entity.setPlace(r);
						cache.addCache(entity);
					}
				}
			}
		}

		cache.printCache();
		
		TestManyEntity temp1 = cache.get(1, 101, 10001l, 1);
		TestManyEntity temp2 = cache.get(1l, 101, 10001l, 2);
		
		List<TestManyEntity> list = new ArrayList<>();
		list.add(temp1);
		list.add(temp2);
		System.out.println(list);
		
		System.out.println("==================================================");
		
		cache.deleteCache(list);
		cache.printCache();
		
		System.out.println("==================================================");
		
		TestManyEntity temp3 = new TestManyEntity();
		temp3.setOwnerId(1);
		temp3.setHostId(101);
		temp3.setType(10001);
		temp3.setPlace(3);
		
		cache.addCache(temp3);
		cache.printCache();
	}
}
