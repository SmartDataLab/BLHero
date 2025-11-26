/**
 * 
 */
package org.gaming.ruler.interesting.marker;

import java.util.Arrays;

/**
 * @author YY
 *
 */
public class MapMarker {
	/**
	 * 坐标数组
	 */
	protected int[] array;
	/**
	 * 空闲区游标
	 */
	protected int freeIndex;
	/**
	 * 检测区游标
	 */
	protected int checkIndex;

	public MapMarker(int[] array) {
		this.array = array;
		this.freeIndex = array.length;
		this.checkIndex = array.length;
	}

	/**
	 * 对坐标进行标记
	 * 
	 * @param xyKey
	 */
	protected void mark(int xyKey) {
		for (int i = 0; i < freeIndex; i++) {
			if (array[i] == xyKey) {
				if (i < checkIndex) {
					// 先交换检测区与空闲区
					int checkTemp = array[checkIndex - 1];
					array[checkIndex - 1] = array[i];
					array[i] = checkTemp;

					// 再交换空闲区与标记区
					int freeTemp = array[freeIndex - 1];
					array[freeIndex - 1] = array[checkIndex - 1];
					array[checkIndex - 1] = freeTemp;

					checkIndex--;
					freeIndex--;
				} else if (i < freeIndex) {
					// 交换空闲区与标记区
					int freeTemp = array[freeIndex - 1];
					array[freeIndex - 1] = array[i];
					array[i] = freeTemp;

					freeIndex--;
				}
				break;
			}
		}
	}

	/**
	 * 取消对坐标的标记
	 * 
	 * @param xy
	 */
	protected void unmark(int xyKey) {
		for (int i = freeIndex; i < array.length; i++) {
			if (array[i] == xyKey) {
				// 先交换标记区与空闲区
				int markTemp = array[freeIndex];
				array[freeIndex] = array[i];
				array[i] = markTemp;

				// 再交换空闲区与检测区
				int temp = array[checkIndex];
				array[checkIndex] = array[freeIndex];
				array[freeIndex] = temp;

				freeIndex++;
				checkIndex++;
				break;
			}
		}
	}

	/**
	 * 标记坐标为已检测
	 * 
	 * @param xy
	 */
	protected void markChecked(int xyKey) {
		for (int i = 0; i < checkIndex; i++) {
			if (array[i] == xyKey) {
				// 交换检测区与空闲区
				int temp = array[checkIndex - 1];
				array[checkIndex - 1] = array[i];
				array[i] = temp;

				checkIndex--;
				break;
			}
		}
	}

	/**
	 * 坐标是否已被标记
	 * 
	 * @param xy
	 * @return
	 */
	protected boolean isMark(int xyKey) {
		for (int i = 0; i < freeIndex; i++) {
			if (array[i] == xyKey) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 重置检测区游标
	 */
	protected void resetCheck() {
		this.checkIndex = this.freeIndex;
	}
	protected int[] getArray() {
		return array;
	}
	public int getFreeIndex() {
		return freeIndex;
	}
	public int getCheckIndex() {
		return checkIndex;
	}
	@Override
	public String toString() {
		return "MapMarker [array=" + Arrays.toString(array) + ", freeIndex=" + freeIndex
				+ ", checkIndex=" + checkIndex + "]";
	}
	
	public int getXYKey(int index) {
		if(index >= freeIndex) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return array[index];
	}
	

	public static void main(String[] args) {

		int[] array = new int[] { 0, 3, 6, 9, 1, 4, 7, 10, 2, 5, 8, 11 };
		MapMarker marker = new MapMarker(array);

		marker.markChecked(10);
		System.out.println(Arrays.toString(marker.array) + " " + marker.freeIndex + " "
				+ marker.checkIndex);

		marker.markChecked(11);
		System.out.println(Arrays.toString(marker.array) + " " + marker.freeIndex + " "
				+ marker.checkIndex);

		marker.mark(4);
		System.out.println(Arrays.toString(marker.array) + " " + marker.freeIndex + " "
				+ marker.checkIndex);

		// 这里无法对坐标10进行取消标记的操作，因为当前坐标10仍然在空闲区内
		// marker.unmark(10);
		// System.out.println(Arrays.toString(marker.array) + " " +
		// marker.freeIndex);
	}

	

}
