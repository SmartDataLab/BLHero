/**
 * 
 */
package org.gaming.ruler.interesting.marker;

/**
 * @author YY
 *
 */
public class MapMarkerTester {

	public static void main(String[] args) {
		// 地图的规模是1000*1000
		int scale = 10;
		MapWholeDesigner designer = new MapWholeDesigner(scale, scale, false);
		// 循环次数
		int loopCount = 10;

		long time11 = System.currentTimeMillis();
		for (int i = 0; i < loopCount; i++) {
			int xyKey = designer.randomXY(2, 2);
			designer.mark(designer.getX(xyKey), designer.getY(xyKey), 2);
			designer.resetCheck();
		}
		long time12 = System.currentTimeMillis();
		System.out.println("普通marker耗时:" + (time12 - time11));

		MapWholeDesigner fastDesigner = new MapWholeDesigner(scale, scale, true);

		long time21 = System.currentTimeMillis();
		for (int i = 0; i < loopCount; i++) {
			int xyKey = fastDesigner.randomXY(2, 2);
			fastDesigner.mark(fastDesigner.getX(xyKey), fastDesigner.getY(xyKey), 2);
			fastDesigner.resetCheck();
		}
		long time22 = System.currentTimeMillis();
		System.out.println("快速marker耗时:" + (time22 - time21));
		
		boolean isSame = true;
		for (int i = 0; i < designer.wholeMarkers.length; i++) {
			for (int j = 0; j < designer.wholeMarkers[i].array.length; j++) {
				if(designer.wholeMarkers[i].array[j] != fastDesigner.wholeMarkers[i].array[j]) {
					isSame = false;
					break;
				}
			}
		}
		System.out.println("普通marker与快速marker在进行位置随机后的结果是否一致：" + isSame);
		
		designer.printDesigner();
		fastDesigner.printDesigner();
	}
}
