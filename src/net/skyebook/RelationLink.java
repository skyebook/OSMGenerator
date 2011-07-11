/**
 *  OSMGenerator - Simplified loading of OpenStreetMap data into MySQL
 *  Copyright (C) 2011  Skye Book
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
