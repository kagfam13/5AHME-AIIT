/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue02.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import ue02.net.test.CipherTest;


/**
 *
 * @author kager
 */
public class SimpleServer {
  private final int port;
  ServerSocket serverSocket = null;

  public SimpleServer (int port) {
    this.port = port;
  }
  
  private Socket listen() throws IOException {
    return serverSocket.accept();
  }
  
  private String readRequest(Socket socket) throws Exception {
    return new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf8")).readLine();
    // return CipherTest.decode(DatatypeConverter.parseBase64Binary(new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf8")).readLine()), "test");
  }
  
  private String createResponse(String request) {
    return "Hallo Client! Deine Anfrage war: " + request + "%n";
  }
  
  private void sendResponse(Socket socket, String response) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf8"));
    writer.write(response);
    writer.flush();
    socket.shutdownOutput();
  }
  
  void handleRequest() throws Exception {
    Socket socket = listen();
    sendResponse(socket, createResponse(readRequest(socket)));
  }
  
  public void start() throws IOException {
    if (serverSocket == null)
      serverSocket = new ServerSocket(port);
  }
  
  public void stop() throws IOException {
    if(serverSocket != null) {
      serverSocket.close();
      serverSocket = null;
    }
  }
  
  public static void main (String[] args)
  {
    try
    {
      final  SimpleServer server = new SimpleServer(4567);
      server.start();
      server.handleRequest();
      server.stop();
    }
    catch (Exception e)
    {
    }
  }
}
