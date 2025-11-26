/**
 * 
 */
package org.gaming.ruler.redis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class RedisMQ {
	
	private static Logger logger = LoggerFactory.getLogger(RedisMQ.class);
	
	private RedisMessageListenerContainer container;
	/**
	 * 订阅redis中的某个主题
	 * @param connectionFactory
	 * @param redisMessageListener
	 * @param topic
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer subscribe(RedisConnectionFactory connectionFactory) {
		container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}
	
	public static abstract class RedisMessageListener {
		
		@Autowired
		private RedisMessageListenerContainer redisMessageListenerContainer;
		
		protected static List<RedisMessageListener> messageListeners = new ArrayList<>();
		
		
		private MessageListenerAdapter listenerAdapter;
		
		
		@PostConstruct
		public final void init() {
			listenerAdapter = new MessageListenerAdapter(this, "receiveMessage");
			listenerAdapter.afterPropertiesSet();
			redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(topic()));
			
			messageListeners.add(this);
		}
		/**
		 * 订阅的主题
		 * @return
		 */
		public abstract String topic();
	    /**
	     * 当收到通道的消息之后执行的方法
	     * @param message
	     */
	    public abstract void receiveMessage(String message);
	    
	    public void unSubscribe() {
	    	try {
	    		if(this.listenerAdapter != null) {
	    			redisMessageListenerContainer.removeMessageListener(this.listenerAdapter);
	    			this.listenerAdapter = null;
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }

	    public void reSubscribe() {
			logger.info("重新进行Redis主题[" + topic() + "]的["+this.getClass().getSimpleName()+"]消息订阅");
	    	
	    	listenerAdapter = new MessageListenerAdapter(this, "receiveMessage");
			listenerAdapter.afterPropertiesSet();
			redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(topic()));
	    }
	}
}
