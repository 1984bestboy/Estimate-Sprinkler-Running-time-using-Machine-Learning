
//////////////////////////////////////////////
// Get XML formatted data from the web.
// 1/6/08 Bob S. - Created
//  Assumptions: single XML line looks like: 
//    <tag>data</tag> or <tag>data 
// Include description files for other libraries used (if any)
//
// updated at later dates zoomkat
//////////////////////////////////////////////

#include <SPI.h>
#include <Ethernet.h>
#include <WString.h>

// Define Constants
// Max string length may have to be adjusted depending on data to be extracted
#define MAX_STRING_LEN  20

// Setup vars
char tagStr[MAX_STRING_LEN] = "";
char dataStr[MAX_STRING_LEN] = "";
char tmpStr[MAX_STRING_LEN] = "";
char endTag[3] = {'<', '/', '\0'};
int len,current_run_time=0;
String timeToken="0";
// Flags to differentiate XML tags from document elements (ie. data)
boolean tagFlag = false;
boolean dataFlag = false;

// Ethernet vars
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
//byte ip[] = { 192, 168, 0, 26 };
byte server[] = { 54, 193, 70, 40 }; // www.weather.gov

// Start ethernet client
EthernetClient client;
int current_pressure=0,current_temperature,run_time=0;
void setup()
{
  Serial.begin(9600);
  Serial.println("Starting WebWx");
  Serial.println("connecting...");
  //Ethernet.begin(mac, ip);
  Ethernet.begin(mac);
  delay(1000);

  if (client.connect("ec2-54-193-70-40.us-west-1.compute.amazonaws.com", 8080)) {
    Serial.println("connected");
    client.println("GET /web_server/sensor_data.jsp?temperature=123456&pressure=12 HTTP/1.0");    
    client.println();
    delay(2000);
  } else {
    Serial.println("connection failed");
  }  
}

void loop() {

  // Read serial data in from web:
  if (client.available()) {
    String line = client.readStringUntil('\r');
    timeToken=line.substring(line.length()-19,line.length()-2);
    Serial.println(line);
     int index_temp_start=line.indexOf("Temperature :");
     int index_temperature = line.indexOf("Temperature :");
     int index_temperature_end = line.indexOf("Pressure")-5;
     
     String  string_current_temperature = line.substring(index_temperature+14,index_temperature_end);
     String string_current_run_time = line.substring(line.indexOf("Sprinkler Run Time : ")+21,line.indexOf("Sprinkler Run Time : ")+27);
      string_current_temperature.trim();
      string_current_run_time.trim();
      //current_pressure    = line.substring(line.indexOf("Pressure :")+10,line.indexOf("Soil Moisture :")-4);
      //current_run_time    = 
//     current_pressure = string_current_pressure.toInt();
//   index_temperature_end=index_temperature_end-4;
//    current_run_time = string_current_run_time.toInt();
    Serial.println("Temperature is : " + string_current_temperature);
    Serial.println("Sprinkler Run Time is :" + string_current_run_time);
  }

  if (!client.connected()) {
    Serial.println();
    Serial.println("Disconnected");
    Serial.println("==================================");
    Serial.println("");
    client.stop();

    // Time until next update
    //Serial.println("Waiting");
    for (int t = 1; t <= 15; t++) {
      delay(60000); // 1 minute
    }

    if (client.connect("ec2-54-193-70-40.us-west-1.compute.amazonaws.com", 8080)) {
      //Serial.println("Reconnected");
      client.println("GET /web_server/sensor_data.jsp?temperature=123456&pressure=12 HTTP/1.0");    
      client.println();
      delay(2000);
    } else {
      Serial.println("Reconnect failed");
    }      
  }
}

// Process each char from web
void serialEvent() {

   // Read a char
   char inChar = client.read();
   //Serial.print(".");
  
   if (inChar == '<') {
      addChar(inChar, tmpStr);
      tagFlag = true;
      dataFlag = false;

   } else if (inChar == '>') {
      addChar(inChar, tmpStr);

      if (tagFlag) {      
         strncpy(tagStr, tmpStr, strlen(tmpStr)+1);
      }

      // Clear tmp
      clearStr(tmpStr);

      tagFlag = false;
      dataFlag = true;      
      
   } else if (inChar != 10) {
      if (tagFlag) {
         // Add tag char to string
         addChar(inChar, tmpStr);

         // Check for </XML> end tag, ignore it
         if ( tagFlag && strcmp(tmpStr, endTag) == 0 ) {
            clearStr(tmpStr);
            tagFlag = false;
            dataFlag = false;
         }
      }
      
      if (dataFlag) {
         // Add data char to string
         addChar(inChar, dataStr);
      }
   }  
  
   // If a LF, process the line
   if (inChar == 10 ) {


      Serial.print("tagStr: ");
      Serial.println(tagStr);
      Serial.print("dataStr: ");
      Serial.println(dataStr);


      // Find specific tags and print data
//      if (matchTag("<temp_f>")) {
//        Serial.print("Temp: ");
//         Serial.print(dataStr);
//      }
//      if (matchTag("<relative_humidity>")) {
//        Serial.print(", Humidity: ");
//         Serial.print(dataStr);
//      }
//      if (matchTag("<pressure_in>")) {
//        Serial.print(", Pressure: ");
//         Serial.print(dataStr);
//         Serial.println("");
//      }

      // Clear all strings
      clearStr(tmpStr);
      clearStr(tagStr);
      clearStr(dataStr);

      // Clear Flags
      tagFlag = false;
      dataFlag = false;
   }
}

/////////////////////
// Other Functions //
/////////////////////

// Function to clear a string
void clearStr (char* str) {
   int len = strlen(str);
   for (int c = 0; c < len; c++) {
      str[c] = 0;
   }
}

//Function to add a char to a string and check its length
void addChar (char ch, char* str) {
   char *tagMsg  = "<TRUNCATED_TAG>";
   char *dataMsg = "-TRUNCATED_DATA-";

   // Check the max size of the string to make sure it doesn't grow too
   // big.  If string is beyond MAX_STRING_LEN assume it is unimportant
   // and replace it with a warning message.
   if (strlen(str) > MAX_STRING_LEN - 2) {
      if (tagFlag) {
         clearStr(tagStr);
         strcpy(tagStr,tagMsg);
      }
      if (dataFlag) {
         clearStr(dataStr);
         strcpy(dataStr,dataMsg);
      }

      // Clear the temp buffer and flags to stop current processing
      clearStr(tmpStr);
      tagFlag = false;
      dataFlag = false;

   } else {
      // Add char to string
      str[strlen(str)] = ch;
   }
}

// Function to check the current tag for a specific string
boolean matchTag (char* searchTag) {
   if ( strcmp(tagStr, searchTag) == 0 ) {
      return true;
   } else {
      return false;
   }
}
