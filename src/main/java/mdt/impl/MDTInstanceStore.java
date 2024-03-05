package mdt.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.CSV;
import utils.Throwables;
import utils.jdbc.JdbcProcessor;
import utils.jdbc.JdbcProcessor.JdbcConsumer;
import utils.jdbc.JdbcRowSource;
import utils.stream.FStream;

/**
 *
 * @author Kang-Woo Lee (ETRI)
 */
public class MDTInstanceStore  {
	private static final Logger s_logger
			= LoggerFactory.getLogger(MDTInstanceStore.class.getPackage().getName() + ".store");
	
	public static class Record {
		private final String m_instanceId;
		private final String m_tag;
		private final String m_aasId;
		private final List<String> m_subModelIdList;
		private final String m_serviceEndpoint;
		
		public Record(String instanceId, String tag, String aasId, List<String> submodelIdList, String svcEp) {
			m_instanceId = instanceId;
			m_tag = tag;
			m_aasId = aasId;
			m_subModelIdList = submodelIdList;
			m_serviceEndpoint = svcEp;
		}
		
		public String getInstanceId() {
			return m_instanceId;
		}
		
		public String getTag() {
			return m_tag;
		}
		
		public String getAASId() {
			return m_aasId;
		}
		
		public List<String> getSubModelIdList() {
			return m_subModelIdList;
		}
		
		public String getServiceEndpoint() {
			return m_serviceEndpoint;
		}
	}

	private final JdbcProcessor m_jdbc;

	public MDTInstanceStore(JdbcProcessor jdbc) {
		m_jdbc = jdbc;
	}
	
	public List<Record> getRecordAll() throws SQLException {
		String sqlStr = "select * from  mdt_instances";
		return JdbcRowSource.select(this::fromResultSet)
							.from(m_jdbc)
							.executeQuery(sqlStr)
							.fstream()
							.toList();
	}

	public Record getRecordByImageId(String imageId) throws SQLException {
		try ( Connection conn = m_jdbc.connect() ) {
			String sqlStr = String.format("select * from mdt_instances where instance_id = '%s'", imageId);
			return readRecord(conn, sqlStr);
		}
	}

	public Record getRecordByAASId(String aasId) throws SQLException {
		try ( Connection conn = m_jdbc.connect() ) {
			String sqlStr = String.format("select * from mdt_instances where aas_id = '%s'", aasId);
			return readRecord(conn, sqlStr);
		}
	}

	private Record readRecord(Connection conn, String sqlStr) throws SQLException {
		ResultSet rs = m_jdbc.executeQuery(sqlStr);
		if ( rs.next() ) {
			return fromResultSet(rs);
		}
		else {
			return null;
		}
	}
	
	public void addRecord(Record record) throws SQLException {
		String insertSql = String.format(
			"insert into %s(instance_id, tag, aas_id, submodels, service_endpoint) "
			+ "values (?,?,?,?,?)", TABLE_MDT_INSTANCES);
		try {
			m_jdbc.executeUpdate(insertSql, new JdbcConsumer<PreparedStatement>() {
				@Override
				public void accept(PreparedStatement pstmt) throws SQLException {
					pstmt.setString(1, record.m_instanceId);
					pstmt.setString(2, record.m_tag);
					pstmt.setString(3, record.m_aasId);
					pstmt.setString(4, FStream.from(record.m_subModelIdList).join(','));
					pstmt.setString(5, record.m_serviceEndpoint);
				}
			});
		}
		catch ( ExecutionException e ) {
			throw Throwables.toRuntimeException(Throwables.unwrapThrowable(e));
		}
	}
	
	public void addOrReplaceRecord(Record record) throws SQLException {
		String insertOrUpdateSql
			= "insert into " + TABLE_MDT_INSTANCES
				+ "(instance_id, tag, aas_id, submodels, service_endpoint) "
				+ "values (?, ?, ?, ?, ?) "
				+ "on conflict(instance_id) do update "
				+ "set tag = ?, "
					+ "aas_id = ?, "
					+ "submodels = ?, "
					+ "service_endpoint = ?";
		try {
			m_jdbc.executeUpdate(insertOrUpdateSql, new JdbcConsumer<PreparedStatement>() {
				@Override
				public void accept(PreparedStatement pstmt) throws SQLException {
					String subModelsCsv = FStream.from(record.m_subModelIdList).join(',');
					pstmt.setString(1, record.m_instanceId);
					pstmt.setString(2, record.m_tag);
					pstmt.setString(3, record.m_aasId);
					pstmt.setString(4, subModelsCsv);
					pstmt.setString(5, record.m_serviceEndpoint);
					pstmt.setString(6, record.m_tag);
					pstmt.setString(7, record.m_aasId);
					pstmt.setString(8, subModelsCsv);
					pstmt.setString(9, record.m_serviceEndpoint);
				}
			});
		}
		catch ( ExecutionException e ) {
			throw Throwables.toRuntimeException(Throwables.unwrapThrowable(e));
		}
	}
	
	public void updateRecord(Record record) throws SQLException {
		String updateSql = "update " +  TABLE_MDT_INSTANCES + " "
							+ "set tag=?, aas_id=?, submodels=?, service_endpoint=? "
							+ "where instance_id=?";
		try {
			m_jdbc.executeUpdate(updateSql, new JdbcConsumer<PreparedStatement>() {
				@Override
				public void accept(PreparedStatement pstmt) throws SQLException {
					pstmt.setString(1, record.m_tag);
					pstmt.setString(2, record.m_aasId);
					pstmt.setString(3, FStream.from(record.m_subModelIdList).join(','));
					pstmt.setString(4, record.m_serviceEndpoint);
					pstmt.setString(5, record.m_instanceId);
				}
			});
		}
		catch ( ExecutionException e ) {
			throw Throwables.toRuntimeException(Throwables.unwrapThrowable(e));
		}
	}
	
	public void deleteRecord(String instanceId) throws SQLException {
		String deleteSql = String.format("delete from %s where instance_id='%s'",
										TABLE_MDT_INSTANCES, instanceId);
		m_jdbc.executeUpdate(deleteSql);
	}
	
	private Record fromResultSet(ResultSet rs) throws SQLException {
		String instanceId = rs.getString(1);
		String tag = rs.getString(2);
		String aasId = rs.getString(3);
		List<String> subModelIdList = CSV.parseCsv(rs.getString(4)).toList();
		String svcEndPoint = rs.getString(5);
		
		return new Record(instanceId, tag, aasId, subModelIdList, svcEndPoint);
	}

	public static void createTable(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(SQL_CREATE_TABLE_MDT_INSTANCES);
			stmt.executeUpdate(SQL_CREATE_INDEX_MDT_INSTANCES);
		}
	}

	public static void dropTable(Connection conn) throws SQLException {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("drop table if exists " + TABLE_MDT_INSTANCES);
		}
	}

	private static final String TABLE_MDT_INSTANCES = "mdt_instances";

	private static final String SQL_CREATE_TABLE_MDT_INSTANCES
		= "create table " + TABLE_MDT_INSTANCES + " ("
			+ "instance_id varchar not null, " 	// 1
			+ "tag varchar not null, " 			// 2
			+ "aas_id varchar not null, " 		// 3
			+ "submodels varchar not null, " 	// 4
			+ "service_endpoint varchar, " 		// 5
			+ "primary key (instance_id)" + ")";

	private static final String SQL_CREATE_INDEX_MDT_INSTANCES
		= "create index " + TABLE_MDT_INSTANCES + "_aas_idx on " + TABLE_MDT_INSTANCES + "(aas_id)";
}
