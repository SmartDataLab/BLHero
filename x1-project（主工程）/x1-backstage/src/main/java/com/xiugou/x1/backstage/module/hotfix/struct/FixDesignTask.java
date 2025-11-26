/**
 * 
 */
package com.xiugou.x1.backstage.module.hotfix.struct;

import java.util.ArrayList;
import java.util.List;

import com.xiugou.x1.backstage.module.hotfix.model.FixDesign;
import com.xiugou.x1.backstage.module.hotfix.model.FixDesignResult;

/**
 * @author YY
 *
 */
public class FixDesignTask {
	private FixDesign fixDesign;
	private List<FixDesignResult> results = new ArrayList<>();
	public FixDesign getFixDesign() {
		return fixDesign;
	}
	public void setFixDesign(FixDesign fixDesign) {
		this.fixDesign = fixDesign;
	}
	public List<FixDesignResult> getResults() {
		return results;
	}
	public void setResults(List<FixDesignResult> results) {
		this.results = results;
	}
}
