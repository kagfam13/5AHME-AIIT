/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue01.modbus;

import java.util.Arrays;
import ue01.serial.SimpleSerial;

/**
 *
 * @author root
 */
public class ReadCoilsTelegram extends AbstractTelegram{
    private final int start, quantity;
    
    public ReadCoilsTelegram(SimpleSerial serial, byte busAddres, int start, int quantity) {
        super(serial, busAddres, (byte)1, buildXmtData(start, quantity), 1 + quantity/8 + (((quantity % 8)!=0) ? 1 : 0));
        this.start = start;
        this.quantity = quantity;
    }
    
    private static byte[] buildXmtData(int start, int quantity) {
        final byte xmt[] = new byte[4];
        xmt[0] = (byte)((start>>8) & 0xFF);
        xmt[1] = (byte)(start&0xFF);
        xmt[2] = (byte)((quantity>>8) & 0xFF);
        xmt[3] = (byte)(quantity&0xFF);
        return xmt;
    }
    
    public boolean getCoil(int address) throws Exception {
        if (address<start || address>=start+quantity)
            throw new Exception("illegal address!");
        final byte[] received = getReceived();
        final int byteIndex = 3 + (address-start)/8, bitIndex = (address-start)%8;
        return (received[byteIndex] & (1<<bitIndex)) != 0;
    }
    
    public static void main(String[] args) {
        try (SimpleSerial serial = new SimpleSerial("/dev/ttyUSB0")) {
            serial.open();
            serial.setParams(jssc.SerialPort.BAUDRATE_57600, jssc.SerialPort.DATABITS_8, jssc.SerialPort.STOPBITS_2, jssc.SerialPort.PARITY_NONE);
            final ReadCoilsTelegram coilsTelegram = new ReadCoilsTelegram(serial, (byte)2, 0, 4);
            coilsTelegram.send();
            System.out.println(Arrays.toString(coilsTelegram.receive()));
            System.out.println(coilsTelegram.getCoil(0));
            System.out.println(coilsTelegram.getCoil(1));
            System.out.println(coilsTelegram.getCoil(2));
            System.out.println(coilsTelegram.getCoil(3));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
