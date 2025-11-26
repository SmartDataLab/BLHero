/**
 * 
 */
package com.xiugou.x1.backstage.module.gamecause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.gaming.tool.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.gamecause.model.GameCause;
import com.xiugou.x1.backstage.module.gamecause.service.GameCauseService;
import com.xiugou.x1.backstage.module.gameserver.model.GameServer;
import com.xiugou.x1.backstage.module.gameserver.service.GameServerService;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.godfinger.struct.GodFingerQuery;
import com.xiugou.x1.backstage.util.X1HttpUtil;

import pojo.xiugou.x1.pojo.GameApi;
import pojo.xiugou.x1.pojo.module.server.ServerResponse;
import pojo.xiugou.x1.pojo.module.server.form.GameCauseForm;

/**
 * @author YY
 *
 */
@Controller
public class GameCauseController {

	@Autowired
	private GameCauseService gameCauseService;
	@Autowired
	private GameServerService gameServerService;
	
	
	@ApiDocument("请求流水事件数据")
	@RequestMapping(value = "/gameCause/data.auth")
	@ResponseBody
	public PageData<GameCause> data(GodFingerQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.formWhere();
		
		return gameCauseService.query(querySet);
	}
	
	@ApiDocument("更新流水事件数据")
	@RequestMapping(value = "/gameCause/refresh.auth")
	@ResponseBody
	public void refresh() {
		GameServer gameServer = gameServerService.getEntities().get(0);
		
		ServerResponse serverResponse = X1HttpUtil.formPost(gameServer, GameApi.gameCauseUpdate, Collections.emptyMap());

		List<GameCauseForm> list = GsonUtil.getList(serverResponse.getData(), GameCauseForm.class);
		
		List<GameCause> updateList = new ArrayList<>();
		for(GameCauseForm form : list) {
			GameCause cause = new GameCause();
			cause.setId(form.getId());
			cause.setName(form.getName());
			updateList.add(cause);
		}
		gameCauseService.insertUpdate(updateList);
	}
	
	@ApiDocument("流水事件下拉菜单数据")
	@RequestMapping(value = "/gameCause/options.do")
	@ResponseBody
	public DropDownOptions options() {
		List<GameCause> list = gameCauseService.getAll();
		
		DropDownOptions options = new DropDownOptions();
		for(GameCause gameCause : list) {
			options.addOption(gameCause.getId(), gameCause.getName());
		}
		return options;
	}
}
