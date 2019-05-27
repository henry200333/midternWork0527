package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class WeatherReportDAO  {

	DataSource dataSource = null;

	public WeatherReportDAO() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/WeatherSQLserver");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	String INSERT = "insert into WeatherReport values (?,?,?,?,?,?,?,?,?)";
	String DELETE = "delete from WeatherReport where locationName=? and dateTime=?";
	String UPDATE = "update WeatherReport set geocode=?, lat=?, lon=?, elementName=?, description=?, "
			+ "value=?, measures=? where locationName=? and dateTime=?";

	String GET_ONE = "select * from WeatherReport where locationName=? and dateTime=?";
	String GET_ALL = "select * from WeatherReport";
	String GET_LOCATION = "select distinct locationName from WeatherReport";
	String GET_LATNLON = "select top 1 * from weatherReport where locationName =?";
	String GET_BY_LOCATION = "select * from WeatherReport where locationName =?";

	
	public int insert(WeatherReportBean wr) throws SQLException {
		int insert_Count = 0;
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT);) {

			pstmt.setString(1, wr.getLacationName());
			pstmt.setInt(2, wr.getGeocode());
			pstmt.setDouble(3, wr.getLat());
			pstmt.setDouble(4, wr.getLon());
			pstmt.setObject(5, wr.getElementName());
			pstmt.setString(6, wr.getDescription());
			pstmt.setTimestamp(7, (Timestamp) wr.getDateTime());
			pstmt.setShort(8, wr.getValue());
			pstmt.setString(9, wr.getMeasures());
			insert_Count = pstmt.executeUpdate();

		} 
		return insert_Count;
	}

	
	public int delete(String locationName, Timestamp dateTime) throws SQLException {
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(DELETE);) {

			pstmt.setString(1, locationName);
			pstmt.setTimestamp(2, dateTime);
			int delete_Count = pstmt.executeUpdate();
			return delete_Count;
		}
	}

	
	public int update(WeatherReportBean wr) throws SQLException {
		int update_Count = 0;
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(UPDATE);) {

			pstmt.setString(8, wr.getLacationName());
			pstmt.setInt(1, wr.getGeocode());
			pstmt.setDouble(2, wr.getLat());
			pstmt.setDouble(3, wr.getLon());
			pstmt.setObject(4, wr.getElementName());
			pstmt.setString(5, wr.getDescription());
			pstmt.setTimestamp(9, (Timestamp) wr.getDateTime());
			pstmt.setShort(6, wr.getValue());
			pstmt.setString(7, wr.getMeasures());

			update_Count = pstmt.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return update_Count;
	}

	
	public WeatherReportBean findByLocationameNDatetime(String locationName, Timestamp dateTime) throws SQLException {

		WeatherReportBean outcome = null;
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(GET_ONE);) {

			pstmt.setString(1, locationName);
			pstmt.setTimestamp(2, dateTime);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				outcome = new WeatherReportBean();
				outcome.setLacationName(rs.getString(1));
				outcome.setGeocode(rs.getInt(2));
				outcome.setLat(rs.getDouble(3));
				outcome.setLon(rs.getDouble(4));
				outcome.setElementName(rs.getString(5));
				outcome.setDescription(rs.getString(6));
				outcome.setDateTime(rs.getTimestamp(7));
				outcome.setValue(rs.getShort(8));
				outcome.setMeasures(rs.getString(9));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outcome;
	}

	
	public List<WeatherReportBean> getAll() throws SQLException {
		List<WeatherReportBean> outcomelist = new ArrayList<>();
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(GET_ALL);) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				WeatherReportBean outcome = new WeatherReportBean();
				outcome.setLacationName(rs.getString(1));
				outcome.setGeocode(rs.getInt(2));
				outcome.setLat(rs.getDouble(3));
				outcome.setLon(rs.getDouble(4));
				outcome.setElementName(rs.getString(5));
				outcome.setDescription(rs.getString(6));
				outcome.setDateTime(rs.getTimestamp(7));
				outcome.setValue(rs.getShort(8));
				outcome.setMeasures(rs.getString(9));

				outcomelist.add(outcome);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outcomelist;
	}

	
	public List<String> getLocationName() throws SQLException {
		List<String> locationList = new ArrayList<String>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(GET_LOCATION);) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				locationList.add(rs.getString(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return locationList;
	}

	public WeatherReportBean getLatNLon(String location) {
		model.WeatherReportBean bean = new WeatherReportBean();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(GET_LATNLON);) {
			pstmt.setString(1, location);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				bean.setLacationName(rs.getString(1));
				bean.setGeocode(rs.getInt(2));
				bean.setLat(rs.getDouble(3));
				bean.setLon(rs.getDouble(4));
				bean.setElementName(rs.getString(5));
				;
				bean.setDescription(rs.getString(6));
				bean.setMeasures(rs.getString(9));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bean;
	}

	public List<WeatherReportBean> getByLocation(String location) throws SQLException {
		List<WeatherReportBean> outcomelist = new ArrayList<>();
		try (Connection conn = dataSource.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(GET_BY_LOCATION);) {

			pstmt.setString(1, location);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				WeatherReportBean outcome = new WeatherReportBean();
				outcome.setLacationName(rs.getString(1));
				outcome.setGeocode(rs.getInt(2));
				outcome.setLat(rs.getDouble(3));
				outcome.setLon(rs.getDouble(4));
				outcome.setElementName(rs.getString(5));
				outcome.setDescription(rs.getString(6));
				outcome.setDateTime(rs.getTimestamp(7));
				outcome.setValue(rs.getShort(8));
				outcome.setMeasures(rs.getString(9));

				outcomelist.add(outcome);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return outcomelist;
	}

}
