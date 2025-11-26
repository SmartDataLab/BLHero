/**
 * 
 */
package org.gaming.tool;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author YY
 *
 */
public class SortUtil {
	
	public static interface ISort {
		int sortBy();
	}
	
	public final static Comparator<ISort> SORTER = new Comparator<ISort>() {
		@Override
		public int compare(ISort o1, ISort o2) {
			return Integer.compare(o1.sortBy(), o2.sortBy());
		}
	};
	
	public static <T> void sortInt(List<T> list, Function<T, Integer> f) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int k1 = f.apply(o1);
				int k2 = f.apply(o2);
				return sortValue(k1, k2);
			}
		});
	}
	
	public static <T> void sortIntDesc(List<T> list, Function<T, Integer> f) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int k1 = f.apply(o1);
				int k2 = f.apply(o2);
				return sortValue(k2, k1);
			}
		});
	}
	
	public static <T> void sort(List<T> list, Function<T, Long> f) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				long k1 = f.apply(o1);
				long k2 = f.apply(o2);
				return sortValue(k1, k2);
			}
		});
	}
	
	public static <T> void sortDesc(List<T> list, Function<T, Long> f) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				long k1 = f.apply(o1);
				long k2 = f.apply(o2);
				return -sortValue(k1, k2);
			}
		});
	}
	
	public static <T> void sortStr(List<T> list, Function<T, String> f) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				String k1 = f.apply(o1);
				String k2 = f.apply(o2);
				return k1.compareTo(k2);
			}
		});
	}
	
	private static int sortValue(long k1, long k2) {
		if(k1 < k2) {
			return -1;
		} else if(k1 > k2) {
			return 1;
		} else {
			return 0;
		}
	}
	
	
	public static <T> void sort(List<T> list, Function<T, Long> f1, Function<T, Long> f2) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				long k1 = f1.apply(o1);
				long k2 = f1.apply(o2);
				int v1 = sortValue(k1, k2);
				if(v1 != 0) {
					return v1;
				} else {
					long sk1 = f2.apply(o1);
					long sk2 = f2.apply(o2);
					return sortValue(sk1, sk2);
				}
			}
		});
	}
	
	
	
	public static <T> void sortDesc(List<T> list, Function<T, Long> f1, Function<T, Long> f2) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				long k1 = f1.apply(o1);
				long k2 = f1.apply(o2);
				int v1 = -sortValue(k1, k2);
				if(v1 != 0) {
					return v1;
				} else {
					long sk1 = f2.apply(o1);
					long sk2 = f2.apply(o2);
					return -sortValue(sk1, sk2);
				}
			}
		});
	}
	
	public static <T> void sortInt(List<T> list, Function<T, Integer> f1, Function<T, Integer> f2) {
		Collections.sort(list, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int k1 = f1.apply(o1);
				int k2 = f1.apply(o2);
				int v1 = sortValue(k1, k2);
				if(v1 != 0) {
					return v1;
				} else {
					int sk1 = f2.apply(o1);
					int sk2 = f2.apply(o2);
					return sortValue(sk1, sk2);
				}
			}
		});
	}
}
