/**
 * 
 */
package org.gaming.design.loader;

import java.io.File;
import java.util.List;

/**
 * @author YY
 *
 */
public interface IDesignReader {

	List<DesignFile> read(File file);
}
