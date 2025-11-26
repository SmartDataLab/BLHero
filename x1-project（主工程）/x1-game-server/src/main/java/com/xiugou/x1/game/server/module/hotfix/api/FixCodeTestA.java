/**
 * 
 */
package com.xiugou.x1.game.server.module.hotfix.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YY
 *
 */
@Component
public class FixCodeTestA {
	
	@Autowired
	private FixCodeTestB fixCodeTestB;
	
	public void test() {
		fixCodeTestB.test();
	}
}
