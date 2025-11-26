/**
 * 
 */
package com.xiugou.x1.cross.server.module.friend;

import org.gaming.fakecmd.annotation.PlayerCrossCmd;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.cross.server.foundation.game.GameContext;

import pb.xiugou.x1.protobuf.friend.Friend.FriendInfoRequest;
import pb.xiugou.x1.protobuf.friend.Friend.FriendInfoResponse;

/**
 * @author yy
 *
 */
@Controller
public class FriendHandler {

	@PlayerCrossCmd
	public FriendInfoResponse test(GameContext gameContext, long playerId, FriendInfoRequest request) {
		System.out.println(request.getId() + " " + request.getNick());
		
		FriendInfoResponse.Builder response = FriendInfoResponse.newBuilder();
		response.setId(request.getId());
		response.setNick(request.getNick());
		return response.build();
	}
}
