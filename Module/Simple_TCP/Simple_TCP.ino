#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>

WiFiServer server(750);
const char* ssid = "FR";
const char* password =  "12345FR485";
IPAddress apIP(192, 168, 4, 1);
unsigned char RX;

void setup() {
  WiFi.disconnect();
  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(apIP, apIP, IPAddress(255, 255, 255, 0));
  WiFi.softAP(ssid, password, 11, 4, false);
  server.begin();
  Serial.begin(115200);
  Serial.print("Guten tug");
}

void loop() {
  //int a = 2;
  //bool x = 0;
  WiFiClient client = server.available();
  if (client) { //Если клиент подключён
    while (client.connected()) {
      if (Serial.available() > 0) { // Если принимаем по UART
        char TX = Serial.read();
        client.print(TX); // 
      }
      if (client.available() > 0) { //Если принимаем по TCP
        char RX = client.read();
        Serial.print(RX);
      }
    }
  }
}
