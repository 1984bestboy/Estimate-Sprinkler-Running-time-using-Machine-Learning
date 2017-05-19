

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
		String weatherCity = "Irvine,US";
		byte forecastDays = 3;
		int column_count=0;
		OpenWeatherMap.Units units = (isMetric) ? OpenWeatherMap.Units.METRIC : OpenWeatherMap.Units.IMPERIAL;
		OpenWeatherMap owm = new OpenWeatherMap(units, owmApiKey);

		try{
			String url = "http://api.openweathermap.org/data/2.5/weather?q=irvine&appid=5565b5455185c3b4f75b4e906831705c&mode=xml";

	    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	    f.setNamespaceAware(false);
	    f.setValidating(false);
	    DocumentBuilder builder = f.newDocumentBuilder();
	    URLConnection urlConnection = new URL(url).openConnection();
	    urlConnection.addRequestProperty("Accept", "application/xml");
	    Document document = builder.parse(urlConnection.getInputStream());
	    document.getDocumentElement().normalize();
	    
	    NodeList lsit_current = document.getElementsByTagName("current");
	    Element element_current = (Element) lsit_current.item(0);
	    
	    NodeList  list_temperature = element_current.getElementsByTagName("temperature");
	    Element element_temperature = (Element) list_temperature.item(0);
	   
	    NodeList  list_humidity = element_current.getElementsByTagName("humidity");
	    Element element_humidity = (Element) list_humidity.item(0);
	    
	    NodeList  list_pressure = element_current.getElementsByTagName("pressure");
	    Element element_pressure = (Element) list_pressure.item(0);
	    
	    NodeList  list_wind = element_current.getElementsByTagName("wind");
	    Element element_wind = (Element) list_wind.item(0);
	    
	    
	    
	    String temperature 	= element_temperature.getAttribute("value");
	    String humidity 	= element_humidity.getAttribute("value");
	    String pressure 		= element_pressure.getAttribute("value");; 
	    String wind_speed = (String)  element_wind.getElementsByTagName("speed").item(0).getAttributes().item(1).getNodeValue();
	    
	    //String wind_speed = (String)  element_wind.getAttribute("value");

	    System.out.println("Current Temperature : " + Float.parseFloat(temperature));
	    System.out.println("Humidity : " + Integer.parseInt(humidity));
	    System.out.println("Pressure : " + Integer.parseInt(pressure));
	    System.out.println("Wind Speed : " + Float.parseFloat(wind_speed));
	    
//Saving the data in the excel file
	   int row_count=0;
	   ArrayList<Object> list_weather_data = new ArrayList<Object>();
	   list_weather_data.add(Float.parseFloat(temperature));
	   list_weather_data.add(Float.parseFloat(humidity));
	   list_weather_data.add(Float.parseFloat(pressure));
	   list_weather_data.add(Float.parseFloat(wind_speed));
	   
	  //Read the spreadsheet that needs to be updated
	    FileInputStream fie_input_stream= new FileInputStream(new File("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx"));  
	    //Access the workbook                  
	    XSSFWorkbook workbook = new XSSFWorkbook(fie_input_stream);
	    //Access the worksheet, so that we can update / modify it. 
	    XSSFSheet worksheet = workbook.getSheet("Current Weather"); 
	    row_count = worksheet.getLastRowNum();
	    Row row=null;
	    if(row_count!=0)
	    row = worksheet.createRow(++row_count);
	    else {
	    	System.out.println("Went inside");
	    	row = worksheet.createRow(row_count);
	    	row.createCell(0).setCellValue("Temperature");
	    	row.createCell(1).setCellValue("Humidity");
	    	row.createCell(2).setCellValue("Pressure");
	    	row.createCell(3).setCellValue("Wind Speed");
	    	
	    	row = worksheet.createRow(++row_count);
	    }
	    
	    // declare a Cell object
	    Cell cell = null; 
	    // Access the second cell in second row to update the value

	    for(int i=0;i<4;i++){
	    	
	    	row.createCell(column_count).setCellValue((Float) list_weather_data.get(i));
	    	column_count++;
	    	System.out.println(column_count);
	    }
 
	    fie_input_stream.close(); 
	    //Open FileOutputStream to write updates
	    FileOutputStream output_file =new FileOutputStream(new File("/Users/balaji/Documents/Github/IOT/Weather/src/newFile.xlsx"));  
	     //write changes
	    workbook.write(output_file);
	    //close the stream
	    output_file.close();
	    

		}catch (FileNotFoundException e) {
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
