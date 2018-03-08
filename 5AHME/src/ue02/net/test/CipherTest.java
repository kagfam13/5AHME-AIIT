/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue02.net.test;

import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author Kager
 */
public class CipherTest
{
  public static String encode(String passwort, String message)
          throws Exception
  {
    //Passwort in Bytefeld umwandeln
    final byte[] bPasswort = passwort.getBytes("utf8");
    //Message in Bytefeld umwandeln
    final byte [] bMessage = message.getBytes("utf8");
    //Message digest über Passwort rechnen
    final byte [] bSchluessel = MessageDigest.getInstance("md5").digest(bPasswort); //Hash erzeugen
    //Schlüssek Objekt erstellen
    SecretKeySpec secretKeySpec = new SecretKeySpec(bSchluessel,"AES");
    //Cipher Objekt erstellen
    Cipher cipher = Cipher.getInstance("AES");
    //Cipher initialisieren
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    //Verschlüsseln
    byte[] b = cipher.doFinal(bMessage);
    //Umwandeln in String
    return DatatypeConverter.printBase64Binary(b);
  }
  
  public static String decode(String decodeData, String passwort)
          throws Exception
  {
    //Umwandeln in byte[]
    byte[] b = DatatypeConverter.parseBase64Binary(decodeData);
    //Passwort in Bytefeld umwandeln
    final byte[] bPasswort = passwort.getBytes("utf8");
    //Message über das Passwort rechnen
    final byte[] bSchluessel = MessageDigest.getInstance("md5").digest(bPasswort);
    //Cipher Objekt erstellen
    final Cipher cipher = Cipher.getInstance("AES");
    //Schlüssel Objekt erstellen
    final SecretKeySpec secretKeySpec = new SecretKeySpec(bSchluessel,"AES");
    //Cipher initialisieren
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    //Daten entschlüsseln
    final byte[] data = cipher.doFinal(b);
    //Wieder als String zurückliefern
    return new String(data,"utf8");
  }
  
  public static void main (String[] args)
  {
    try
    {
      String send = "Das ist ein sehr sicheres Passwort";
      System.out.println(send);
      String crypto = CipherTest.encode("1234", send);
      System.out.println(crypto);
      String receive = CipherTest.decode(crypto, "1234");
      System.out.println(receive);
    }
    catch (Exception ex)
    {
      Logger.getLogger(CipherTest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
