package mdt.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import utils.func.KeyValue;
import utils.jdbc.JdbcProcessor;
import utils.stream.FStream;
import utils.stream.Generator;

import mdt.model.EnvironmentSummary;
import mdt.model.IdPair;
import mdt.model.MDTInstanceManagerException;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceStore  {
	private static final Logger s_logger
			= LoggerFactory.getLogger(MDTInstanceStore.class.getPackage().getName() + ".store");
	
	public static class Record {
		private final String m_instanceId;
		private final String m_imageId;
		private final EnvironmentSummary m_envSummary;
		private String m_serviceEndpoint;
		
		public Record(String instanceId, String imageId, EnvironmentSummary envSummary, String svcEp) {
			m_instanceId = instanceId;
			m_imageId = imageId;
			m_envSummary = envSummary;
			m_serviceEndpoint = svcEp;
		}
		
		public String getInstanceId() {
			return m_instanceId;
		}
		
		public String getImageId() {
			return m_imageId;
		}
		
		public EnvironmentSummary getEnvironmentSummary() {
			return m_envSummary;
		}
		
		public String getServiceEndpoint() {
			return m_serviceEndpoint;
		}
		
		public void setServiceEndpoint(String endpoint) {
			m_serviceEndpoint = endpoint;
		}
	}

	private final JdbcProcessor m_jdbc;

	public MDTInstanceStore(JdbcProcessor jdbc) {
		m_jdbc = jdbc;
	}
	
	public JdbcProcessor getJdbcProcessor() {
		return m_jdbc;
	}
	
	public List<Record> getRecordAll() throws SQLException {
		try ( Connection conn = m_jdbc.connect();
				Statement stmt = conn.createStatement(); ) {
			ResultSet rs =  stmt.executeQuery(SQL_SELECT_ALL);
			return streamResultSet(rs).toList();
		}
	}

	public Record getRecordByInstanceId(String instId) throws SQLException {
		try ( Connection conn = m_jdbc.connect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_INST_ID); ) {
			pstmt.setString(1, instId);
			ResultSet rs =  pstmt.executeQuery();
			return streamResultSet(rs).findFirst().getOrNull();
		}
	}

	private Record readRecord(Connection conn, String sqlStr) throws SQLException {
		try ( ResultSet rs = m_jdbc.executeQuery(sqlStr, false) ) {
			if ( !rs.next() ) {
				return null;
			}
			
			int instKey = rs.getInt(1);
			List<IdPair> submodelIdList = Lists.newArrayList();
			String instSql = String.format("select submodel_id, submodel_id_short from %s where instance_pkey=%d",
											TABLE_MDT_SUBMODELS, instKey);
			try ( ResultSet rs2 = m_jdbc.executeQuery(instSql, false) ) {
				while ( rs2.next() ) {
					submodelIdList.add(IdPair.of(rs2.getString(1), rs2.getString(2)));
				}
			}
			
			String instanceId = rs.getString(2);
			String imageId = rs.getString(3);
			String aasId = rs.getString(4);
			String aasIdShort = rs.getString(5);
			String svcEndPoint = rs.getString(6);
			
			EnvironmentSummary summary = new EnvironmentSummary(IdPair.of(aasId, aasIdShort),
																submodelIdList);
			return new Record(instanceId, imageId, summary, svcEndPoint);
		}
	}
	
	public void addRecord(Record record) throws SQLException {
		String insertInstance
			= "insert into " + TABLE_MDT_INSTANCES
				+ "(instance_id, image_id, aas_id, aas_id_short, service_endpoint) "
				+ "values (?, ?, ?, ?, ?) returning pkey";
		
		String insertSubmodel
			= "insert into " + TABLE_MDT_SUBMODELS
				+ "(instance_pkey, submodel_id, submodel_id_short) "
				+ "values (?, ?, ?)";
		
		EnvironmentSummary envSummary = record.getEnvironmentSummary();
		try ( Connection conn = m_jdbc.connect() ) {
			int instanceKey = -1;
			try ( PreparedStatement pstmt = conn.prepareStatement(insertInstance); ) {
				pstmt.setString(1, record.m_instanceId);
				pstmt.setString(2, record.m_imageId);
				pstmt.setString(3, envSummary.getAASId().getId());
				pstmt.setString(4, envSummary.getAASId().getIdShort());
				pstmt.setString(5, record.m_serviceEndpoint);
				pstmt.execute();
				
				ResultSet createdId = pstmt.getResultSet();
				if ( createdId.next() ) {
					instanceKey = createdId.getInt(1);
				}
				else {
					throw new MDTInstanceManagerException("cannot get instance pkey");
				}
			}
			
			try ( PreparedStatement pstmt = conn.prepareStatement(insertSubmodel); ) {
				for ( IdPair pair: envSummary.getSubmodelIdList() ) {
					pstmt.setInt(1, instanceKey);
					pstmt.setString(2, pair.getId());
					pstmt.setString(3, pair.getIdShort());
					pstmt.execute();
				}
			}
		}
	}
	
	public void updateEndpoint(String id, String endpoint) throws SQLException {
		String updateSql = "update " + TABLE_MDT_INSTANCES
							+ " set service_endpoint=? "
							+ "where instance_id=?";
		try ( Connection conn = m_jdbc.connect();
			PreparedStatement pstmt = conn.prepareStatement(updateSql); ) {
			pstmt.setString(1, endpoint);
			pstmt.setString(2, id);
			pstmt.execute();
		}
	}
	
	public void deleteRecord(String instanceId) throws SQLException {
		String deleteSql = String.format("delete from %s where instance_id='%s'",
										TABLE_MDT_INSTANCES, instanceId);
		m_jdbc.executeUpdate(deleteSql);
	}
	
	public void deleteRecordAll() throws SQLException {
		String deleteSql = "delete from " + TABLE_MDT_INSTANCES;
		m_jdbc.executeUpdate(deleteSql);
	}

	public static void createTable(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(SQL_CREATE_TABLE_MDT_INSTANCES);
			stmt.executeUpdate(SQL_CREATE_INDEX_MDT_INSTANCES);
 			stmt.executeUpdate(SQL_CREATE_TABLE_MDT_SUBMODELS);
		}
	}

	public static void dropTable(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("drop table if exists " + TABLE_MDT_SUBMODELS);
			stmt.executeUpdate("drop table if exists " + TABLE_MDT_INSTANCES);
		}
	}

	private static final String TABLE_MDT_INSTANCES = "mdt_instances";
	private static final String TABLE_MDT_SUBMODELS = "mdt_submodels";

	private static final String SQL_CREATE_TABLE_MDT_INSTANCES
		= "create table " + TABLE_MDT_INSTANCES + " ("
			+ "pkey int generated by default as identity, " 	// 1
			+ "instance_id varchar not null, " 		// 2
			+ "image_id varchar not null, " 			// 3
			+ "aas_id varchar not null, " 			// 4
			+ "aas_id_short varchar, " 				// 5
			+ "service_endpoint varchar, " 			// 6
			+ "primary key (pkey), "
			+ "constraint unique_instance_id unique(instance_id)"
			+ ")";

	private static final String SQL_CREATE_INDEX_MDT_INSTANCES
		= "create index " + TABLE_MDT_INSTANCES + "_aas_idx on " + TABLE_MDT_INSTANCES + "(aas_id)";

	private static final String SQL_CREATE_TABLE_MDT_SUBMODELS
		= "create table " + TABLE_MDT_SUBMODELS + " ("
			+ "instance_pkey INT not null, " 	// 1
			+ "submodel_id varchar not null, " 	// 2
			+ "submodel_id_short varchar, " 	// 3
			+ "primary key (submodel_id), "
			+ "constraint fk_instance_pkey "
				+ "foreign key(instance_pkey) "
					+ "references " + TABLE_MDT_INSTANCES + "(pkey) "
					+ "on delete cascade"
			+ ")";
	
	private static final String SQL_SELECT_ALL = String.format(
		"select pkey, instance_id, image_id, aas_id, aas_id_short, service_endpoint, "
		+ "submodel_id, submodel_id_short "
		+ "from %s as i, %s as s "
		+ "where i.pkey = s.instance_pkey "
		+ "order by pkey",
		TABLE_MDT_INSTANCES, TABLE_MDT_SUBMODELS);
	
	private static final String SQL_SELECT_BY_INST_ID = String.format(
			"select pkey, instance_id, image_id, aas_id, aas_id_short, service_endpoint, "
			+ "submodel_id, submodel_id_short "
			+ "from %s as i, %s as s "
			+ "where i.pkey = s.instance_pkey and i.instance_id = ?",
			TABLE_MDT_INSTANCES, TABLE_MDT_SUBMODELS);
	
	private KeyValue<Integer, Object[]> readARow(ResultSet rs) throws SQLException {
		Object[] values = new Object[7];
		for ( int i =2; i <= 8; ++i ) {
			values[i-2] = rs.getObject(i);
		}
		return KeyValue.of(rs.getInt(1), values);
	}
	
	private Record toRecord(int pkey, List<Object[]> group) {
		Object[] first = group.get(0);
		String instanceId = (String)first[0];
		String imageId = (String)first[1];
		IdPair aasIdPair = IdPair.of((String)first[2], (String)first[3]);
		String svcEndPoint = (String)first[4];
		
		List<IdPair> submodeIdList = Lists.newArrayListWithExpectedSize(group.size());
		for ( Object[] member: group ) {
			submodeIdList.add(IdPair.of((String)member[5], (String)member[6]));
		}
		
		EnvironmentSummary summary = new EnvironmentSummary(aasIdPair, submodeIdList);
		return new Record(instanceId, imageId, summary, svcEndPoint);
	}
	
	private FStream<Record> streamResultSet(ResultSet rs) throws SQLException {
		return new Generator<Record>(5) {
			@Override
			public void run() throws Throwable {
				int lastKey = -1;
				List<Object[]> group = Lists.newArrayList();
				while ( rs.next() ) {
					KeyValue<Integer,Object[]> kvalue = readARow(rs);
					if ( group.isEmpty() ) {
						lastKey = kvalue.key();
					}
					if ( lastKey == kvalue.key() ) {
						group.add(kvalue.value());
					}
					else {
						Record rec = toRecord(lastKey, group);
						lastKey = kvalue.key();
						
						this.yield(rec);
						group.clear();
						group.add(kvalue.value());
					}
				}
				if ( group.size() > 0 ) {
					Record rec = toRecord(lastKey, group);
					this.yield(rec);
				}
			}
		};
	}
		
		
}
