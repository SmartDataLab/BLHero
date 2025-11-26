/**
 * 
 */
package org.gaming.ruler.interesting.aoi;

/**
 * @author YY
 *
 */
public interface IAreaOfInterest<T> {

	AoiEntity<T> enter(T t, int x, int y, int widthSize, int heightSize);
	
	void moveTo(T t, int x, int y);
	
	void exit(T t);
}
