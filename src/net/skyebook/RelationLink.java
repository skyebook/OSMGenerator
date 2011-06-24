/**
 * 
 */
package net.skyebook;

import edu.poly.bxmc.betaville.osm.BaseOSMObject;

/**
 * @author Skye Book
 *
 */
public class RelationLink extends BaseOSMObject {
	
	public static enum Type{node, way, relation};
	
	public Type type;
	

	/**
	 * 
	 */
	public RelationLink(){
	}
	
	public void setType(Type type){
		this.type=type;
	}
	
	public Type getType(){
		return type;
	}
}
