package com.xiugou.x1.game.server.module.shop;

import java.util.Map;

import org.gaming.fakecmd.annotation.PlayerCmd;
import org.gaming.prefab.exception.Asserts;
import org.gaming.prefab.thing.NoticeType;
import org.gaming.prefab.thing.RewardReceipt;
import org.gaming.ruler.eventbus.EventBus;
import org.gaming.tool.LocalDateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.xiugou.x1.design.constant.GameCause;
import com.xiugou.x1.design.constant.TipsCode;
import com.xiugou.x1.design.module.ExchangeShopCache;
import com.xiugou.x1.design.module.autogen.ExchangeShopAbstractCache.ExchangeShopCfg;
import com.xiugou.x1.design.struct.ThingUtil;
import com.xiugou.x1.game.server.foundation.player.PlayerContext;
import com.xiugou.x1.game.server.module.bag.service.ThingService;
import com.xiugou.x1.game.server.module.ministruct.PbHelper;
import com.xiugou.x1.game.server.module.player.AbstractModuleHandler;
import com.xiugou.x1.game.server.module.server.service.ServerInfoService;
import com.xiugou.x1.game.server.module.shop.constant.ShopEnum;
import com.xiugou.x1.game.server.module.shop.event.ExchangeShopEvent;
import com.xiugou.x1.game.server.module.shop.model.ShopPlayer;
import com.xiugou.x1.game.server.module.shop.model.ShopSystem;
import com.xiugou.x1.game.server.module.shop.service.AbstractShopService;
import com.xiugou.x1.game.server.module.shop.service.ShopPlayerService;
import com.xiugou.x1.game.server.module.shop.service.ShopSystemService;
import com.xiugou.x1.game.server.module.shop.struct.ProductDetailData;

import pb.xiugou.x1.protobuf.exchangeshop.ExchangeShop.ExchangeShopExchangeRequest;
import pb.xiugou.x1.protobuf.exchangeshop.ExchangeShop.ExchangeShopExchangeResponse;
import pb.xiugou.x1.protobuf.exchangeshop.ExchangeShop.ExchangeShopInfoResponse;
import pb.xiugou.x1.protobuf.exchangeshop.ExchangeShop.PbShop;

/**
 * @author yh
 * @date 2023/7/27
 * @apiNote
 */
@Controller
public class ShopHandler extends AbstractModuleHandler {

	@Autowired
	private ThingService thingService;
	@Autowired
	private ExchangeShopCache exchangeShopCache;
	@Autowired
	private ServerInfoService serverInfoService;
	@Autowired
	private ShopSystemService shopSystemService;
	@Autowired
	private ShopPlayerService shopPlayerService;
	
	@Override
	public InfoPriority infoPriority() {
		return InfoPriority.DETAIL;
	}
	
	@Override
	public void pushInfo(PlayerContext playerContext) {
		long playerId = playerContext.getId();
		ExchangeShopInfoResponse.Builder response = ExchangeShopInfoResponse.newBuilder();

		for (AbstractShopService shopService : AbstractShopService.shopService.values()) {
			int shopId = shopService.shopEnum().getShopId();
			ShopPlayer shopPlayer = shopService.getShopPlayer(playerId);
			ShopSystem shopSystem = shopService.getShopSystem();
			
			PbShop.Builder pbShop = PbShop.newBuilder();
			pbShop.setShopId(shopId);
			pbShop.setResetTime(LocalDateTimeUtil.toEpochMilli(shopSystem.getNextReset()));
			pbShop.setRound(shopSystem.getConfigRound());
			for (ProductDetailData product : shopPlayer.getProductDetail().values()) {
				pbShop.addProducts(AbstractShopService.build(product));
			}
			response.addShops(pbShop);
		}
		response.setOpenDay(serverInfoService.getOpenedDay());
		playerContextManager.push(playerId, ExchangeShopInfoResponse.Proto.ID, response.build());
	}

    @PlayerCmd
    public ExchangeShopExchangeResponse exchange(PlayerContext playerContext, ExchangeShopExchangeRequest request) {
        int shopId = request.getShopId();
        ShopEnum shopEnum = ShopEnum.valueOf(shopId);
        Asserts.isTrue(shopEnum != null, TipsCode.EXCHANGE_SHOP_ID_WRONG, shopId);
        AbstractShopService service = AbstractShopService.getService(shopEnum);
		
        ShopPlayer shopPlayer = service.getShopPlayer(playerContext.getId());
        
        Map<Integer, ProductDetailData> productDetail = shopPlayer.getProductDetail();
        ProductDetailData product = productDetail.get(request.getProductId());
        if (product == null) {
        	product = new ProductDetailData();
        	product.setProductId(request.getProductId());
            productDetail.put(product.getProductId(), product);
        }

        //兑换期数限定
        ShopSystem shopSystem = service.getShopSystem();
        ExchangeShopCfg exchangeShopCfg = exchangeShopCache.findInShopIdRoundProductIdIndex(shopId, shopSystem.getConfigRound(), product.getProductId());

        int closeDay = exchangeShopCfg.getCloseDay();
        if (closeDay > 0) {
        	//开服天数限定，开服后x天关闭
        	int openedDay = serverInfoService.getOpenedDay();
            Asserts.isTrue(closeDay >= openedDay, TipsCode.EXCHANGE_PRODUCT_CANNOT_BUY);
        }
        //条件限制
		Asserts.isTrue(shopSystemService.limitCondition(playerContext.getId(), exchangeShopCfg.getCondition()),
				TipsCode.EXCHANGE_CONDITION_INCORRECT);
		
		boolean watchAdv = false;
		int num = request.getExchangeNum();
        //数据变更
        if (product.getFreeNum() + num <= exchangeShopCfg.getFreeTimes()) {
        	product.setFreeNum(product.getFreeNum() + num);
        } else if (product.getAdvNum() + num <= exchangeShopCfg.getAdvertising()) {
        	product.setAdvNum(product.getAdvNum() + num);
        	watchAdv = true;
        } else {
            if (exchangeShopCfg.getLimit() != 0) {
                Asserts.isTrue(product.getBuyNum() + num <= exchangeShopCfg.getLimit(), TipsCode.EXCHANGE_ACHIEVE_LIMIT);
            }
            if(exchangeShopCfg.getCost().getNum() > 0) {
            	thingService.cost(playerContext.getId(), ThingUtil.multiplyCost(exchangeShopCfg.getCost(), num), GameCause.EXCHANGE_PRODUCT);
            }
            product.setBuyNum(product.getBuyNum() + num);
        }

        shopPlayerService.update(shopPlayer);
        //商品发放
		RewardReceipt rewardReceipt = thingService.add(playerContext.getId(),
				ThingUtil.multiplyReward(exchangeShopCfg.getProduct(), num), GameCause.EXCHANGE_PRODUCT,
				NoticeType.TIPS);
        
        EventBus.post(ExchangeShopEvent.of(playerContext.getId(), num, watchAdv));

        ExchangeShopExchangeResponse.Builder response = ExchangeShopExchangeResponse.newBuilder();
		response.setReceipt(PbHelper.build(rewardReceipt));
		response.setShopId(shopId);
        response.setProduct(AbstractShopService.build(product));
		response.setExchangeNum(num);
        return response.build();
    }
}
