// defines pins numbers

const int trigPin = 16;  //D0
const int echoPin = 5;  //D1
const int led = 4;  //D2

// defines variables
long duration;
int distance;
double disMin = 20;

void setup() {
pinMode(trigPin, OUTPUT); // Sets the trigPin as an Output
pinMode(echoPin, INPUT); // Sets the echoPin as an Input
pinMode(led, OUTPUT); // Sets the led as an Output
Serial.begin(9600); // Starts the serial communication
}

void loop() {

// Clears the trigPin
digitalWrite(trigPin, LOW);
digitalWrite(led, LOW);
delayMicroseconds(2);

// Sets the trigPin on HIGH state for 10 micro seconds
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);

// Reads the echoPin, returns the sound wave travel time in microseconds
duration = pulseIn(echoPin, HIGH);

// Calculating the distance
distance= duration*0.017;
// Prints the distance on the Serial Monitor
Serial.print("Distance: ");
Serial.println(distance);
if (distance <= disMin){
    digitalWrite(4, HIGH);
  }
  else {
    digitalWrite(led, LOW);
    }
delay(2000);
}
 
