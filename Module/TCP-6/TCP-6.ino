#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
WiFiServer server(750);
 
void startAP()
{
  delay(100);
  WiFi.mode(WIFI_STA);
  WiFi.softAP("FR","12345FR485");
  delay(500);
  server.begin();
  Serial.println("Server started");
}
void setup() {
  Serial.begin(115200);
  startAP();
}
 
void loop(){
  int a = 2;
  bool x = 0;
  WiFiClient client=server.available();
  if(client){//Если клиент подключён
  while(client.connected()){
    if(Serial.available()>0){// Если принимаем по UART
      char TX = Serial.read(); // Считываем байт( если нужно строку то Serial.ReadStringUntil("\n")
      client.print(TX); // Шлём по TCP то что приняли по UART
        }
      }
    }
    if(client.available()>0){ //Если принимаем по TCP
      char RX = client.read(); // Считываем сообщение клиента
      Serial.print(RX);// Шлём в UART
      if(0xFF == RX){
        Serial.println("Начало отправки сообщения");
        RX = Serial.read();
        client.print(RX);
        if (RX == 0xC0 && x == 0){
          RX = 0;
          while(RX != 0xC0 && x != 1){
            RX = Serial.read();
            a++;
            if (a == 3){
              Serial.print("Кому: ");
              Serial.println(RX);
            }
            if (a == 4){
              Serial.print("Куда: ");
              Serial.println(RX);
            }
            if (a == 5){
              Serial.print("Выполнен ли запрос? :");
              Serial.println(RX);
            }
            if (a == 6){
              Serial.print("Размер данных: ");
              Serial.print(RX);
              a == RX;
              while (a > 0){
                RX = Serial.read();
                Serial.print("Data: ");
                Serial.print(RX);
                a--;
              }
              a == 6;
            }
            if (a == 7){
              Serial.print("Контрольная сумма: ");
              Serial.print(RX);
              a++;
            }
            if (a == 9){
              Serial.print("Конец кадра: ");
              Serial.print(RX);
              x = 1;
            }
          }
        }
      }
    }
 }
