import java.io.*;
import java.net.*;

public class tcpClient {
    public static class client {
        Socket c;
        InputStream isc; // входящий поток от сокета
        OutputStream osc; // исходящий поток от сокета
        client(){
            try {
                c = new Socket("192.168.4.1", 750);
                isc = c.getInputStream();
                osc = c.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public boolean sendData(byte[] data){
            try {
                osc.write(data);
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        public byte[] getData(){
            try{
                int count;
                if((count = isc.available()) > 0){
                    byte[] data = new byte[count];
                    isc.read(data);
                    return data;
                } else {
                    return null;
                }
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void main(String[] args){
        client c = new client();
        InputStream consoleIn = System.in;
        int count;
        while (true){
            try {
                if ((count = consoleIn.available()) > 0) {
                    byte[] data = new byte[count];
                    consoleIn.read(data);
                    c.sendData(data);
                }
                byte[] getData = c.getData();
                if(getData != null){
                    System.out.write(getData);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
