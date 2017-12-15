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
public abstract class AbstractTelegram implements Telegram { 
  private final SimpleSerial serial;
  private final byte busAddress, funtionCode;
  private final byte[] xmtData;
  private final int lengthOfAnswer;


  public AbstractTelegram (SimpleSerial serial, byte busAddres, byte funtionCode, byte[] xmtData, int lengthOfAnswer)
  {
    this.serial = serial;
    this.busAddress = busAddres;
    this.funtionCode = funtionCode;
    this.xmtData = xmtData;
    this.lengthOfAnswer = lengthOfAnswer;
  }
  
  private static int calcCrC(byte[] data, int start, int cnt) {
    int crc = 0xFFF;
    for (int i=start; i<start+cnt;i++)
      crc = crc16(crc, data[1]);
    return crc;
  }  
  
  private static int crc16 (int crc, byte data) {
    final int poly16 = 0xA001;
    int lsb;
    crc = ((crc ^ data) | 0xFF00) & (crc | 0x00FF);
    for (int i = 0; i < 8; i++) {
      lsb = (crc & 0x0001);
      crc = crc >> 1;
      if(lsb!=0)
        crc ^= poly16;
    }
    return crc;
  }


  @Override
  public void send () throws Exception
  {
    serial.purgeInput();
    final int bytesToSend = xmtData.length+4;
    final byte[] toSend = new byte[bytesToSend];
    toSend[0] = busAddress;
    toSend[1] = funtionCode;
    System.arraycopy(xmtData, 0, toSend, 2, xmtData.length);
    final int crc = calcCrC(toSend, 0, bytesToSend-2);
    toSend[bytesToSend-2] = (byte)(crc & 0xFF);
    toSend[bytesToSend-1] = (byte)((crc>>8) & 0xFF);
    serial.writeBytes(toSend);
  }


  @Override
  public byte[] receive () throws Exception
  {
    final int bytesToReceive = lengthOfAnswer + 4;
    final byte[] received = serial.readBytes(bytesToReceive, 5000);
    // Plausikontrolle
    if (received[0] != busAddress)
      throw new Exception("illegal device address returned");
    if (received[1] != funtionCode)
      throw new Exception("illegal function code returned");
    return received;
  }
  
  
}
