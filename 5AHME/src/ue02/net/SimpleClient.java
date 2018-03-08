/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue02.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.xml.bind.DatatypeConverter;
import ue02.net.test.CipherTest;


/**
 *
 * @author kager
 */
public class SimpleClient
{
  public static String sendRequestAndReceiveResponse(String host,int port, String request) 
          throws Exception {
    final InetAddress address = InetAddress.getByName(host);
    final StringBuilder response = new StringBuilder();
    try(final Socket socket = new Socket(address, port)) {
      // Senden
      System.out.println("*** Verbindung hergestellt");
      final BufferedWriter writer = new BufferedWriter(
              new OutputStreamWriter(socket.getOutputStream(), "utf8"));
      writer.write(request);
      writer.flush();
      socket.shutdownOutput();
      
      // enmpfangen
      BufferedReader reader = new BufferedReader(
              new InputStreamReader(socket.getInputStream(), "utf8"));
      String line;
      while ((line=reader.readLine()) != null) {
        response.append(line).append("\n");
      }
    }
    
    return response.toString();
  }
  
  public static void main (String[] args)
  {
    try
    {
      final String response = sendRequestAndReceiveResponse("localhost", 4567, "hallo");
      System.out.println("Antwort" + response);
    }
    catch (Exception e)
    {
    }
//    try {
//      String string = DatatypeConverter.printBase64Binary(CipherTest.encode("test", "hallo"));
//      System.out.println(string);
//      
//      final String response = sendRequestAndReceiveResponse("localhost", 4567, string);
//      System.out.println("Antwort: " + response);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
  }
}
