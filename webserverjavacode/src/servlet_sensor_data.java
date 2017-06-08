

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class servlet_sensor_data
 */
@WebServlet("/servlet_sensor_data")
public class servlet_sensor_data extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public servlet_sensor_data() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {

			ServletInputStream sin = request.getInputStream();
			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String user_name = "", pass_word = "",empty_string="", send_output="";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			sin.close();
			String string_search = sb.toString().trim();


			Class.forName("com.mysql.jdbc.Driver");
			Connection test_connection = null;
			test_connection = (Connection) DriverManager
					.getConnection("jdbc:mysql:///weatherdb?autoReconnect=true&useSSL=false", "root", "root");

			if (test_connection == null) {
				System.out.println("Not successful");
				System.out.println("Connection not successfull");
			} else {
				System.out.println("Connection Successfull");
			}
			if(!string_search.equals(empty_string)){
			Statement stmt = test_connection.createStatement();
			String search_query = "select * from sensor limit 1;";
			String string_update_sensors = "UPDATE sensor SET running_time=? WHERE id = 1";
			
			if(!string_search.equals(empty_string)){
				PreparedStatement prepare_update_sensor_data = test_connection.prepareStatement(string_update_sensors);
				
				
				prepare_update_sensor_data.setFloat(1, Float.parseFloat(string_search));
				prepare_update_sensor_data.executeUpdate();
			}
			ResultSet rs = stmt.executeQuery(search_query);
			ArrayList<String> list_movies = new ArrayList<String>();
			StringBuilder sb_movies = new StringBuilder();
			
			while (rs.next()) {
				send_output = send_output+rs.getFloat(5) +",";
				sb_movies.append(rs.getFloat(5));
				
				sb_movies.append(",");
				//list_movies.add(rs.getFloat(5));
			}
			}
				response.setStatus(HttpServletResponse.SC_OK);
				OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
				if(!send_output.equals(empty_string))
				writer.write(send_output);
				else writer.write("132,");
				writer.flush();
				writer.close();
//				String json = new Gson().toJson(list_movies);
//
//			    response.setContentType("application/json");
//			    response.setCharacterEncoding("UTF-8");
//			    response.getWriter().write(json);
			
		} catch (IOException e) {

			try {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().print(e.getMessage());
				response.getWriter().close();
			} catch (IOException ioe) {
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//doGet(request, response);
	}

}
