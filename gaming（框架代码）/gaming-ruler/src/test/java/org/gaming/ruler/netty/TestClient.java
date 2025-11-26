/**
 * 
 */
package org.gaming.ruler.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.gaming.ruler.netty.datagram.ByteDatagramChannelInitializer;

/**
 * @author YY
 *
 */
public class TestClient {
	
	static ConcurrentMap<Integer, NettySocketClient> clientMap = new ConcurrentHashMap<>();
	
	static AtomicInteger ID = new AtomicInteger();
	
	private static ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(8);
	
	/*
	 * windows环境下需要设置tcp动态端口数量，以管理员身份运行cmd，并执行命令netsh int ipv4 set dynamicport tcp start=32768 num=32767
	 * 设置参数后，通过命令netsh int ipv4 show dynamicport tcp来确认对应参数
	 */
	
	public static void main(String[] args) {
		ByteDatagramChannelInitializer channelInitializer = new ByteDatagramChannelInitializer(new NettyHandler());
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 8000; i++) {
					int portNum = TestConfig.endPort - TestConfig.startPort;
					if(portNum == 0) {
						portNum = 1;
					}
					int portOffset = i % portNum;
					NettySocketClient client = new NettySocketClient("192.168.1.42", TestConfig.startPort + portOffset, channelInitializer);
					clientMap.put(ID.incrementAndGet(), client);
					client.syncConnect();
				}
			}
		};
		
		Thread t1 = new Thread(runnable);
		Thread t2 = new Thread(runnable);
		Thread t3 = new Thread(runnable);
		Thread t4 = new Thread(runnable);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		pool.scheduleAtFixedRate(() -> {
			try {
				byte[] hello = "hello".getBytes();
				for(NettySocketClient client : clientMap.values()) {
					client.write(hello);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 2000, 10000, TimeUnit.MILLISECONDS);
	}
}
