package pojo.xiugou.x1.pojo.module.hero;

/**
 * @author yh
 * @date 2023/7/18
 * @apiNote
 */
public class HeroForm {
	// 英雄唯一ID
	private long id;
	// 英雄名字
	private String name;
	// 英雄品质
	private int quality;
	// 英雄等级
	private int level;
	// 碎片数量
	private long num;
	// 战力
	private long fighting;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public long getFighting() {
		return fighting;
	}

	public void setFighting(long fighting) {
		this.fighting = fighting;
	}
}
