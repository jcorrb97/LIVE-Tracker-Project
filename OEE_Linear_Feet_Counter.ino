//Arduino Due ordered to implement the keyboardcontroller method to have the scan gun functioning
//#include <KeyboardController.h>

//Required WiFi network libraries and initializers
#include <WiFiNINA.h>
#include <ArduinoHttpClient.h>
//Controller, database IP address & port information
int    HTTP_PORT   = 8080;
char   HOST_NAME[] = "10.104.50.99";
WiFiClient wifi;
HttpClient httpClient = HttpClient(wifi, HOST_NAME, HTTP_PORT);

//Required digital encoder libray
#include <Encoder.h>

#include <SoftwareSerial.h>
#define Rx 0
#define Tx 1

SoftwareSerial BTSerial(Rx, Tx);

//WiFi network information
char   ssid[]      = "UPG-Corp"; 
char   pass[]      = "W3rUPG2014!"; 

//Initialize digital pins 2 and 3 to read encoder
Encoder encoder(2, 3);

//Static Parameters
String WC_ID       = "167";
String department  = "FILTER";

//Dynamic Parameters
double linearFeet = 0;
//String jobStatus = "CLOSED";
String objectID;

//Required to have software reset function
void(* resetFunc) (void) = 0;

//We can eliminate String jobStatus if this is proven 
void loop() {
  //Serial.print(BTSerial.available());
  int availableBytes = BTSerial.available();
  Serial.println(availableBytes);
  if(BTSerial.available() > 0) {
    //String ordNumber = "";
    char orderNumber[availableBytes];
    char rescan[availableBytes];
    for(int i = 0; i < availableBytes; i++) {
      orderNumber[i] = BTSerial.read();
      Serial.print(orderNumber[i]);
    }
    httpClient.post("http://" + ((String) HOST_NAME) + ":" + HTTP_PORT + "/entry/post/" + department + "/" + WC_ID + "/" + linearFeet); //Creates entry in controller
    Serial.println(httpClient.responseStatusCode());
    Serial.println(httpClient.responseBody());

    httpClient.get("http://" + ((String) HOST_NAME) + ":" + HTTP_PORT + "/getID"); //Retrieves ID from database
    Serial.println(httpClient.responseStatusCode());
    objectID = (String) httpClient.responseBody();
    //Serial.println(objectID);   

//Need IT to disable security blocking IP requests or another possible way to make this to work
//    httpClient.put("http://" + ((String) HOST_NAME) + ":" + HTTP_PORT + "/orderInfo/" + objectID + "/" + ((String) orderNumber)); //Initiates SAP Query to find item number to track
//    Serial.println(httpClient.responseStatusCode());
//    Serial.println(httpClient.responseBody());
    
  while(strcmp(rescan, orderNumber) != 0) {
    if(BTSerial.available() > 0 && availableBytes == BTSerial.available()) {
      for(int j = 0; j < availableBytes; j++) {
        rescan[j] = BTSerial.read();
        Serial.print(rescan[j]);
        }
    }
      linearFeet = ((double) encoder.read()) / 1220;
      httpClient.put("http://" + ((String) HOST_NAME) + ":" + HTTP_PORT + "/entry/linearFeetUpdate/" + objectID + "/" + linearFeet);
      Serial.println(httpClient.responseStatusCode());
      Serial.println(httpClient.responseBody());
    }
    httpClient.put("http://" + ((String) HOST_NAME) + ":" + HTTP_PORT + "/entry/endTimeUpdate/" + objectID);
    Serial.println(httpClient.responseStatusCode());
    Serial.println(httpClient.responseBody());
    Serial.println("REBOOTING");
    resetFunc();
  }
  delay(1000);
}

//Connects to the WiFi network, SpringBoot controller and JPA Repository database. 
//If it fails, it will remain stuck in setup until the device is reset.
void setup() {
  //Initialize serial reader @ specified speed [9600 BAUD]
  Serial.begin(9600);
  BTSerial.begin(9600);
  //Begin connection to WiFi
  WiFi.begin(ssid, pass);
  Serial.print("Attempting to connect to network: ");
  Serial.println(ssid);
  while(WiFi.status() != WL_CONNECTED){
    Serial.println("Connection failed");
    while(true);
  } 
  Serial.println("Connection succesfull");
  Serial.println("Attempting to connect to database server...");
  //Begin connection to Database
  if(wifi.connect(HOST_NAME, HTTP_PORT)){
    Serial.println("Connected to server");
  } else {
    Serial.println("Couldn't connect to database server");
    while(true);
  }
}
