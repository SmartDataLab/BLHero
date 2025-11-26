/**
 * 
 */
package org.gaming.ruler.netty;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.gaming.tool.RandomUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * @author YY
 *
 */
@Sharable
public class NettyHandler extends ChannelInboundHandlerAdapter {

	private static AtomicInteger counter = new AtomicInteger();
	
	private static AtomicInteger ID = new AtomicInteger();
	
	private final AttributeKey<Integer> KEY = AttributeKey.newInstance("CONTEXT");
	
	private static ConcurrentMap<Integer, Boolean> map = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		String sql = "";
//		for(int i = 1; i <= 290; i++) {
//			sql += "select id, open_time from jhjy_" + i + "_data.server_info union all\n";
//		}
		
		for(int i = 190; i <= 290; i++) {
			sql += "select s1.id,s1.open_time,s2.* from jhjy_"+i+"_data.server_info s1 left join jhjy_" + i + "_data.p669_system s2 on 1 = 1 union all\n";
		}
		
//		for(int i = 197; i <= 264; i++) {
//			sql += "select * from jhjy_"+i+"_data.p669_newyingxiongjijie union all\n";
//		}
		
		LocalDateTime t1 = LocalDateTime.of(2023, 3, 3, 5, 0);
		System.out.println(t1.plusDays(61));
		
//		System.out.println(sql);
	}
	
	static int minPort = Integer.MAX_VALUE;
	static int maxPort = 0;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		int count = counter.incrementAndGet();
		ctx.channel().attr(KEY).set(ID.incrementAndGet());
		System.out.println("当前连接数+" + count + " " + ctx.channel().localAddress() + " " + ctx.channel().attr(KEY).get());
		map.put(ctx.channel().attr(KEY).get(), true);
		String hostPort = ctx.channel().localAddress().toString();
		String[] strs = hostPort.split(":");
		int port = Integer.parseInt(strs[1]);
		if(port > maxPort) {
			maxPort = port;
		}
		if(port < minPort) {
			minPort = port;
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		int count = counter.decrementAndGet();
		map.remove(ctx.channel().attr(KEY).get());
		System.out.println("当前连接数-" + count);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] data = (byte[])msg;
		String hello = new String(data);
		int id = ctx.channel().attr(KEY).get();
		if(RandomUtil.closeClose(1, 10000) < 3) {
			System.out.println(id + " channelRead " + hello + " " + map.size() + " " + minPort + " " + maxPort);
		}
		if("hello".equals(hello)) {
			ctx.channel().writeAndFlush("world".getBytes());
		}
	}
}
