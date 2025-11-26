/**
 * 
 */
package org.gaming.ruler.interesting.marker;

import java.util.Random;

/**
 * @author YY
 *
 */
public class MapWholeDesigner {
	
	private int xScale;
	private int yScale;
	protected MapMarker[] wholeMarkers;
	
	private int[] indexArray;
	
	public MapWholeDesigner(int xScale, int yScale) {
		this(xScale, yScale, true);
	}
	
	public MapWholeDesigner(int xScale, int yScale, boolean useFastMarker) {
		this.xScale = xScale;
		this.yScale = yScale;
		
		int[] array = new int[xScale * yScale];
		int index = 0;
		//将坐标的换算值逐一添加到一维数组中
		for(int y = 0; y < yScale; y++) {
			for(int x = 0; x < xScale; x++) {
				array[index] = x * yScale + y;
				index++;
			}
		}
		
		//下标记录数组的构建
		indexArray = new int[array.length];
		for(int i = 0; i < array.length; i++) {
			indexArray[array[i]] = i;
		}
		
		//本类是整个地图的设计器，因此marker数组的长度为1
		//后续还会有分块的地图设计器，marker数组的长度是分块的块数
		//甚至也可以添加分环的地图设计器，marker数组的长度是分环的环数
		wholeMarkers = new MapMarker[1];
		if(useFastMarker) {
			wholeMarkers[0] = new MapFastMarker(array, indexArray);
		} else {
			wholeMarkers[0] = new MapMarker(array);
		}
	}
	
	//使用带种子的随机类，可便于验证同一个随机种子在多次随机中产生的结果
	//每一次的修改或者升级都需要用带种子的随机类来反复验证
	//正式环境下可以去除种子的设定
	private Random random = new Random(6);
	
	public int getX(int xyKey) {
		return xyKey / yScale;
	}
	public int getY(int xyKey) {
		return xyKey % yScale;
	}
	private int getXYKey(int x, int y) {
		if(x < 0 || x >= xScale || y < 0 || y >= yScale) {
			throw new RuntimeException("非法的坐标");
		}
		return x * yScale + y;
	}
	
	private boolean isMark(int x, int y) {
		return wholeMarkers[0].isMark(getXYKey(x, y));
	}
	
	private int randomXYKey() {
		if(wholeMarkers[0].getCheckIndex() == 0) {
			throw new RuntimeException("已经没有可用的位置了");
		}
		int index = random.nextInt(wholeMarkers[0].getCheckIndex());
		return wholeMarkers[0].getXYKey(index);
	}
	
	public int randomXY(int width, int height) {
		int xyKey = randomXYKey();
		while(true) {
			if(isXYKeyUsable(xyKey, width, height)) {
				break;
			} else {
				//当某一次的随机坐标不适用时，标记为已检测
				wholeMarkers[0].markChecked(xyKey);
				xyKey = randomXYKey();
			}
		}
		return xyKey;
	}
	
	public boolean isXYUsable(int x, int y, int width, int height) {
		int endX = x + width - 1;
		int endY = y + height - 1;
		//先检查边界的情况
		if(x < 0 || x > endX || xScale <= endX || y < 0 || y > endY || yScale <= endY) {
			return false;
		}
		//再逐一检查各个坐标位置是否已被标记
		boolean usable = true;
		for (int x0 = x; x0 <= endX; x0++) {
			for (int y0 = y; y0 <= endY; y0++) {
				if(isMark(x0, y0)) {
					usable = false;
					break;
				}
			}
		}
		return usable;
	}
	public boolean isXYUsable(int x, int y, int size) {
		return isXYUsable(x, y, size, size);
	}
	private boolean isXYKeyUsable(int xyKey, int width, int height) {
		int x = getX(xyKey);
		int y = getY(xyKey);
		return isXYUsable(x, y, width, height);
	}
	public void mark(int x, int y, int size) {
		mark(x, y, size, size);
	}
	
	public void mark(int x, int y, int width, int height) {
		int endX = x + width - 1;
		int endY = y + height - 1;
		markArea(x, y, endX, endY);
	}
	/**
	 * 标记某一个范围内的所有坐标位置
	 * @param x
	 * @param y
	 * @param endX
	 * @param endY
	 */
	public void markArea(int x, int y, int endX, int endY) {
		isLegalCoordinate(x, y, endX, endY);
		for (int x0 = x; x0 <= endX; x0++) {
			for (int y0 = y; y0 <= endY; y0++) {
				wholeMarkers[0].mark(getXYKey(x0, y0));
			}
		}
	}
	
	public void isLegalCoordinate(int x, int y, int endX, int endY) {
		if (x < 0 || x > endX || endX >= xScale || y < 0 || y > endY || endY >= yScale) {
			throw new IllegalArgumentException("x:" + x + " y:" + y + " endX:" + endX + "endY:"
					+ endY + " is illegal");
		}
	}
	/**
	 * 重置已检测区的游标
	 */
	public void resetCheck() {
		for (int i = 0; i < wholeMarkers.length; i++) {
			wholeMarkers[i].resetCheck();
		}
	}
	
	/**
	 * 打印二维数组
	 */
	public void printDesigner() {
		for (int i = 0; i < wholeMarkers.length; i++) {
			StringBuilder printer = new StringBuilder();
			
			MapMarker marker = wholeMarkers[i];
			for (int y = 0; y < yScale; y++) {
				for (int x = 0; x < xScale; x++) {
					boolean isMark = marker.isMark(getXYKey(x, y));
					if(isMark) {
						printer.append("1");
					} else {
						printer.append("0");
					}
					printer.append(" ");
				}
				printer.append("\n");
			}
			System.out.println(printer.toString());
		}
	}
}
