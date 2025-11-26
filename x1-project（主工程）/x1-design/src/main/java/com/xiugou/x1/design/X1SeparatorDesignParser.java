/**
 * 
 */
package com.xiugou.x1.design;

import org.gaming.design.loader.SeparatorDesignParser;


/**
 * @author YY
 *
 */
public class X1SeparatorDesignParser extends SeparatorDesignParser {

	@Override
	protected String fieldSeparator() {
		return "#";
	}

	@Override
	protected String objSeparator() {
		return "\\|";
	}
}
