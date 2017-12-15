/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue01.modbus;


/**
 *
 * @author kager
 */
public interface Telegram
{
  public void send()
          throws Exception;
  public byte[] receive()
          throws Exception;
}
