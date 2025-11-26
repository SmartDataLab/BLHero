/**
 * 
 */
package com.xiugou.x1.backstage.module.clientversion;

import org.gaming.backstage.PageData;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.clientversion.model.ClientVersion;
import com.xiugou.x1.backstage.module.clientversion.service.ClientVersionService;
import com.xiugou.x1.backstage.module.clientversion.struct.ClientVersionForm;
import com.xiugou.x1.backstage.module.clientversion.struct.ClientVersionQuery;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;

/**
 * @author YY
 *
 */
@Controller
public class ClientVersionController {

	@Autowired
	private ClientVersionService clientVersionService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求客户端版本信息数据")
	@RequestMapping(value = "/clientVersion/data.auth")
	@ResponseBody
	public PageData<ClientVersion> data(ClientVersionQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		if(query.getChannelId() > 0) {
			querySet.addCondition("channel_id = ?", query.getChannelId());
		}
		querySet.formWhere();
		
		return clientVersionService.query(querySet);
	}
	
	@ApiDocument("保存版本信息数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/clientVersion/save.authw")
	@ResponseBody
	public ClientVersion save(ClientVersionForm form) {
//		Asserts.isTrue(form.getServerType() != 0, TipsCode.CLIENT_VERSION_TYPE_ERROR);
		
		ClientVersion clientVersion = null;
		if(form.getId() <= 0) {
			GameChannel gameChannel = gameChannelService.getEntity(form.getChannelId());
			Asserts.isTrue(gameChannel != null, TipsCode.CHANNEL_MISS, form.getChannelId());
			
			clientVersion = new ClientVersion();
			clientVersion.setChannelId(gameChannel.getId());
			clientVersion.setChannelName(gameChannel.getName());
			clientVersion.setVersionCode(form.getVersionCode());
			clientVersion.setServerType(form.getServerType());
			clientVersion.setRemoteUrl(form.getRemoteUrl());
			clientVersion.setResourceVersion(form.getResourceVersion());
			clientVersion.setRemark(form.getRemark());
			clientVersion.setPcResourceVersion(form.getPcResourceVersion());
			clientVersion.setQuickUrl(form.getQuickUrl());
			clientVersionService.insert(clientVersion);
		} else {
			clientVersion = clientVersionService.getById(form.getId());
			Asserts.isTrue(clientVersion != null, TipsCode.CLIENT_VERSION_MISS, form.getId());
			
			clientVersion.setServerType(form.getServerType());
			clientVersion.setRemoteUrl(form.getRemoteUrl());
			clientVersion.setResourceVersion(form.getResourceVersion());
			clientVersion.setRemark(form.getRemark());
			clientVersion.setPcResourceVersion(form.getPcResourceVersion());
			clientVersion.setQuickUrl(form.getQuickUrl());
			clientVersionService.update(clientVersion);
		}
		return clientVersion;
	}
	
	@ApiDocument("删除版本信息数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/clientVersion/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("id") long id) {
		ClientVersion clientVersion = clientVersionService.getById(id);
		Asserts.isTrue(clientVersion != null, TipsCode.CLIENT_VERSION_MISS, id);
		
		clientVersionService.delete(clientVersion);
	}
}
