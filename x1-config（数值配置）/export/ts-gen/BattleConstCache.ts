
import App from "../../App";
import DesignCache from "../../config/base/DesignCache";
import DesignDataNotFoundError from "../../config/base/DesignDataNotFoundError";
import { X1SeparatorDesignParser } from "../../config/X1SeparatorDesignParser";
import { Reward } from "../../config/struct/Reward";
import { Keyv } from "../../config/struct/Keyv";
import { Cost } from "../../config/struct/Cost";
import { RandomItem } from "../../config/struct/RandomItem";
import { BattAttr } from "../../config/struct/BattAttr";
import { BattBuff } from "../../config/struct/BattBuff";
//当前类代码由导表工具生成，请勿修改
export default class BattleConstCache extends DesignCache<CFG_BattleConst> {
	private static instance: BattleConstCache = null;
	public static get Instance(): BattleConstCache {
		if(BattleConstCache.instance === null) {
			BattleConstCache.instance = new BattleConstCache();
			let dataList: object[] = App.ResManager.readConfigFile("gamecfg/" + BattleConstCache.instance.fileName);
			BattleConstCache.instance.LoadObjects(dataList, new X1SeparatorDesignParser());
		}
		return BattleConstCache.instance;
	}
	public get fileName(): string {
		return "3常量表_BattleConst";
	}
	protected createInstance(): CFG_BattleConst {
		return new CFG_BattleConst();
	}



	protected loadAutoGenerate(): void {
		//构建常量
		const constMap: Map<string, string> = new Map<string, string>();
		for(let i = 0; i < this.all().length; i++) {
			let data: CFG_BattleConst = this.all()[i];
			constMap.set(data.getKeyCol(), data.getValueCol());
		}
		this.loadConst(constMap);
	}





	protected final_normal_factor: number = 0;
	public getFinal_normal_factor(): number {
		return this.final_normal_factor;
	}
	protected final_skill_factor: number[] = [];
	public getFinal_skill_factor(): number[] {
		return this.final_skill_factor;
	}
	private formatFinal_skill_factor(): number {
		return 0;
	}
	protected hit_range: number[] = [];
	public getHit_range(): number[] {
		return this.hit_range;
	}
	private formatHit_range(): number {
		return 0;
	}
	protected crit_range: number[] = [];
	public getCrit_range(): number[] {
		return this.crit_range;
	}
	private formatCrit_range(): number {
		return 0;
	}
	protected dmg_float: number = 0;
	public getDmg_float(): number {
		return this.dmg_float;
	}
	protected fighting_factor: number[] = [];
	public getFighting_factor(): number[] {
		return this.fighting_factor;
	}
	private formatFighting_factor(): number {
		return 0;
	}
	protected hero_init_attr: BattAttr[] = [];
	public getHero_init_attr(): BattAttr[] {
		return this.hero_init_attr;
	}
	private formatHero_init_attr(): BattAttr {
		return new BattAttr();
	}
	protected hero_fell_trees: number = 0;
	public getHero_fell_trees(): number {
		return this.hero_fell_trees;
	}
	protected hero_on_create: Reward = null;
	public getHero_on_create(): Reward {
		return this.hero_on_create;
	}
	private formatHero_on_create(): Reward {
		return new Reward();
	}
	protected monster_init_attr: BattAttr[] = [];
	public getMonster_init_attr(): BattAttr[] {
		return this.monster_init_attr;
	}
	private formatMonster_init_attr(): BattAttr {
		return new BattAttr();
	}
	protected building_distance: number = 0;
	public getBuilding_distance(): number {
		return this.building_distance;
	}
	protected harvest_distance: number = 0;
	public getHarvest_distance(): number {
		return this.harvest_distance;
	}
	protected recruit_points_chance: number = 0;
	public getRecruit_points_chance(): number {
		return this.recruit_points_chance;
	}
	protected recruit_three_times: number = 0;
	public getRecruit_three_times(): number {
		return this.recruit_three_times;
	}
	protected recruit_five_times: number = 0;
	public getRecruit_five_times(): number {
		return this.recruit_five_times;
	}
	protected recruit_reset_time: number = 0;
	public getRecruit_reset_time(): number {
		return this.recruit_reset_time;
	}
	protected recruit_refresh_times: number = 0;
	public getRecruit_refresh_times(): number {
		return this.recruit_refresh_times;
	}
	protected recruit_extra_weight1: number = 0;
	public getRecruit_extra_weight1(): number {
		return this.recruit_extra_weight1;
	}
	protected recruit_multiplicity: number = 0;
	public getRecruit_multiplicity(): number {
		return this.recruit_multiplicity;
	}
	protected recruit_discount: number = 0;
	public getRecruit_discount(): number {
		return this.recruit_discount;
	}
	protected recruit_extra_weight4: number = 0;
	public getRecruit_extra_weight4(): number {
		return this.recruit_extra_weight4;
	}
	protected recruit_green_crystal: number = 0;
	public getRecruit_green_crystal(): number {
		return this.recruit_green_crystal;
	}
	protected recruit_second: number = 0;
	public getRecruit_second(): number {
		return this.recruit_second;
	}
	protected recruit_third: number = 0;
	public getRecruit_third(): number {
		return this.recruit_third;
	}
	protected recruit_gold: number = 0;
	public getRecruit_gold(): number {
		return this.recruit_gold;
	}
	protected recruit_diamond: Cost = null;
	public getRecruit_diamond(): Cost {
		return this.recruit_diamond;
	}
	private formatRecruit_diamond(): Cost {
		return new Cost();
	}
	protected recruit_ticket: Cost = null;
	public getRecruit_ticket(): Cost {
		return this.recruit_ticket;
	}
	private formatRecruit_ticket(): Cost {
		return new Cost();
	}
	protected recruit_ad_refresh_times: number = 0;
	public getRecruit_ad_refresh_times(): number {
		return this.recruit_ad_refresh_times;
	}
	protected hero_skillunlock_level1: Keyv[] = [];
	public getHero_skillunlock_level1(): Keyv[] {
		return this.hero_skillunlock_level1;
	}
	private formatHero_skillunlock_level1(): Keyv {
		return new Keyv();
	}
	protected hero_skillunlock_level2: Keyv[] = [];
	public getHero_skillunlock_level2(): Keyv[] {
		return this.hero_skillunlock_level2;
	}
	private formatHero_skillunlock_level2(): Keyv {
		return new Keyv();
	}
	protected backage_special: number = 0;
	public getBackage_special(): number {
		return this.backage_special;
	}
	protected mainline_box_time: number = 0;
	public getMainline_box_time(): number {
		return this.mainline_box_time;
	}
	protected mainline_box_max_num: number = 0;
	public getMainline_box_max_num(): number {
		return this.mainline_box_max_num;
	}
	protected mainline_revive_time: number = 0;
	public getMainline_revive_time(): number {
		return this.mainline_revive_time;
	}
	protected mainline_revive_cost: Cost = null;
	public getMainline_revive_cost(): Cost {
		return this.mainline_revive_cost;
	}
	private formatMainline_revive_cost(): Cost {
		return new Cost();
	}
	protected mainline_revive_num: number = 0;
	public getMainline_revive_num(): number {
		return this.mainline_revive_num;
	}
	protected equip_attr_numbr: Keyv[] = [];
	public getEquip_attr_numbr(): Keyv[] {
		return this.equip_attr_numbr;
	}
	private formatEquip_attr_numbr(): Keyv {
		return new Keyv();
	}
	protected hp_restore: number[] = [];
	public getHp_restore(): number[] {
		return this.hp_restore;
	}
	private formatHp_restore(): number {
		return 0;
	}
	protected player_renick_cost: Cost = null;
	public getPlayer_renick_cost(): Cost {
		return this.player_renick_cost;
	}
	private formatPlayer_renick_cost(): Cost {
		return new Cost();
	}
	protected equip_capacity_limit: number = 0;
	public getEquip_capacity_limit(): number {
		return this.equip_capacity_limit;
	}
	protected gather_skill: number[] = [];
	public getGather_skill(): number[] {
		return this.gather_skill;
	}
	private formatGather_skill(): number {
		return 0;
	}
	protected drop_book_rate: number = 0;
	public getDrop_book_rate(): number {
		return this.drop_book_rate;
	}
	protected drop_book_max: number = 0;
	public getDrop_book_max(): number {
		return this.drop_book_max;
	}
	protected purgatory_partner: Keyv[] = [];
	public getPurgatory_partner(): Keyv[] {
		return this.purgatory_partner;
	}
	private formatPurgatory_partner(): Keyv {
		return new Keyv();
	}
	protected purgatory_buy_refine_item: Cost = null;
	public getPurgatory_buy_refine_item(): Cost {
		return this.purgatory_buy_refine_item;
	}
	private formatPurgatory_buy_refine_item(): Cost {
		return new Cost();
	}
	protected purgatory_buy_plus_times: Cost = null;
	public getPurgatory_buy_plus_times(): Cost {
		return this.purgatory_buy_plus_times;
	}
	private formatPurgatory_buy_plus_times(): Cost {
		return new Cost();
	}
	protected purgatory_break_level: number = 0;
	public getPurgatory_break_level(): number {
		return this.purgatory_break_level;
	}
	protected task_ls_tz: number = 0;
	public getTask_ls_tz(): number {
		return this.task_ls_tz;
	}
	protected task_jysj_tzzb: Keyv[] = [];
	public getTask_jysj_tzzb(): Keyv[] {
		return this.task_jysj_tzzb;
	}
	private formatTask_jysj_tzzb(): Keyv {
		return new Keyv();
	}
	protected task_mcsj_tzzbbj: number = 0;
	public getTask_mcsj_tzzbbj(): number {
		return this.task_mcsj_tzzbbj;
	}
	protected task_mcsj_tzzb: Keyv[] = [];
	public getTask_mcsj_tzzb(): Keyv[] {
		return this.task_mcsj_tzzb;
	}
	private formatTask_mcsj_tzzb(): Keyv {
		return new Keyv();
	}
	protected task_lssj_tzzb: Keyv[] = [];
	public getTask_lssj_tzzb(): Keyv[] {
		return this.task_lssj_tzzb;
	}
	private formatTask_lssj_tzzb(): Keyv {
		return new Keyv();
	}
	protected gold_pid_ticket: Cost = null;
	public getGold_pid_ticket(): Cost {
		return this.gold_pid_ticket;
	}
	private formatGold_pid_ticket(): Cost {
		return new Cost();
	}
	protected tower_strength: number[] = [];
	public getTower_strength(): number[] {
		return this.tower_strength;
	}
	private formatTower_strength(): number {
		return 0;
	}
	protected tower_agility: number[] = [];
	public getTower_agility(): number[] {
		return this.tower_agility;
	}
	private formatTower_agility(): number {
		return 0;
	}
	protected tower_wisdom: number[] = [];
	public getTower_wisdom(): number[] {
		return this.tower_wisdom;
	}
	private formatTower_wisdom(): number {
		return 0;
	}
	protected village_soul_stone_rate: int  = null;
	public getVillage_soul_stone_rate(): int  {
		return this.village_soul_stone_rate;
	}
	private formatVillage_soul_stone_rate(): int  {
		return new int ();
	}
	protected village_break_level: number = 0;
	public getVillage_break_level(): number {
		return this.village_break_level;
	}
	protected dungeon_settlement_time: number = 0;
	public getDungeon_settlement_time(): number {
		return this.dungeon_settlement_time;
	}
	protected everyday_free_reward: Reward = null;
	public getEveryday_free_reward(): Reward {
		return this.everyday_free_reward;
	}
	private formatEveryday_free_reward(): Reward {
		return new Reward();
	}
	protected town_portal_cool_down_time: number = 0;
	public getTown_portal_cool_down_time(): number {
		return this.town_portal_cool_down_time;
	}
	protected evil_speed_cost: Cost = null;
	public getEvil_speed_cost(): Cost {
		return this.evil_speed_cost;
	}
	private formatEvil_speed_cost(): Cost {
		return new Cost();
	}
	protected evil_refine_num: number = 0;
	public getEvil_refine_num(): number {
		return this.evil_refine_num;
	}
	protected dungeon_combo_time: number = 0;
	public getDungeon_combo_time(): number {
		return this.dungeon_combo_time;
	}
	protected dungeon_combo_add: BattAttr = null;
	public getDungeon_combo_add(): BattAttr {
		return this.dungeon_combo_add;
	}
	private formatDungeon_combo_add(): BattAttr {
		return new BattAttr();
	}
	protected reconnects_time: number = 0;
	public getReconnects_time(): number {
		return this.reconnects_time;
	}
	protected disappears_time: number = 0;
	public getDisappears_time(): number {
		return this.disappears_time;
	}
	protected reconnectslimit_time: number = 0;
	public getReconnectslimit_time(): number {
		return this.reconnectslimit_time;
	}
	protected bubbling_time: number = 0;
	public getBubbling_time(): number {
		return this.bubbling_time;
	}
	protected Initial_hero: number[] = [];
	public getInitial_hero(): number[] {
		return this.Initial_hero;
	}
	private formatInitial_hero(): number {
		return 0;
	}
	protected questionnaire_reward: Reward[] = [];
	public getQuestionnaire_reward(): Reward[] {
		return this.questionnaire_reward;
	}
	private formatQuestionnaire_reward(): Reward {
		return new Reward();
	}
	protected seven_days_pack: number = 0;
	public getSeven_days_pack(): number {
		return this.seven_days_pack;
	}
	protected boss_drop_level_limit: number = 0;
	public getBoss_drop_level_limit(): number {
		return this.boss_drop_level_limit;
	}
	protected camp_adv_time: number = 0;
	public getCamp_adv_time(): number {
		return this.camp_adv_time;
	}
	protected camp_adv_exp_buff: number = 0;
	public getCamp_adv_exp_buff(): number {
		return this.camp_adv_exp_buff;
	}
	protected camp_adv_reborn_time: number = 0;
	public getCamp_adv_reborn_time(): number {
		return this.camp_adv_reborn_time;
	}
	protected camp_adv_store_up: number = 0;
	public getCamp_adv_store_up(): number {
		return this.camp_adv_store_up;
	}
	protected camp_adv_max_num: number = 0;
	public getCamp_adv_max_num(): number {
		return this.camp_adv_max_num;
	}
	protected camp_team_sight_range: number = 0;
	public getCamp_team_sight_range(): number {
		return this.camp_team_sight_range;
	}
	protected camp_monster_sensor_range: number = 0;
	public getCamp_monster_sensor_range(): number {
		return this.camp_monster_sensor_range;
	}
	protected camp_cool_time: number = 0;
	public getCamp_cool_time(): number {
		return this.camp_cool_time;
	}
	protected qrmb_last_days: number = 0;
	public getQrmb_last_days(): number {
		return this.qrmb_last_days;
	}
	protected privilege_normal_exp_pool_max: number = 0;
	public getPrivilege_normal_exp_pool_max(): number {
		return this.privilege_normal_exp_pool_max;
	}
	protected privilege_normal_exp_add: number = 0;
	public getPrivilege_normal_exp_add(): number {
		return this.privilege_normal_exp_add;
	}
	protected privilege_normal_equip_salvage: number = 0;
	public getPrivilege_normal_equip_salvage(): number {
		return this.privilege_normal_equip_salvage;
	}
	protected privilege_normal_dungeon_drop: number = 0;
	public getPrivilege_normal_dungeon_drop(): number {
		return this.privilege_normal_dungeon_drop;
	}
	protected privilege_normal_recruit_discount: number = 0;
	public getPrivilege_normal_recruit_discount(): number {
		return this.privilege_normal_recruit_discount;
	}
	protected suppression_coefficient: number = 0;
	public getSuppression_coefficient(): number {
		return this.suppression_coefficient;
	}
	protected offline_duration_time: number = 0;
	public getOffline_duration_time(): number {
		return this.offline_duration_time;
	}
	protected advertisement_add_tiem: Cost = null;
	public getAdvertisement_add_tiem(): Cost {
		return this.advertisement_add_tiem;
	}
	private formatAdvertisement_add_tiem(): Cost {
		return new Cost();
	}
	protected per_minute_monster: number = 0;
	public getPer_minute_monster(): number {
		return this.per_minute_monster;
	}
	protected buff_scale_standard: number = 0;
	public getBuff_scale_standard(): number {
		return this.buff_scale_standard;
	}
	protected min_attack_range: number = 0;
	public getMin_attack_range(): number {
		return this.min_attack_range;
	}
	protected troop_init_num: number = 0;
	public getTroop_init_num(): number {
		return this.troop_init_num;
	}
	protected Equip_attr_quality: string = "";
	public getEquip_attr_quality(): string {
		return this.Equip_attr_quality;
	}
	protected ref_monster_distance: number = 0;
	public getRef_monster_distance(): number {
		return this.ref_monster_distance;
	}
	protected box_exceed_distance: number = 0;
	public getBox_exceed_distance(): number {
		return this.box_exceed_distance;
	}
}
//当前类代码由导表工具生成，请勿修改
export class CFG_BattleConst implements IDesignData {
	//属性ID
	protected id: number = 0;
	//键名
	protected keyCol: string = "";
	//值类型
	protected typeCol: string = "";
	//值
	protected valueCol: string = "";
	public Id(): number {
		return this.id;
	}
	public getId(): number {
		return this.id;
	}
	public getKeyCol(): string {
		return this.keyCol;
	}
	public getTypeCol(): string {
		return this.typeCol;
	}
	public getValueCol(): string {
		return this.valueCol;
	}
}