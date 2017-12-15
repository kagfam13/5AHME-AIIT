/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue01.serial;

import java.util.Arrays;
import java.util.List;
import jssc.SerialPort;
import jssc.SerialPortList;

/**
 *
 * @author Fabian
 */
public class SimpleSerial implements AutoCloseable{
    private final String portName; 
    private SerialPort serialPort;
    public SimpleSerial(String portName) throws Exception
    {
        final List <String> comms = findSerialComms();
        if(!comms.contains(portName))
            throw new Exception(String.format("Schnittstelle %s nicht gefunden!", portName));
        this.portName = portName;
    }
    
    public void open() throws Exception
    {
        try {
            if(serialPort == null)
            serialPort = new SerialPort(portName);
            if(!serialPort.isOpened())
            serialPort.openPort();
        } catch (Exception e) {
            serialPort = null;
            throw e;
        }
    }
    
    @Override
    public void close() throws Exception
    {
        if(serialPort != null)
        {
            if(serialPort.isOpened())
                serialPort.closePort();
        }
    }
    
    public final static List<String> findSerialComms()
    {
        return Arrays.asList(SerialPortList.getPortNames());
    }
    
    public static void main(String[] args) {
        final List <String> comms = findSerialComms();
        System.out.format("%d Schnittstellen gefunden:%n",comms.size());
        for(String com: comms)
            System.out.println(com);
    }
}
