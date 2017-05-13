

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.aksingh.owmjapis.OpenWeatherMap;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.sql.Date;




public class getWeatherData {

	public static void main(String[] args) {
		boolean isMetric = true;
		String owmApiKey = "5565b5455185c3b4f75b4e906831705c"; 
		String weatherCity = "Irvine,US";
		byte forecastDays = 3;
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
	   
	    String temperature = element_temperature.getAttribute("value");
	    
	    System.out.println("Current Temperature : " + temperature);
	    
	    System.out.println ("Root element: " +  document.getDocumentElement().getNodeName());
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
