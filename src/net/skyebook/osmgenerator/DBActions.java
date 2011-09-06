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
package net.skyebook.osmgenerator;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;

import com.mysql.jdbc.Statement;

import edu.poly.bxmc.betaville.osm.Node;
import edu.poly.bxmc.betaville.osm.Relation;
import edu.poly.bxmc.betaville.osm.RelationMemeber;
import edu.poly.bxmc.betaville.osm.tag.AbstractTag;

/**
 * @author Skye Book
 *
 */
public class DBActions {

	private AtomicBoolean busy = new AtomicBoolean(false);

	private Connection con = null;

	private static final int bufferSizeLimit = (4096*4);
	private static final int smallTableMultiple = 2;

	private int insertNodeBufferSize = 0;
	private StringBuilder bulkInsertNodeBuilder;
	private Statement bulkInsertNode;

	private int insertNodeTagBufferSize = 0;
	private StringBuilder bulkInsertNodeTagBuilder;
	private Statement bulkInsertNodeTag;

	private int insertWayBufferSize = 0;
	private StringBuilder bulkInsertWayBuilder;
	private Statement bulkInsertWay;
	
	private int insertWayTagBufferSize = 0;
	private StringBuilder bulkInsertWayTagBuilder;
	private Statement bulkInsertWayTag;

	private int insertWayMemberBufferSize = 0;
	private StringBuilder bulkInsertWayMemberBuilder;
	private Statement bulkInsertWayMember;

	private int insertRelationBufferSize = 0;
	private StringBuilder bulkInsertRelationBuilder;
	private Statement bulkInsertRelation;
	
	private int insertRelationTagBufferSize = 0;
	private StringBuilder bulkInsertRelationTagBuilder;
	private Statement bulkInsertRelationTag;

	private int insertRelationMemberBufferSize = 0;
	private StringBuilder bulkInsertRelationMemberBuilder;
	private Statement bulkInsertRleationMember;

	private static final String BULK_DELIMITER = "$,$";



	/**
	 * Constructor - Creates (opens) the SQL connection
	 */
	public DBActions(String user, String pass, String dbName) {
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/"+dbName,
						pass,
						pass);

				bulkInsertNode = (Statement) con.createStatement();
				bulkInsertNodeTag = (Statement) con.createStatement();
				bulkInsertWay = (Statement) con.createStatement();
				bulkInsertWayTag = (Statement) con.createStatement();
				bulkInsertWayMember = (Statement) con.createStatement();
				bulkInsertRelation = (Statement) con.createStatement();
				bulkInsertRelationTag = (Statement) con.createStatement();
				bulkInsertRleationMember = (Statement) con.createStatement();

				System.out.println("Statements Prepared");

			} catch (ClassNotFoundException e) {
				System.err.println("ClassNotFoundException: " + e.getMessage());
			} catch (InstantiationException e) {
				System.err.println("InstantiationException: " + e.getMessage());
			} catch (IllegalAccessException e) {
				System.err.println("IllegalAccessException: " + e.getMessage());
			}
		} catch (SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
			System.err.println("SQLState: " + e.getSQLState());
			System.err.println("VendorError: " + e.getErrorCode());
		}
	}

	public void flushAllLoadBuffers(){
		pushBulkNodes();
		pushBulkNodeTags();
		pushBulkWays();
		pushBulkWayTags();
		pushBulkWayMembers();
		pushBulkRelations();
		pushBulkRelationTags();
		pushBulkRelationMembers();
	}

	private void pushBulkNodes(){
		if(bulkInsertNodeBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertNodeBuilder.toString());
			bulkInsertNode.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertNode.setLocalInfileInputStream(is);

			bulkInsertNode.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE nodes FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (id, latitude, longitude, tags)");

			bulkInsertNode.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertNodeBuilder = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void pushBulkNodeTags(){
		if(bulkInsertNodeTagBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertNodeTagBuilder.toString());
			bulkInsertNodeTag.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertNodeTag.setLocalInfileInputStream(is);

			bulkInsertNodeTag.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE node_tags FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (node, key, value)");

			bulkInsertNodeTag.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertNodeTagBuilder = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pushBulkWays(){
		if(bulkInsertWayBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertWayBuilder.toString());
			bulkInsertWay.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertWay.setLocalInfileInputStream(is);

			bulkInsertWay.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE ways FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (id, tags)");

			bulkInsertWay.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertWayBuilder = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void pushBulkWayTags(){
		if(bulkInsertWayTagBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertWayTagBuilder.toString());
			bulkInsertWayTag.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertWayTag.setLocalInfileInputStream(is);

			bulkInsertWayTag.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE way_tags FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (node, key, value)");

			bulkInsertWayTag.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertWayTagBuilder = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pushBulkWayMembers(){
		if(bulkInsertWayMemberBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertWayMemberBuilder.toString());
			bulkInsertWayMember.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertWayMember.setLocalInfileInputStream(is);

			bulkInsertWayMember.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE way_members FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (way, node)");

			bulkInsertWayMember.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertWayMemberBuilder=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pushBulkRelations(){
		if(bulkInsertRelationBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertRelationBuilder.toString());
			bulkInsertRelation.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertRelation.setLocalInfileInputStream(is);

			bulkInsertRelation.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE relations FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (id, tags)");

			bulkInsertRelation.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertRelationBuilder=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void pushBulkRelationTags(){
		if(bulkInsertRelationTagBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertRelationTagBuilder.toString());
			bulkInsertRelationTag.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertRelationTag.setLocalInfileInputStream(is);

			bulkInsertRelationTag.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE relation_tags FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (node, key, value)");

			bulkInsertRelationTag.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertRelationTagBuilder = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void pushBulkRelationMembers(){
		if(bulkInsertRelationMemberBuilder == null) return;
		try {
			InputStream is = IOUtils.toInputStream(bulkInsertRelationMemberBuilder.toString());
			bulkInsertRleationMember.execute("SET UNIQUE_CHECKS=0; ");
			bulkInsertRleationMember.setLocalInfileInputStream(is);

			bulkInsertRleationMember.execute("LOAD DATA LOCAL INFILE 'file.txt' INTO TABLE relation_members FIELDS TERMINATED BY '"+BULK_DELIMITER+"' (relation, way, type)");

			bulkInsertRleationMember.execute("SET UNIQUE_CHECKS=1; ");
			bulkInsertRelationMemberBuilder=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNode(Node node) throws SQLException{
		busy.set(true);

		// if this is the first node in this buffer create a new StringBuilder
		if(insertNodeBufferSize==0) bulkInsertNodeBuilder = new StringBuilder();

		bulkInsertNodeBuilder.append(node.getId());
		bulkInsertNodeBuilder.append(BULK_DELIMITER);
		bulkInsertNodeBuilder.append(node.getLocation().getLatitude());
		bulkInsertNodeBuilder.append(BULK_DELIMITER);
		bulkInsertNodeBuilder.append(node.getLocation().getLongitude());
		bulkInsertNodeBuilder.append(BULK_DELIMITER);
		bulkInsertNodeBuilder.append((node.getTags()==null)?"NULL":createTagString(node.getTags()));

		insertNodeBufferSize++;

		if(insertNodeBufferSize==bufferSizeLimit){
			// execute the insert
			pushBulkNodes();

			// reset the buffer size
			insertNodeBufferSize=0;
		}
		else{
			// if this wans't the final node in the buffer, add a newline
			bulkInsertNodeBuilder.append("\n");
		}

		// do nothing if there are no tags
		if(node.getTags()!=null){
			for(AbstractTag tag : node.getTags()){
				addNodeTag(node.getId(), tag);
			}
		}

		busy.set(false);
	}

	private void addNodeTag(long nodeID, AbstractTag tag){

		if(insertNodeTagBufferSize==0) bulkInsertNodeTagBuilder = new StringBuilder();

		bulkInsertNodeTagBuilder.append(nodeID);
		bulkInsertNodeTagBuilder.append(BULK_DELIMITER);
		bulkInsertNodeTagBuilder.append(tag.getKey());
		bulkInsertNodeTagBuilder.append(BULK_DELIMITER);
		bulkInsertNodeTagBuilder.append(tag.getValue());

		insertNodeTagBufferSize++;

		if(insertNodeTagBufferSize==bufferSizeLimit){
			// execute the insert
			pushBulkNodeTags();

			// reset the buffer size
			insertNodeTagBufferSize=0;
		}
		else{
			// if this wans't the final node in the buffer, add a newline
			bulkInsertNodeTagBuilder.append("\n");
		}
	}

	public void addWay(ShallowWay way) throws SQLException{
		busy.set(true);

		// if this is the first way in this buffer create a new StringBuilder
		if(insertWayBufferSize==0) bulkInsertWayBuilder = new StringBuilder();

		bulkInsertWayBuilder.append(way.getId());
		bulkInsertWayBuilder.append(BULK_DELIMITER);
		bulkInsertWayBuilder.append((way.getTags()==null)?"NULL":createTagString(way.getTags()));

		insertWayBufferSize++;

		if(insertWayBufferSize==(bufferSizeLimit*smallTableMultiple)){
			// execute the insert
			pushBulkWays();

			// reset the buffer size
			insertWayBufferSize=0;
		}
		else{
			// if this wans't the final way in the buffer, add a newline
			bulkInsertWayBuilder.append("\n");
		}

		for(long reference : way.getNodeReferences()){
			addWayMember(way.getId(), reference);
		}
		
		// do nothing if there are no tags
		if(way.getTags()!=null){
			for(AbstractTag tag : way.getTags()){
				addWayTag(way.getId(), tag);
			}
		}

		busy.set(false);
	}
	
	private void addWayTag(long wayID, AbstractTag tag){

		if(insertWayTagBufferSize==0) bulkInsertWayTagBuilder = new StringBuilder();

		bulkInsertWayTagBuilder.append(wayID);
		bulkInsertWayTagBuilder.append(BULK_DELIMITER);
		bulkInsertWayTagBuilder.append(tag.getKey());
		bulkInsertWayTagBuilder.append(BULK_DELIMITER);
		bulkInsertWayTagBuilder.append(tag.getValue());

		insertWayTagBufferSize++;

		if(insertWayTagBufferSize==bufferSizeLimit){
			// execute the insert
			pushBulkWayTags();

			// reset the buffer size
			insertWayTagBufferSize=0;
		}
		else{
			// if this wans't the final way in the buffer, add a newline
			bulkInsertWayTagBuilder.append("\n");
		}
	}

	public void addRelation(Relation relation) throws SQLException{
		busy.set(true);

		// if this is the first relation in this buffer create a new StringBuilder
		if(insertRelationBufferSize==0) bulkInsertRelationBuilder = new StringBuilder();

		bulkInsertRelationBuilder.append(relation.getId());
		bulkInsertRelationBuilder.append(BULK_DELIMITER);
		bulkInsertRelationBuilder.append((relation.getTags()==null)?"NULL":createTagString(relation.getTags()));

		insertRelationBufferSize++;

		if(insertRelationBufferSize==(bufferSizeLimit*smallTableMultiple)){
			// execute the insert
			pushBulkRelations();

			// reset the buffer size
			insertRelationBufferSize=0;
		}
		else{
			// if this wans't the final relation in the buffer, add a newline
			bulkInsertRelationBuilder.append("\n");
		}

		for(RelationMemeber rm : relation.getMemebers()){
			addRelationMember(relation.getId(), rm);
		}
		
		// do nothing if there are no tags
		if(relation.getTags()!=null){
			for(AbstractTag tag : relation.getTags()){
				addRelationTag(relation.getId(), tag);
			}
		}

		busy.set(false);
	}
	
	private void addRelationTag(long relationID, AbstractTag tag){

		if(insertRelationTagBufferSize==0) bulkInsertRelationTagBuilder = new StringBuilder();

		bulkInsertRelationTagBuilder.append(relationID);
		bulkInsertRelationTagBuilder.append(BULK_DELIMITER);
		bulkInsertRelationTagBuilder.append(tag.getKey());
		bulkInsertRelationTagBuilder.append(BULK_DELIMITER);
		bulkInsertRelationTagBuilder.append(tag.getValue());

		insertRelationTagBufferSize++;

		if(insertRelationTagBufferSize==bufferSizeLimit){
			// execute the insert
			pushBulkRelationTags();

			// reset the buffer size
			insertRelationTagBufferSize=0;
		}
		else{
			// if this wans't the final Relation in the buffer, add a newline
			bulkInsertRelationTagBuilder.append("\n");
		}
	}

	private void addRelationMember(long relationID, RelationMemeber rm) throws SQLException{
		// if this is the first relation member in this buffer create a new StringBuilder
		if(insertRelationMemberBufferSize==0) bulkInsertRelationMemberBuilder = new StringBuilder();

		bulkInsertRelationMemberBuilder.append(relationID);
		bulkInsertRelationMemberBuilder.append(BULK_DELIMITER);
		bulkInsertRelationMemberBuilder.append(rm.getObjectReference().getId());
		bulkInsertRelationMemberBuilder.append(BULK_DELIMITER);
		bulkInsertRelationMemberBuilder.append(rm.getRole());

		insertRelationMemberBufferSize++;

		if(insertRelationMemberBufferSize==(bufferSizeLimit*smallTableMultiple)){
			// execute the insert
			pushBulkRelationMembers();

			// reset the buffer size
			insertRelationMemberBufferSize=0;
		}
		else{
			// if this wans't the final relation member in the buffer, add a newline
			bulkInsertRelationMemberBuilder.append("\n");
		}
	}

	private void addWayMember(long wayID, long nodeID) throws SQLException{
		// if this is the first way member in this buffer create a new StringBuilder
		if(insertWayMemberBufferSize==0) bulkInsertWayMemberBuilder = new StringBuilder();

		bulkInsertWayMemberBuilder.append(wayID);
		bulkInsertWayMemberBuilder.append(BULK_DELIMITER);
		bulkInsertWayMemberBuilder.append(nodeID);

		insertWayMemberBufferSize++;

		if(insertWayMemberBufferSize==(bufferSizeLimit*smallTableMultiple)){
			// execute the insert
			pushBulkWayMembers();

			// reset the buffer size
			insertWayMemberBufferSize=0;
		}
		else{
			// if this wans't the final way member in the buffer, add a newline
			bulkInsertWayMemberBuilder.append("\n");
		}
	}

	private String createTagString(List<AbstractTag> tags){
		// build the tag string
		StringBuilder tagString = new StringBuilder();
		//System.out.println("there are "+tags.size()+" tags");
		for(AbstractTag tag : tags){
			// if this is not the first tag, add a comma to separate it from the last pair
			if(tagString.length()>0) tagString.append(",");

			tagString.append(tag.getKey()+","+tag.getValue());
		}

		return tagString.toString();
	}

	public synchronized boolean isBusy(){
		return busy.get();
	}

	/**
	 * Closes the connection with the database
	 */
	public void closeConnection() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection(){
		return con;
	}

}
