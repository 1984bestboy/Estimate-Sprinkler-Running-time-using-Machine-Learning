void setup() {
  Serial.begin(9600);
}
void loop() {
  if (Serial.available() > 0) {
    int data = Serial.parseInt();

//    char str[2];
//    str[0] = data;
//    str[1] = '\0';
    int int_value = data;
    if (data > 850) {
      Serial.println("No Watering required" );
    }
    else if(data<850)
    {
      Serial.println("Watering required" );
    }
  }
}
//const byte numChars = 32;
//char receivedChars[numChars];
//
//boolean newData = false;
//
//void setup() {
//    Serial.begin(9600);
//    Serial.println("<Arduino is ready>");
//}
//
//void loop() {
//    recvWithStartEndMarkers();
//    showNewData();
//}
//
//void recvWithStartEndMarkers() {
//    static boolean recvInProgress = false;
//    static byte ndx = 0;
//    char startMarker = '<';
//    char endMarker = '>';
//    char rc;
//
// // if (Serial.available() > 0) {
//    while (Serial.available() > 0 && newData == false) {
//        rc = Serial.read();
//
//        if (recvInProgress == true) {
//            if (rc != endMarker) {
//                receivedChars[ndx] = rc;
//                ndx++;
//                if (ndx >= numChars) {
//                    ndx = numChars - 1;
//                }
//            }
//            else {
//                receivedChars[ndx] = '\0'; // terminate the string
//                recvInProgress = false;
//                ndx = 0;
//                newData = true;
//            }
//        }
//
//        else if (rc == startMarker) {
//            recvInProgress = true;
//        }
//    }
//}
//
//void showNewData() {
//    if (newData == true) {
//        Serial.print("This just in ... ");
//        Serial.println(receivedChars);
//        newData = false;
//    }
//}

