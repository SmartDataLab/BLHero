/**
 * 
 */
package org.gaming.prefab.rank;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author YY
 *
 */
public class TopN<Key, T extends TopEntity<Key>> {

	private final int pageSize;
	
	private final TopPageLoader<T> pageLoader;
	
	private Page[] pages;
	//最大页数
	private int maxPage;
	private int maxCount;
	
	private final Comparator<T> comparator;
	
	public TopN(int pageSize, TopPageLoader<T> pageLoader, Comparator<T> comparator) {
		this.pageSize = pageSize;
		this.pageLoader = pageLoader;
		
		this.maxCount = pageLoader.dataCount();
		this.maxPage = this.maxCount / pageSize;
		if(this.maxCount % pageSize != 0) {
			this.maxPage ++;
		}
		if(this.maxPage == 0) {
			this.maxPage = 1;
		}
		this.comparator = comparator;
		this.pages = new Page[maxPage];
	}
	
	protected static class Page {
		//数据释放时间
		public long gcTime;
		
		private Object[] datas;
	}
	
	public synchronized void rank(T t) {
		if(t.getRankIndex() == -1) {
			t.setRankIndex(maxCount);
			this.maxCount ++;
			
			int pageIndex = t.getRankIndex() / pageSize;
			int rankIndex = t.getRankIndex() % pageSize;
			if(rankIndex != 0 && t.getRankIndex() > pageSize * maxPage) {
				pageIndex ++;
			}
			Page page = getPage(pageIndex);
			page.datas[rankIndex] = t;
		}
		doRank(t);
	}
	
	@SuppressWarnings("unchecked")
	private void doRank(T t) {
		int currPage = t.getRankIndex() / pageSize;
		int currIndex = t.getRankIndex() % pageSize;
		Page page = getPage(currPage);
		
		//1、向前排序
		forward : while(true) {
			//是否已经添加到排序中
			if(currPage == 0 && currIndex == 0) {
				page.datas[0] = t;
				break forward;
			}
			for(int i = currIndex - 1; i >= 0; i--) {
				T front = (T)page.datas[i];
				int result = comparator.compare(t, front);
				if(result < 0) {
					page.datas[i] = t;
					page.datas[currIndex] = front;
					t.setRankIndex(currPage * pageSize + i);
					front.setRankIndex(currPage * pageSize + currIndex);
					currIndex = i;
				} else {
					//向前排序时，如果temp比t的排名更前或者排名没有变化，则表示需要向后排序
					break forward;
				}
			}
			if(currPage > 0) {
				Page frontPage = getPage(currPage - 1);
				T front = (T)frontPage.datas[pageSize - 1];
				int result = comparator.compare(t, front);
				
				if(result < 0) {
					//跟前面一页的最后一名做比较
					frontPage.datas[pageSize - 1] = t;
					page.datas[0] = front;
					t.setRankIndex(currPage * pageSize - 1);
					front.setRankIndex(currPage * pageSize);
					
					currPage = t.getRankIndex() / pageSize;
					currIndex = t.getRankIndex() % pageSize;
					page = getPage(currPage);
				} else {
					break forward;
				}
			} else {
				break forward;
			}
		}
		
		//2、向后排序
		backward : while(true) {
			//是否已经添加到排序中
			for(int i = currIndex + 1; i < pageSize; i++) {
				T behind = (T)page.datas[i];
				if(behind == null) {
					break backward;
				}
				int result = comparator.compare(t, behind);
				if(result > 0) {
					page.datas[i] = t;
					page.datas[currIndex] = behind;
					t.setRankIndex(currPage * pageSize + i);
					behind.setRankIndex(currPage * pageSize + currIndex);
					currIndex = i;
				} else {
					break backward;
				}
			}
			
			Page behindPage = getPage(currPage + 1);
			T behind = (T)behindPage.datas[0];
			if(behind == null) {
				break backward;
			} else {
				//跟后面一页的第一名做比较
				int result = comparator.compare(t, behind);
				if(result > 0) {
					behindPage.datas[0] = t;
					page.datas[pageSize - 1] = behind;
					t.setRankIndex(currPage * pageSize + 1);
					behind.setRankIndex(currPage * pageSize);
					
					currPage = t.getRankIndex() / pageSize;
					currIndex = t.getRankIndex() % pageSize;
					page = getPage(currPage);
				} else {
					break backward;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param page 从0开始
	 * @return
	 */
	private Page getPage(int page) {
		if(pages.length < page + 1) {
			synchronized (this) {
				if(pages.length < page + 1) {
					Page[] tempPages = pages;
					maxPage = pages.length * 2;
					pages = new Page[maxPage];
					System.arraycopy(tempPages, 0, pages, 0, tempPages.length);
				}
			}
		}
//		System.out.println("page " + page + " " + pages.length);
		Page pageData = (Page)pages[page];
		if(pageData == null) {
			synchronized (this) {
				pageData = (Page)pages[page];
				if(pageData == null) {
					pageData = new Page();
					pageData.datas = new Object[pageSize];
					//TODO 
					pageData.gcTime = System.currentTimeMillis() + 10000000;
					//TODO 加载数据
					List<T> list = this.pageLoader.loadPage(page, pageSize);
					for(int i = 0; i < list.size() && i < pageData.datas.length; i++) {
						pageData.datas[i] = list.get(i);
					}
					pages[page] = pageData;
				}
			}
		}
		return pageData;
	}
	
	public static interface TopPageLoader<E> {
		List<E> loadPage(int page, int pageSize);
		int dataCount();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> topN(int n) {
		List<T> list = new ArrayList<>();
		int pageIndex = n / pageSize;
		if(n % pageSize != 0) {
			pageIndex ++;
		}
		out : for(int i = 0; i < pageIndex && i < maxPage; i++) {
			Page page = this.getPage(i);
			for(Object obj : page.datas) {
				if(obj == null) {
					break out;
				}
				list.add((T)obj);
			}
		}
		return list;
	}
}
