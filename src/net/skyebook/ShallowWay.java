/**
 * 
 */
package net.skyebook;

import java.util.ArrayList;
import java.util.List;

import edu.poly.bxmc.betaville.osm.BaseOSMObject;

/**
 * @author Skye Book
 *
 */
public class ShallowWay extends BaseOSMObject {
	
	private ArrayList<Long> nodeReferences;

	/**
	 * 
	 */
	public ShallowWay() {
		nodeReferences = new ArrayList<Long>();
	}
	
	public void addNodeReference(long nodeReference){
		nodeReferences.add(nodeReference);
	}
	
	public List<Long> getNodeReferences(){
		return nodeReferences;
	}

}
