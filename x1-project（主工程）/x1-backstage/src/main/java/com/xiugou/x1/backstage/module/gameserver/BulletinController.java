/**
 * 
 */
package com.xiugou.x1.backstage.module.gameserver;

import java.util.List;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.advice.Asserts;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.tool.PageUtil;
import org.gaming.tool.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.TipsCode;
import com.xiugou.x1.backstage.module.gameserver.form.BulletinForm;
import com.xiugou.x1.backstage.module.gameserver.model.Bulletin;
import com.xiugou.x1.backstage.module.gameserver.model.GameChannel;
import com.xiugou.x1.backstage.module.gameserver.service.BulletinService;
import com.xiugou.x1.backstage.module.gameserver.service.GameChannelService;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions;
import com.xiugou.x1.backstage.module.gameserver.vo.DropDownOptions.DropDownOption;

/**
 * @author YY
 *
 */
@Controller
public class BulletinController {

	@Autowired
	private BulletinService bulletinService;
	@Autowired
	private GameChannelService gameChannelService;
	
	@ApiDocument("请求公告数据")
	@RequestMapping(value = "/bulletin/data.auth")
	@ResponseBody
	public PageData<Bulletin> info(PageQuery query) {
		List<Bulletin> list = bulletinService.getEntities();
		SortUtil.sort(list, Bulletin::getId);
		
		PageData<Bulletin> pageData = new PageData<>();
		pageData.setCount(list.size());
		pageData.setData(PageUtil.pageN(list, query.getPage(), query.getLimit()));
		return pageData;
	}
	
	@ApiDocument("保存公告数据，数据ID小于等于0时插入数据，大于0时更新对应数据")
	@RequestMapping(value = "/bulletin/save.authw")
	@ResponseBody
	public Bulletin save(BulletinForm form) {
		Bulletin bulletin = null;
		if(form.getId() <= 0) {
			bulletin = new Bulletin();
			bulletin.setTitle(form.getTitle());
			bulletin.setContent(form.getContent());
			bulletinService.insert(bulletin);
		} else {
			bulletin = bulletinService.getEntity(form.getId());
			Asserts.isTrue(bulletin != null, TipsCode.BULLETIN_MISS, form.getId());
			
			bulletin.setTitle(form.getTitle());
			bulletin.setContent(form.getContent());
			bulletinService.update(bulletin);
		}
		return bulletin;
	}
	
	@ApiDocument("删除渠道数据，并删除与之关联的服务器关系数据")
	@RequestMapping(value = "/bulletin/delete.authw")
	@ResponseBody
	public void delete(@RequestParam("id") long id) {
		Bulletin bulletin = bulletinService.getEntity(id);
		Asserts.isTrue(bulletin != null, TipsCode.BULLETIN_MISS, id);
		
		for(GameChannel gameChannel : gameChannelService.getEntities()) {
			if(gameChannel.getBulletinId() == id) {
				Asserts.isTrue(false, TipsCode.BULLETIN_USING_IN_CHANNEL, gameChannel.getName());
			}
		}
		bulletinService.delete(bulletin);
	}
	
	@ApiDocument("请求公告下拉菜单数据")
	@RequestMapping(value = "/bulletin/options.do")
	@ResponseBody
	public DropDownOptions bulletinOptions() {
		List<Bulletin> list = bulletinService.getEntities();
		SortUtil.sort(list, Bulletin::getId);
		
		DropDownOptions options = new DropDownOptions();
		for(Bulletin bulletin : list) {
			options.addOption(bulletin.getId(), bulletin.getTitle());
		}
		SortUtil.sort(options.getOptions(), DropDownOption::getValue);
		return options;
	}
}
