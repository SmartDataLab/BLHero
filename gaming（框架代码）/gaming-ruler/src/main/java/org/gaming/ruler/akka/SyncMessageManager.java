/**
 * 
 */
package org.gaming.ruler.akka;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author YY
 * 同步消息管理器
 */
public class SyncMessageManager {

	private static Logger logger = LoggerFactory.getLogger(SyncMessageManager.class);
	
	private final AtomicLong syncIdGen = new AtomicLong();
	
	public ConcurrentMap<Long, SyncFuture> futureMap = new ConcurrentHashMap<>();
	
	public ConcurrentMap<Long, Object> resultMap = new ConcurrentHashMap<>();
	
	/**
	 * 注册同步任务
	 * @param clazz
	 * @param timeout
	 * @return
	 */
	public <T> SyncFuture register(Class<T> clazz, long timeout) {
		long syncId = syncIdGen.incrementAndGet();
		logger.info("注册同步消息任务{}，{}", syncId, clazz.getSimpleName());
		SyncFuture future = new SyncFuture(syncId, () -> {
			long time1 = System.currentTimeMillis();
			
			Object result = null;
			long endTime = System.currentTimeMillis() + timeout;
			while(System.currentTimeMillis() < endTime && result == null) {
				result = resultMap.remove(syncId);
			}
			
			long time2 = System.currentTimeMillis();
			logger.info("同步消息任务{}，{}，耗时{}", syncId, clazz.getSimpleName(), (time2 - time1));
			return result;
		});
		futureMap.put(future.syncId, future);
		return future;
	}
	
	/**
	 * 添加同步任务结果
	 * @param syncId
	 * @param result
	 */
	public void result(long syncId, Object result) {
		if(futureMap.containsKey(syncId)) {
			logger.info("添加同步消息任务返回结果{}，{}", syncId, result.getClass().getSimpleName());
			resultMap.put(syncId, result);
		}
	}
	
	/**
	 * @author YY
	 * 同步任务Future
	 */
	public class SyncFuture {
		private final long syncId;
		private final CompletableFuture<Object> future;

		private SyncFuture(long syncId, Supplier<Object> supplier) {
			this.syncId = syncId;
			this.future = CompletableFuture.supplyAsync(supplier);
		}

		public Object get() {
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} finally {
				futureMap.remove(syncId);
			}
			return null;
		}

		public long getSyncId() {
			return syncId;
		}
	}
	
	public void info() {
		logger.info("同步任务等待数量{}", futureMap.size());
		logger.info("同步任务结果数量{}", resultMap.size());
	}
	
	public static void main(String[] args) {
		SyncMessageManager syncMessageManager = new SyncMessageManager();
		final SyncFuture future = syncMessageManager.register(SyncTestMessage.class, 5000);
		Thread t = new Thread(() -> {
			try {
				while (true) {
					Thread.sleep(1000);
					syncMessageManager.resultMap.put(future.syncId, new SyncTestMessage());
					System.out.println("put syncResultMap");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
		
		long time1 = System.currentTimeMillis();
		SyncTestMessage result = (SyncTestMessage)future.get();
		System.out.println("result " + result.hashCode());
		long time2 = System.currentTimeMillis();
		System.out.println("usetime " + (time2 - time1));
	}
	
	private static class SyncTestMessage {
		
	}
}
