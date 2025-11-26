/**
 * 
 */
package org.gaming.protobuf3.simulator.protocol;

/**
 * @author YY
 *
 */
public interface ProtocolClassPrinter {

	void print(Object object, String offset, StringBuilder result, boolean printInline);
}
