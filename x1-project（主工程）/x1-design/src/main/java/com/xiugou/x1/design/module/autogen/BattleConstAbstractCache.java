package com.xiugou.x1.design.module.autogen;


public abstract class BattleConstAbstractCache<T extends BattleConstAbstractCache.BattleConstCfg> extends org.gaming.design.loader.DesignCache<T> {
	@Override
	public String fileName() {
		return "3常量表_BattleConst";
	}


	@Override
	protected final void loadAutoGenerate() {
		//构建常量
		java.util.HashMap<String, String> constMap = new java.util.HashMap<String, String>();
		for(T data : all()) {
			constMap.put(data.getKeyCol(), data.getValueCol());
		}
		this.loadConst(constMap);
	}





	public static class BattleConstCfg implements org.gaming.design.loader.IDesignData {
		/**
		 * 属性ID
		 */
		protected int id;
		/**
		 * 键名
		 */
		protected String keyCol;
		/**
		 * 值类型
		 */
		protected String typeCol;
		/**
		 * 值
		 */
		protected String valueCol;
		@Override
		public int id() {
			return id;
		}
		public int getId() {
			return id;
		}
		public String getKeyCol() {
			return keyCol;
		}
		public String getTypeCol() {
			return typeCol;
		}
		public String getValueCol() {
			return valueCol;
		}
	}

	protected int final_normal_factor;
	public int getFinal_normal_factor() {
		return final_normal_factor;
	}
	protected java.util.List<Integer> final_skill_factor;
	public java.util.List<Integer> getFinal_skill_factor() {
		return final_skill_factor;
	}
	protected java.util.List<Integer> hit_range;
	public java.util.List<Integer> getHit_range() {
		return hit_range;
	}
	protected java.util.List<Integer> crit_range;
	public java.util.List<Integer> getCrit_range() {
		return crit_range;
	}
	protected int dmg_float;
	public int getDmg_float() {
		return dmg_float;
	}
	protected java.util.List<Float> fighting_factor;
	public java.util.List<Float> getFighting_factor() {
		return fighting_factor;
	}
	protected java.util.List<com.xiugou.x1.battle.config.Attr> hero_init_attr;
	public java.util.List<com.xiugou.x1.battle.config.Attr> getHero_init_attr() {
		return hero_init_attr;
	}
	protected int hero_fell_trees;
	public int getHero_fell_trees() {
		return hero_fell_trees;
	}
	protected com.xiugou.x1.design.struct.RewardThing hero_on_create;
	public com.xiugou.x1.design.struct.RewardThing getHero_on_create() {
		return hero_on_create;
	}
	protected java.util.List<com.xiugou.x1.battle.config.Attr> monster_init_attr;
	public java.util.List<com.xiugou.x1.battle.config.Attr> getMonster_init_attr() {
		return monster_init_attr;
	}
	protected int building_distance;
	public int getBuilding_distance() {
		return building_distance;
	}
	protected int harvest_distance;
	public int getHarvest_distance() {
		return harvest_distance;
	}
	protected int recruit_points_chance;
	public int getRecruit_points_chance() {
		return recruit_points_chance;
	}
	protected int recruit_three_times;
	public int getRecruit_three_times() {
		return recruit_three_times;
	}
	protected int recruit_five_times;
	public int getRecruit_five_times() {
		return recruit_five_times;
	}
	protected long recruit_reset_time;
	public long getRecruit_reset_time() {
		return recruit_reset_time;
	}
	protected int recruit_refresh_times;
	public int getRecruit_refresh_times() {
		return recruit_refresh_times;
	}
	protected int recruit_extra_weight1;
	public int getRecruit_extra_weight1() {
		return recruit_extra_weight1;
	}
	protected int recruit_multiplicity;
	public int getRecruit_multiplicity() {
		return recruit_multiplicity;
	}
	protected int recruit_discount;
	public int getRecruit_discount() {
		return recruit_discount;
	}
	protected int recruit_extra_weight4;
	public int getRecruit_extra_weight4() {
		return recruit_extra_weight4;
	}
	protected int recruit_green_crystal;
	public int getRecruit_green_crystal() {
		return recruit_green_crystal;
	}
	protected int recruit_second;
	public int getRecruit_second() {
		return recruit_second;
	}
	protected int recruit_third;
	public int getRecruit_third() {
		return recruit_third;
	}
	protected int recruit_gold;
	public int getRecruit_gold() {
		return recruit_gold;
	}
	protected com.xiugou.x1.design.struct.CostThing recruit_diamond;
	public com.xiugou.x1.design.struct.CostThing getRecruit_diamond() {
		return recruit_diamond;
	}
	protected com.xiugou.x1.design.struct.CostThing recruit_ticket;
	public com.xiugou.x1.design.struct.CostThing getRecruit_ticket() {
		return recruit_ticket;
	}
	protected int recruit_ad_refresh_times;
	public int getRecruit_ad_refresh_times() {
		return recruit_ad_refresh_times;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> hero_skillunlock_level1;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getHero_skillunlock_level1() {
		return hero_skillunlock_level1;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> hero_skillunlock_level2;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getHero_skillunlock_level2() {
		return hero_skillunlock_level2;
	}
	protected int backage_special;
	public int getBackage_special() {
		return backage_special;
	}
	protected int mainline_box_time;
	public int getMainline_box_time() {
		return mainline_box_time;
	}
	protected int mainline_box_max_num;
	public int getMainline_box_max_num() {
		return mainline_box_max_num;
	}
	protected int mainline_revive_time;
	public int getMainline_revive_time() {
		return mainline_revive_time;
	}
	protected com.xiugou.x1.design.struct.CostThing mainline_revive_cost;
	public com.xiugou.x1.design.struct.CostThing getMainline_revive_cost() {
		return mainline_revive_cost;
	}
	protected int mainline_revive_num;
	public int getMainline_revive_num() {
		return mainline_revive_num;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> equip_attr_numbr;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getEquip_attr_numbr() {
		return equip_attr_numbr;
	}
	protected java.util.List<Integer> hp_restore;
	public java.util.List<Integer> getHp_restore() {
		return hp_restore;
	}
	protected com.xiugou.x1.design.struct.CostThing player_renick_cost;
	public com.xiugou.x1.design.struct.CostThing getPlayer_renick_cost() {
		return player_renick_cost;
	}
	protected int equip_capacity_limit;
	public int getEquip_capacity_limit() {
		return equip_capacity_limit;
	}
	protected java.util.List<Integer> gather_skill;
	public java.util.List<Integer> getGather_skill() {
		return gather_skill;
	}
	protected int drop_book_rate;
	public int getDrop_book_rate() {
		return drop_book_rate;
	}
	protected int drop_book_max;
	public int getDrop_book_max() {
		return drop_book_max;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> purgatory_partner;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getPurgatory_partner() {
		return purgatory_partner;
	}
	protected com.xiugou.x1.design.struct.CostThing purgatory_buy_refine_item;
	public com.xiugou.x1.design.struct.CostThing getPurgatory_buy_refine_item() {
		return purgatory_buy_refine_item;
	}
	protected com.xiugou.x1.design.struct.CostThing purgatory_buy_plus_times;
	public com.xiugou.x1.design.struct.CostThing getPurgatory_buy_plus_times() {
		return purgatory_buy_plus_times;
	}
	protected int purgatory_break_level;
	public int getPurgatory_break_level() {
		return purgatory_break_level;
	}
	protected int task_ls_tz;
	public int getTask_ls_tz() {
		return task_ls_tz;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> task_jysj_tzzb;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getTask_jysj_tzzb() {
		return task_jysj_tzzb;
	}
	protected int task_mcsj_tzzbbj;
	public int getTask_mcsj_tzzbbj() {
		return task_mcsj_tzzbbj;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> task_mcsj_tzzb;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getTask_mcsj_tzzb() {
		return task_mcsj_tzzb;
	}
	protected java.util.List<com.xiugou.x1.design.struct.Keyv> task_lssj_tzzb;
	public java.util.List<com.xiugou.x1.design.struct.Keyv> getTask_lssj_tzzb() {
		return task_lssj_tzzb;
	}
	protected com.xiugou.x1.design.struct.CostThing gold_pid_ticket;
	public com.xiugou.x1.design.struct.CostThing getGold_pid_ticket() {
		return gold_pid_ticket;
	}
	protected java.util.List<Integer> tower_strength;
	public java.util.List<Integer> getTower_strength() {
		return tower_strength;
	}
	protected java.util.List<Integer> tower_agility;
	public java.util.List<Integer> getTower_agility() {
		return tower_agility;
	}
	protected java.util.List<Integer> tower_wisdom;
	public java.util.List<Integer> getTower_wisdom() {
		return tower_wisdom;
	}
	protected int  village_soul_stone_rate;
	public int  getVillage_soul_stone_rate() {
		return village_soul_stone_rate;
	}
	protected int village_break_level;
	public int getVillage_break_level() {
		return village_break_level;
	}
	protected int dungeon_settlement_time;
	public int getDungeon_settlement_time() {
		return dungeon_settlement_time;
	}
	protected com.xiugou.x1.design.struct.RewardThing everyday_free_reward;
	public com.xiugou.x1.design.struct.RewardThing getEveryday_free_reward() {
		return everyday_free_reward;
	}
	protected int town_portal_cool_down_time;
	public int getTown_portal_cool_down_time() {
		return town_portal_cool_down_time;
	}
	protected com.xiugou.x1.design.struct.CostThing evil_speed_cost;
	public com.xiugou.x1.design.struct.CostThing getEvil_speed_cost() {
		return evil_speed_cost;
	}
	protected int evil_refine_num;
	public int getEvil_refine_num() {
		return evil_refine_num;
	}
	protected int dungeon_combo_time;
	public int getDungeon_combo_time() {
		return dungeon_combo_time;
	}
	protected com.xiugou.x1.battle.config.Attr dungeon_combo_add;
	public com.xiugou.x1.battle.config.Attr getDungeon_combo_add() {
		return dungeon_combo_add;
	}
	protected int reconnects_time;
	public int getReconnects_time() {
		return reconnects_time;
	}
	protected int disappears_time;
	public int getDisappears_time() {
		return disappears_time;
	}
	protected int reconnectslimit_time;
	public int getReconnectslimit_time() {
		return reconnectslimit_time;
	}
	protected int bubbling_time;
	public int getBubbling_time() {
		return bubbling_time;
	}
	protected java.util.List<Integer> Initial_hero;
	public java.util.List<Integer> getInitial_hero() {
		return Initial_hero;
	}
	protected java.util.List<com.xiugou.x1.design.struct.RewardThing> questionnaire_reward;
	public java.util.List<com.xiugou.x1.design.struct.RewardThing> getQuestionnaire_reward() {
		return questionnaire_reward;
	}
	protected int seven_days_pack;
	public int getSeven_days_pack() {
		return seven_days_pack;
	}
	protected int boss_drop_level_limit;
	public int getBoss_drop_level_limit() {
		return boss_drop_level_limit;
	}
	protected int camp_adv_time;
	public int getCamp_adv_time() {
		return camp_adv_time;
	}
	protected int camp_adv_exp_buff;
	public int getCamp_adv_exp_buff() {
		return camp_adv_exp_buff;
	}
	protected int camp_adv_reborn_time;
	public int getCamp_adv_reborn_time() {
		return camp_adv_reborn_time;
	}
	protected int camp_adv_store_up;
	public int getCamp_adv_store_up() {
		return camp_adv_store_up;
	}
	protected int camp_adv_max_num;
	public int getCamp_adv_max_num() {
		return camp_adv_max_num;
	}
	protected int camp_team_sight_range;
	public int getCamp_team_sight_range() {
		return camp_team_sight_range;
	}
	protected int camp_monster_sensor_range;
	public int getCamp_monster_sensor_range() {
		return camp_monster_sensor_range;
	}
	protected int camp_cool_time;
	public int getCamp_cool_time() {
		return camp_cool_time;
	}
	protected int qrmb_last_days;
	public int getQrmb_last_days() {
		return qrmb_last_days;
	}
	protected int privilege_normal_exp_pool_max;
	public int getPrivilege_normal_exp_pool_max() {
		return privilege_normal_exp_pool_max;
	}
	protected int privilege_normal_exp_add;
	public int getPrivilege_normal_exp_add() {
		return privilege_normal_exp_add;
	}
	protected float privilege_normal_equip_salvage;
	public float getPrivilege_normal_equip_salvage() {
		return privilege_normal_equip_salvage;
	}
	protected float privilege_normal_dungeon_drop;
	public float getPrivilege_normal_dungeon_drop() {
		return privilege_normal_dungeon_drop;
	}
	protected int privilege_normal_recruit_discount;
	public int getPrivilege_normal_recruit_discount() {
		return privilege_normal_recruit_discount;
	}
	protected int suppression_coefficient;
	public int getSuppression_coefficient() {
		return suppression_coefficient;
	}
	protected int offline_duration_time;
	public int getOffline_duration_time() {
		return offline_duration_time;
	}
	protected com.xiugou.x1.design.struct.CostThing advertisement_add_tiem;
	public com.xiugou.x1.design.struct.CostThing getAdvertisement_add_tiem() {
		return advertisement_add_tiem;
	}
	protected int per_minute_monster;
	public int getPer_minute_monster() {
		return per_minute_monster;
	}
	protected int buff_scale_standard;
	public int getBuff_scale_standard() {
		return buff_scale_standard;
	}
	protected int min_attack_range;
	public int getMin_attack_range() {
		return min_attack_range;
	}
	protected int troop_init_num;
	public int getTroop_init_num() {
		return troop_init_num;
	}
	protected String Equip_attr_quality;
	public String getEquip_attr_quality() {
		return Equip_attr_quality;
	}
	protected int ref_monster_distance;
	public int getRef_monster_distance() {
		return ref_monster_distance;
	}
	protected int box_exceed_distance;
	public int getBox_exceed_distance() {
		return box_exceed_distance;
	}
	protected int arena_challenge_item;
	public int getArena_challenge_item() {
		return arena_challenge_item;
	}
	protected int unlock_towns;
	public int getUnlock_towns() {
		return unlock_towns;
	}
	protected int return_distance;
	public int getReturn_distance() {
		return return_distance;
	}
	protected int unlock_safe_zone;
	public int getUnlock_safe_zone() {
		return unlock_safe_zone;
	}
}