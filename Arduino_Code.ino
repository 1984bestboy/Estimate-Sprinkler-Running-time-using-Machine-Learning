#include <dht.h>

dht DHT;


#define DHT22_PIN 5
const int soil = A0;

void setup()
{
    Serial.begin(9600);
    Serial.println("DHT TEST PROGRAM ");
    Serial.print("LIBRARY VERSION: ");
    Serial.println(DHT_LIB_VERSION);
    Serial.println();
    Serial.println("Type,\tstatus,\tHumidity (%),\tTemperature (C), \tSoil Moisture Values");

    //Read Soil Moisture Sensor
    pinMode(soil, INPUT);
}

void loop()
{
    // READ DATA
    Serial.print("DHT22, \t");
    int chk = DHT.read22(DHT22_PIN);
    switch (chk)
    {
        case DHTLIB_OK: 
            Serial.print("OK,\t"); 
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

    //Displaying Humidity
    Serial.print(DHT.humidity, 1);
    Serial.print(",\t");

    //Displaying the temperature
    Serial.println(DHT.temperature, 1);
     Serial.print(",\t");

    //Displaying the Soil Moisture Values
    Serial.println(analogRead(soil));

    delay(1000);

}
