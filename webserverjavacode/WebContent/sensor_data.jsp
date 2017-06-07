<%-- <%@page import="org.apache.jasper.tagplugins.jstl.core.Out"%> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	`
	<%@ page import="java.util.*"%>
	<%@ page import="java.sql.*"%>
	<%@ page import="javax.sql.*"%>
	<%@ page import="java.lang.*"%>
	<%@ page import="java.util.ArrayList"%>
	<%
		float temperature = 0, pressure = 0, soil_moisture = 0,running_time=10;


		//code for updating the records
		// Incorporate MySQL driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			// Connect to the test database
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://ec2-54-193-70-40.us-west-1.compute.amazonaws.com:3306/weatherdb?autoReconnect=true&useSSL=false", "root", "root");
			Statement stmt = connection.createStatement();
			// prepare SQL statement template that's to be repeatedly excuted
			String string_update_sensors = "UPDATE sensor SET temperature = ?, pressure=?,soil_moisture=?,running_time=? WHERE id = 1";
			String retrieve_sensor_values = "select * from sensor limit 1";
			ResultSet rs_old = stmt.executeQuery(retrieve_sensor_values);
			if (rs_old.next()) {
				temperature   = rs_old.getFloat(2);
				pressure 	  = rs_old.getFloat(3);
				soil_moisture = rs_old.getFloat(4);
				running_time  = rs_old.getFloat(5);
			}
			if (request.getParameter("temperature") != null)
				temperature = Float.valueOf(request.getParameter("temperature"));

			if (request.getParameter("pressure") != null)
				pressure = Float.valueOf(request.getParameter("pressure"));

			if (request.getParameter("soil_moisture") != null)
				soil_moisture = Float.valueOf(request.getParameter("soil_moisture"));
			
			if (request.getParameter("running_time") != null)
				running_time = Float.valueOf(request.getParameter("running_time"));
			
			if (temperature != 0 || pressure != 0 || soil_moisture != 0) {
				PreparedStatement prepare_update_sensor_data = connection.prepareStatement(string_update_sensors);
			
			
				prepare_update_sensor_data.setFloat(1, temperature);
				prepare_update_sensor_data.setFloat(2, pressure);
				prepare_update_sensor_data.setFloat(3, soil_moisture);
				prepare_update_sensor_data.setFloat(4, running_time);
				prepare_update_sensor_data.executeUpdate();
			}
			ResultSet rs = stmt.executeQuery(retrieve_sensor_values);
			if (rs.next()) {
				//code for updating the records
/* 				out.println("Went inside");
				out.println("<br>"); */
				out.println("Temperature : " + rs.getFloat(2));
				out.println("<br>");
				out.println("Pressure : " + rs.getFloat(3));
				out.println("<br>");
				out.println("Soil Moisture : " + rs.getFloat(4));
				out.println("<br>");
				out.println("Sprinkler Run Time : " + rs.getFloat(5));%>
	<form method="get" action="sensor_data.jsp">
		<input type="text" name="running_time" value=<%=rs.getFloat(5)%>>
		<%-- <a href=sensor_data.jsp?temperature=<%=temperature%>&pressure=<%=pressure%>&running_time=<%=running_time%>> --%>
		<input type="submit" value="update">
		</tr>
		<!-- </a> -->
	</form>

	<%
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	%>

</body>
</html>
"
