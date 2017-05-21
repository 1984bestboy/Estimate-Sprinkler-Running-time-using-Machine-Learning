
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.aksingh.owmjapis.OpenWeatherMap;

import org.w3c.dom.Element;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class getWeatherData {

	public static void main(String[] args) {
		String excel_weather_filepath = "/Users/balaji/Documents/Github/IOT/Weather/src/package_iot/newFile.xlsx";
		boolean isMetric = true;
		String owmApiKey = "5565b5455185c3b4f75b4e906831705c";
		String weatherCity = "Irvine,US", forecast_precipitation = null;
		byte forecastDays = 3;
		int column_count = 0;
		OpenWeatherMap.Units units = (isMetric) ? OpenWeatherMap.Units.METRIC : OpenWeatherMap.Units.IMPERIAL;
		System.out.println("Units : " + units);
		OpenWeatherMap owm = new OpenWeatherMap(units, owmApiKey);

		try {
			String current_weather_url = "http://api.openweathermap.org/data/2.5/weather?q=irvine&units=metric&appid=5565b5455185c3b4f75b4e906831705c&mode=xml";
			String forecast_weather_url = "http://api.openweathermap.org/data/2.5/forecast?q=IRVINE,US&mode=xml&units=metric&cnt=1&appid=5565b5455185c3b4f75b4e906831705c";

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			f.setNamespaceAware(false);
			f.setValidating(false);
			DocumentBuilder builder = f.newDocumentBuilder();

			URLConnection current_urlConnection = new URL(current_weather_url).openConnection();
			URLConnection forecast_urlConnection = new URL(forecast_weather_url).openConnection();

			current_urlConnection.addRequestProperty("Accept", "application/xml");
			forecast_urlConnection.addRequestProperty("Accept", "application/xml");

			// Parse the forecast data
			Document document_forecast = builder.parse(forecast_urlConnection.getInputStream());
			document_forecast.getDocumentElement().normalize();

			NodeList list_forecast_weatherdata = document_forecast.getElementsByTagName("weatherdata");
			Element element_forecast_weatherdata = (Element) list_forecast_weatherdata.item(0);

			NodeList list_forecast_data = element_forecast_weatherdata.getElementsByTagName("forecast");
			Element element_forecast = (Element) list_forecast_data.item(0);

			NodeList list_forecast_time = element_forecast_weatherdata.getElementsByTagName("time");
			Element element_forecast_time = (Element) list_forecast_data.item(0);
			
			String forecast_time_from = list_forecast_time.item(0).getAttributes().getNamedItem("from").getNodeValue();
			
			String forecast_time_to =list_forecast_time.item(0).getAttributes().getNamedItem("to").getNodeValue() ;

			NodeList List_forecast_temperature = element_forecast_time.getElementsByTagName("temperature");
			Element element_forecast_temperature = (Element) List_forecast_temperature.item(0);

			NodeList List_forecast_pressure = element_forecast_time.getElementsByTagName("pressure");
			Element element_forecast_pressure = (Element) List_forecast_pressure.item(0);

			NodeList List_forecast_humidity = element_forecast_time.getElementsByTagName("humidity");
			Element element_forecast_humidity = (Element) List_forecast_humidity.item(0);

			NodeList List_forecast_precipitation = element_forecast_time.getElementsByTagName("precipitation");
			Element element_forecast_precipitation = (Element) List_forecast_precipitation.item(0);

			NodeList List_forecast_windspeed = element_forecast_time.getElementsByTagName("windSpeed");
			Element element_forecast_windspeed = (Element) List_forecast_windspeed.item(0);

			String forecast_temperature = element_forecast_temperature.getAttributeNode("value").getValue();
			String forecast_pressure = element_forecast_pressure.getAttributeNode("value").getValue();
			String forecast_humidity = element_forecast_humidity.getAttributeNode("value").getValue();
			if (element_forecast_precipitation.getAttributeNode("value") != null)
				forecast_precipitation = element_forecast_precipitation.getAttributeNode("value").getValue();
			String forecast_windspeed = element_forecast_windspeed.getAttributeNode("mps").getValue();

			System.out.println("Forecasted Temperature : " + forecast_temperature);
			System.out.println("Forecasted Pressure : " + forecast_pressure);
			System.out.println("Forecasted Humidity : " + forecast_humidity);
			System.out.println("Forecasted Precipitation : " + forecast_precipitation);
			System.out.println("Forecasted Wind Speed : " + forecast_windspeed);
			System.out.println("Forecasted Time from : " + forecast_time_from);
			System.out.println("Forecasted Time to : " + forecast_time_to);

			System.out.println("Length : " + list_forecast_time.getLength());
			for (int i = 0; i < list_forecast_time.getLength(); i++) {
				System.out.println("Temperature : "
						+ element_forecast_time.getElementsByTagName("temperature").item(0).getNodeValue());

			}

			// End of parsing the forecasted data

			// Parse the current weather data
			Document document = builder.parse(current_urlConnection.getInputStream());
			document.getDocumentElement().normalize();

			NodeList list_current = document.getElementsByTagName("current");
			Element element_current = (Element) list_current.item(0);

			NodeList list_temperature = element_current.getElementsByTagName("temperature");
			Element element_temperature = (Element) list_temperature.item(0);

			NodeList list_humidity = element_current.getElementsByTagName("humidity");
			Element element_humidity = (Element) list_humidity.item(0);

			NodeList list_pressure = element_current.getElementsByTagName("pressure");
			Element element_pressure = (Element) list_pressure.item(0);

			NodeList list_wind = element_current.getElementsByTagName("wind");
			Element element_wind = (Element) list_wind.item(0);

			// Getting the time stamp
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String final_timestamp = sdf.format(timestamp);

			String temperature = element_temperature.getAttribute("value");
			String humidity = element_humidity.getAttribute("value");
			String pressure = element_pressure.getAttribute("value");
			;
			String wind_speed = (String) element_wind.getElementsByTagName("speed").item(0).getAttributes().item(1)
					.getNodeValue();

			// String wind_speed = (String) element_wind.getAttribute("value");
			System.out.println("Time Stamp : " + final_timestamp);
			System.out.println("Current Temperature : " + Float.parseFloat(temperature));
			System.out.println("Humidity : " + Integer.parseInt(humidity));
			System.out.println("Pressure : " + Integer.parseInt(pressure));
			System.out.println("Wind Speed : " + Float.parseFloat(wind_speed));

			// Saving the data in the excel file
			int row_count = 0;
			Row row = null;
			// The data is stored in teh list
			ArrayList<Object> list_weather_data = new ArrayList<Object>();
			list_weather_data.add(final_timestamp);
			list_weather_data.add(Float.parseFloat(temperature));
			list_weather_data.add(Float.parseFloat(humidity));
			list_weather_data.add(Float.parseFloat(pressure));
			list_weather_data.add(Float.parseFloat(wind_speed));
			list_weather_data.add(forecast_time_from);
			list_weather_data.add(forecast_time_to);
			list_weather_data.add(Float.parseFloat(forecast_temperature));
			list_weather_data.add(Float.parseFloat(forecast_pressure));
			list_weather_data.add(Float.parseFloat(forecast_humidity));

			if (forecast_precipitation != null)
				list_weather_data.add(Float.parseFloat(forecast_precipitation));
			else
				list_weather_data.add("");
			list_weather_data.add(Float.parseFloat(forecast_windspeed));

			// Read the spreadsheet that needs to be updated
			FileInputStream fie_input_stream = new FileInputStream(
					new File("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx"));
			// Access the workbook
			XSSFWorkbook workbook = new XSSFWorkbook(fie_input_stream);
			// Access the worksheet, so that we can update / modify it.
			XSSFSheet worksheet = workbook.getSheet("Current Weather");
			row_count = worksheet.getLastRowNum();

			if (row_count != 0)
				row = worksheet.createRow(++row_count);
			else {
				// System.out.println("Went inside");
				row = worksheet.createRow(row_count);
				row.createCell(0).setCellValue("Time Stamp");
				row.createCell(1).setCellValue("Temperature (celcius)");
				row.createCell(2).setCellValue("Humidity (%)");
				row.createCell(3).setCellValue("Pressure (hPa)");
				row.createCell(4).setCellValue("Wind Speed (mps)");
				row.createCell(5).setCellValue("Forecasted Time ( From )");
				row.createCell(6).setCellValue("Forecasted Time ( To )");
				row.createCell(7).setCellValue("Forecasted Temperature (celcius)");
				row.createCell(8).setCellValue("Forecasted Pressure (hPa))");
				row.createCell(9).setCellValue("Forecasted Humidity (%");
				row.createCell(10).setCellValue("Forecasted Precipitation");
				row.createCell(11).setCellValue("Forecasted Wind Speed (mps)");

				row = worksheet.createRow(++row_count);
			}

			// declare a Cell object
			Cell cell = null;
			// Access the second cell in second row to update the value

			for (int i = 0; i < 12; i++) {
				System.out.println("List Output : " + list_weather_data.get(i));
				if (i == 0)
					row.createCell(column_count).setCellValue(final_timestamp);
				else if (list_weather_data.get(i).equals("") || list_weather_data.get(i).equals("null"))
					row.createCell(column_count).setCellValue("");
				
				else if(i==5||i==6) row.createCell(column_count).setCellValue((String) list_weather_data.get(i));
				else
					row.createCell(column_count).setCellValue((Float) list_weather_data.get(i));
				column_count++;

			}

			fie_input_stream.close();
			// Open FileOutputStream to write updates
			FileOutputStream output_file = new FileOutputStream(
					new File("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx"));
			// write changes
			workbook.write(output_file);
			// close the stream
			output_file.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {

			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
