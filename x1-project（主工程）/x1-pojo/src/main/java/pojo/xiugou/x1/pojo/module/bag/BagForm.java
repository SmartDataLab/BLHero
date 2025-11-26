package pojo.xiugou.x1.pojo.module.bag;

/**
 * @author yh
 * @date 2023/7/18
 * @apiNote
 */
public class BagForm {
    private int itemId;//道具ID
    private String itemName; //道具名称
    private long haveNum; //拥有数量

    public int getItemId() {
	  return itemId;
    }

    public void setItemId(int itemId) {
	  this.itemId = itemId;
    }

    public String getItemName() {
	  return itemName;
    }

    public void setItemName(String itemName) {
	  this.itemName = itemName;
    }

    public long getHaveNum() {
	  return haveNum;
    }

    public void setHaveNum(long haveNum) {
	  this.haveNum = haveNum;
    }
}
