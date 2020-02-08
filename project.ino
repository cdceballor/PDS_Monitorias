#include "ThingSpeak.h"
#include <ESP8266WiFi.h>

//ThingSpeak constants
// channelnumber example 
//Channel key 

const unsigned int channelNumber = 983678; //Change
const String channelWriteKey = "RB2KL329P3BG3WZA"; //Change

//Sensor constants & pins
const int trigPin = 16;  //D0
const int echoPin = 5;  //D1
const int led = 4;  //D2
const double disMin = 20;

//Wifi configuration
const String red = ""; //Red
const String pass = ""; //PassRed
WiFiClient prueba;


void setup() {

  connections();
  
  pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
  pinMode(echoPin, INPUT); // Sets the echoPin as an Input
  pinMode(led, OUTPUT); // Sets the led as an Output
  Serial.begin(115200); // Starts the serial communication

  //Clean pins
  digitalWrite(led, LOW);
  digitalWrite(trigPin, LOW);
}

void loop() {
  double distance = sensor();
  sendThingSpeak(1, distance);
  delay(1000); //12000
}

void sendThingSpeak(int channelField, float ultraS) {
    ThingSpeak.setField(channelField , ultraS);
    ThingSpeak.writeFields(983678 , "RB2KL329P3BG3WZA"); //Change
}

bool connections() {
  Serial.println();
  WiFi.begin(red, pass);
  ThingSpeak.begin(prueba);
  
  while(WiFi.status() != WL_CONNECTED){
    delay(500); //Delay connection
    Serial.print(".");
  }
  Serial.println();
  Serial.println("Conectado a WiFi");
  Serial.print("Direccion IP: ");
  Serial.println(WiFi.localIP());
}

double sensor() {
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10); //On and off
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  long duration = pulseIn(echoPin, HIGH);

  // Calculating the distance
  double distance = duration * 0.017;
  // Prints the distance on the Serial Monitor
  Serial.print("Distance: ");
  Serial.println(distance);
  if (distance <= disMin) 
    digitalWrite(led, HIGH);  
  else 
    digitalWrite(led, LOW); 
  return distance;  
}
