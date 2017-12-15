/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue01.modbus;

import ue01.serial.SimpleSerial;


/**
 *
 * @author kager
 */
public class WriteSingleCoilTelegram extends AbstractTelegram
{
  
  public WriteSingleCoilTelegram (SimpleSerial serial, byte busAddres, int coil, boolean set)
  {
    super(serial, busAddres, 5, buildXmtData(coil, set), 4);
  }
  
  private static byte[] buildXmtData(int coil, boolean set) {
    
    return null;
  }
  
}
