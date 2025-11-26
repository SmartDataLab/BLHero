/**
 * 
 */
package pojo.xiugou.x1.pojo.module.player.model;

/**
 * @author YY
 *
 */
public interface IPlayerEntity {
	long getId();
	String getNick();
	int getLevel();
	long getFighting();
	String getHead();
	boolean isOnline();
	int getServerId();
	int getImage();
}
