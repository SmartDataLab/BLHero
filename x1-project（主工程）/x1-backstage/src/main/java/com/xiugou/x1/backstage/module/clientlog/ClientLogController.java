/**
 * 
 */
package com.xiugou.x1.backstage.module.clientlog;

import org.gaming.backstage.PageData;
import org.gaming.backstage.PageQuery;
import org.gaming.backstage.module.apidoc.annotation.ApiDocument;
import org.gaming.db.util.QueryUtil.QuerySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiugou.x1.backstage.module.clientlog.model.ClientLog;
import com.xiugou.x1.backstage.module.clientlog.service.ClientLogService;

/**
 * @author YY
 *
 */
@Controller
public class ClientLogController {

	@Autowired
	private ClientLogService clientLogService;
	
	@ApiDocument("请求客户端日志数据")
	@RequestMapping(value = "/clientLog/data.auth")
	@ResponseBody
	public PageData<ClientLog> data(PageQuery query) {
		QuerySet querySet = new QuerySet();
		querySet.limit(query.getPage(), query.getLimit());
		querySet.orderBy("order by id desc");
		querySet.formWhere();
		
		return clientLogService.query(querySet);
	}
	
	/**
	 * level分为：fatal、error、warn，其中fatal会发送邮件至管理员邮箱
	 * title为邮件标题
	 * content为邮件内容
	 * http://120.79.34.46:10000的地址后续需要替换
	 * http://120.79.34.46:10000/api/clientLog?level=zzzz&title=yyyyy&content=xxxxxxx
	 * @return
	 */
	@ApiDocument("记录客户端日志，给游戏客户端请求用的")
	@RequestMapping(value = "/api/clientLog")
	@ResponseBody
	public void addLog(@RequestParam("level") String level, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("playerId") String playerId,
			@RequestParam("name") String name, @RequestParam("data") String data) {
		ClientLog log = new ClientLog();
		log.setLevel(level);
		log.setTitle(title);
		log.setContent(content);
		log.setPlayerId(playerId);
		log.setName(name);
		log.setData(data);
		clientLogService.insert(log);
	}
}
