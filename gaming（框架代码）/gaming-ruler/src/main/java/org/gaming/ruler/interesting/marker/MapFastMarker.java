/**
 * 
 */
package org.gaming.ruler.interesting.marker;

/**
 * @author YY
 *
 */
public class MapFastMarker extends MapMarker {
	//下标记录数组
	private int[] indexArray;
	
	protected MapFastMarker(int[] array, int[] indexArray) {
		super(array);
		this.indexArray = indexArray;
	}

	@Override
	protected void mark(int xyKey) {
		int i = this.indexArray[xyKey];
		if (i < checkIndex) {
			// 先交换检测区与空闲区
			int checkTemp = array[checkIndex - 1];
			array[checkIndex - 1] = array[i];
			array[i] = checkTemp;

			// 再交换空闲区与标记区
			int freeTemp = array[freeIndex - 1];
			array[freeIndex - 1] = array[checkIndex - 1];
			array[checkIndex - 1] = freeTemp;
			
			// 设置索引
			this.indexArray[array[i]] = i;
			this.indexArray[array[checkIndex - 1]] = checkIndex - 1;
			this.indexArray[array[freeIndex - 1]] = freeIndex - 1;

			checkIndex--;
			freeIndex--;
		} else if (i < freeIndex) {
			// 交换空闲区与标记区
			int freeTemp = array[freeIndex - 1];
			array[freeIndex - 1] = array[i];
			array[i] = freeTemp;
			
			// 设置索引
			this.indexArray[array[i]] = i;
			this.indexArray[array[freeIndex - 1]] = freeIndex - 1;

			freeIndex--;
		}
	}

	@Override
	protected void unmark(int xyKey) {
		int i = this.indexArray[xyKey];
		if (array[i] == xyKey) {
			// 先交换标记区与空闲区
			int markTemp = array[freeIndex];
			array[freeIndex] = array[i];
			array[i] = markTemp;

			// 再交换空闲区与检测区
			int temp = array[checkIndex];
			array[checkIndex] = array[freeIndex];
			array[freeIndex] = temp;
			
			// 设置索引
			this.indexArray[array[i]] = i;
			this.indexArray[array[freeIndex]] = freeIndex;
			this.indexArray[array[checkIndex]] = checkIndex;

			freeIndex++;
			checkIndex++;
		}
	}

	@Override
	protected void markChecked(int xyKey) {
		int i = this.indexArray[xyKey];
		if (array[i] == xyKey) {
			// 交换检测区与空闲区
			int temp = array[checkIndex - 1];
			array[checkIndex - 1] = array[i];
			array[i] = temp;
			
			// 设置索引
			this.indexArray[array[i]] = i;
			this.indexArray[array[checkIndex - 1]] = checkIndex - 1;

			checkIndex--;
		}
	}

	@Override
	protected boolean isMark(int xyKey) {
		int i = this.indexArray[xyKey];
		if(i < freeIndex) {
			return false;
		}
		return true;
	}
}
