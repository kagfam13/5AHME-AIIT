/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue01.modbus;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ue01.serial.SimpleSerial;
import static ue01.serial.SimpleSerial.findSerialComms;


/**
 *
 * @author kager
 */
public class WriteSingleCoilTelegram extends AbstractTelegram
{
  
  public WriteSingleCoilTelegram (SimpleSerial serial, byte busAddres, int coil, boolean set)
  {
    super(serial, busAddres, (byte)5, buildXmtData(coil, set), 4);
  }
  
  private static byte[] buildXmtData(int coil, boolean set) {
    final byte[] xmt = new byte[4];
    xmt[0] = (byte)((coil>>8) & 0xFF);
    xmt[1] = (byte)(coil&0xFF);
    xmt[2] = set ? (byte)0xFF : (byte)0x00;
    xmt[3] = 0;
    return xmt;
  }
  
  public static void main (String[] args)
  {
    try (SimpleSerial serial = new SimpleSerial("/dev/ttyUSB0"))
    {
      serial.open();
      serial.setParams(jssc.SerialPort.BAUDRATE_57600, jssc.SerialPort.DATABITS_8, jssc.SerialPort.STOPBITS_2, jssc.SerialPort.PARITY_NONE);
      
      //for (int i = 0;i<10000;i++) 
      {
        final WriteSingleCoilTelegram coilTelegram = new WriteSingleCoilTelegram(serial, (byte)2, 2, true);
        coilTelegram.send();
        System.out.println(Arrays.toString(coilTelegram.receive()));
        Thread.sleep(250);
      }
      serial.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
