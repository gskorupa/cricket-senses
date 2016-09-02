/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cricketmsf.raspberry;

/**
 *
 * @author Grzegorz Skorupa <g.skorupa at gmail.com>
 */
public class GpioConfiguration {
    
    public int spi = 0;
    public int pin = 0;
    
    public GpioConfiguration(int spi, int pin){
        this.spi=spi;
        this.pin=pin;
    }
    
}
