
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.aksingh.owmjapis.OpenWeatherMap;

import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;

public class writeto_aws_database {

	public static void main(String[] args) {
		int row_count = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection test_connection = null;
			    test_connection = DriverManager.getConnection("jdbc:mysql://" + "ec2-54-193-70-40.us-west-1.compute.amazonaws.com" + ":" + "3306" + "/" + "weatherdb", "root", "root");

			FileInputStream fie_input_stream = new FileInputStream(
					new File("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx"));
			// Access the workbook
			XSSFWorkbook workbook = new XSSFWorkbook(fie_input_stream);
			// Access the worksheet, so that we can update / modify it.
			XSSFSheet worksheet = workbook.getSheet("Current Weather");
			row_count = worksheet.getLastRowNum();

			XSSFCell cell_value = workbook.getSheetAt(0).getRow(row_count).getCell(15);
			System.out.println("Cell Value : " + cell_value);
			final DataFormatter df = new DataFormatter();
			String valueAsString = df.formatCellValue(cell_value);

			float final_cell_value = Float.parseFloat(valueAsString);
			String string_update_sensors = "UPDATE sensor SET running_time=? WHERE id = 1";

			PreparedStatement prepare_update_sensor_data = test_connection.prepareStatement(string_update_sensors);

			prepare_update_sensor_data.setFloat(1, final_cell_value);
			prepare_update_sensor_data.executeUpdate();
			test_connection.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}
}
		  