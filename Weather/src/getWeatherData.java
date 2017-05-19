

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
		int row_count=0,column_count=0;
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
	    
	  //Read the spreadsheet that needs to be updated
	    FileInputStream fsIP= new FileInputStream(new File("newFile.xlsx"));
	    
	    //create a blanc document
        XSSFWorkbook wb = new XSSFWorkbook();
        //create a black sheet
        Sheet sheet = wb.createSheet("new sheet");
        //create a new row 0
        Row row = sheet.createRow((short)0);
        
        // Updating the headers
        row.createCell(0).setCellValue("Temperature");
        row.createCell(1).setCellValue("Humidity");
        row.createCell(2).setCellValue("Pressure");
        row.createCell(3).setCellValue("Wind Speed");
        
        // Create a new row 1
        row = sheet.createRow((short)1);
        //create a new cell
        Cell cell = row.createCell(0);
        //insert value in the created cell
        cell.setCellValue(Float.parseFloat(temperature));
    
        //add other cells with different types
       // row.createCell(0).setCellValue(Float.parseFloat(temperature));
        row.createCell(1).setCellValue(Integer.parseInt(humidity));
        row.createCell(2).setCellValue(Integer.parseInt(pressure));
        row.createCell(3).setCellValue(Float.parseFloat(wind_speed));

        FileOutputStream fos;
     
          fos= new FileOutputStream("newFile.xlsx");
          wb.write(fos);
          fos.close();
//	    FileInputStream input_stream =  new FileInputStream(new File(excel_weather_filepath));
//	    Workbook workbook = WorkbookFactory.create(new FileInputStream(excel_weather_filepath));
//	    Sheet sheet = workbook.getSheetAt(0);
//	    workbook.close();
//	    
//	    Object[][] weather_data = {{Float.parseFloat(temperature),Integer.parseInt(humidity),Integer.parseInt(pressure),Float.parseFloat(wind_speed)}};
//	    row_count = sheet.getLastRowNum();
//	   // input_stream.close();
//	    
//	    for(int i=0;i<4;i++){
//	    	Row row = sheet.createRow(++row_count);
//	    	
//	    	Cell cell = row.createCell(column_count);
//	    	cell.setCellValue(row_count);
//	    	
//	    	++column_count;
//	    	cell.setCellValue((Float) Float.parseFloat(temperature));
//	    	cell.setCellValue((Integer) Integer.parseInt(humidity));
//	    	cell.setCellValue((Integer) Integer.parseInt(pressure));
//	    	cell.setCellValue((Float) Float.parseFloat(wind_speed));
//	    	
//	    	
//	    	 
//            FileOutputStream outputStream = new FileOutputStream("src/package_iot/newFile1.xlsx");
//            workbook.write(outputStream);
//            workbook.close();
//            outputStream.close();
//	    }
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
