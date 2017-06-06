/*
  Web Server

  A simple web server that shows the value of the analog input pins.
  using an Arduino Wiznet Ethernet shield.

  Circuit:
   Ethernet shield attached to pins 10, 11, 12, 13
   Analog inputs attached to pins A0 through A5 (optional)

  created 18 Dec 2009
  by David A. Mellis
  modified 9 Apr 2012
  by Tom Igoe
  modified 02 Sept 2015
  by Arturo Guadalupi

*/

#include <SPI.h>
#include <Ethernet.h>
#include <dht.h>

dht DHT;




// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};
const int soil = A15;
#define DHT22_PIN 5
IPAddress ip(192, 168, 0, 27);

// Initialize the Ethernet server library
// with the IP address and port you want to use
// (port 80 is default for HTTP):
EthernetServer server(80);

void setup() {
  pinMode(4, OUTPUT);
  digitalWrite(4, HIGH);
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  //Read Soil Moisture Sensor
  pinMode(soil, INPUT);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }


  // start the Ethernet connection and the server:
  Ethernet.begin(mac, ip);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
}


void loop() {
  //New Temperature Code being added
  unsigned long Time1 = 0;
  Time1 = millis();
  Time1 = Time1 / 1000;
  unsigned long interval = 10;

  // READ DATA
  //Serial.print("DHT22, \t");
  int chk = DHT.read22(DHT22_PIN);

  Serial.print(Time1);

  switch (chk)
  {
    case DHTLIB_OK:
      //Serial.print("OK,\t");
      break;
    case DHTLIB_ERROR_CHECKSUM:
      Serial.print("Checksum error,\t");
      break;
    case DHTLIB_ERROR_TIMEOUT:
      Serial.print("Time out error,\t");
      break;
    default:
      Serial.print("Unknown error,\t");
      break;
  }
  // DISPLAY DATA

  //New Temperature Code being added
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client) {
    Serial.println("new client");
    // an http request ends with a blank line
    boolean currentLineIsBlank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        Serial.write(c);
        // if you've gotten to the end of the line (received a newline
        // character) and the line is blank, the http request has ended,
        // so you can send a reply
        if (c == '\n' && currentLineIsBlank) {
          // send a standard http response header
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");
          client.println("Connection: close");  // the connection will be closed after completion of the response
          client.println("Refresh: 5");  // refresh the page automatically every 5 sec
          client.println();
          client.println("<!DOCTYPE HTML>");
          client.println("<html>");

          //Displaying Humidity
          Serial.print(DHT.humidity, 1);
          Serial.print(",");
          client.print("Humidity : ");
          client.print(DHT.humidity, 1);
          client.print(",");
          
          //Displaying the temperature
          Serial.print(DHT.temperature, 1);
          Serial.print(",");
          client.print("Temperature : " );
          client.print(DHT.temperature, 1);
          client.print(",");
          
          client.print("Soil Moisture Sensor :");
          client.print(analogRead(soil));
          client.println("<br>");
          delay(1000);
          // output the value of each analog input pin
          for (int analogChannel = 0; analogChannel < 6; analogChannel++) {
            int sensorReading = analogRead(analogChannel);
            client.print("analog input ");
            client.print(analogChannel);
            client.print(" is ");
            client.print(sensorReading);
            client.println("<br />");
          }
          client.println("</html>");
          break;
        }
        if (c == '\n') {
          // you're starting a new line
          currentLineIsBlank = true;
        } else if (c != '\r') {
          // you've gotten a character on the current line
          currentLineIsBlank = false;
        }
      }
    }
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disconnected");
  }
}

