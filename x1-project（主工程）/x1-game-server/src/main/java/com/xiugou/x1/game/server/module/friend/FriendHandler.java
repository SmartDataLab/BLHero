/**
 * 
 */
package com.xiugou.x1.game.server.module.friend;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.foundation.player.PlayerContextManager;

import pb.xiugou.x1.protobuf.friend.Friend.FriendInfoRequest;
import pb.xiugou.x1.protobuf.friend.Friend.FriendInfoResponse;

/**
 * @author YY
 *
 */
@Controller
public class FriendHandler {

	@Autowired
	private PlayerContextManager playerContextManager;
	
	@PlayerCmd(forwardTo = "cross")
	public void test(PlayerContext playerContext, FriendInfoRequest request) {
	}
	
	@PlayerCrossCmd
	public void test(long playerId, FriendInfoResponse response) {
		System.out.println("FriendHandler " + response.getId() + " " + response.getNick());
		playerContextManager.push(playerId, FriendInfoResponse.Proto.ID, response);
	}
}
