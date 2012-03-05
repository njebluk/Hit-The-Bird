/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.masasdani.hellogame;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author badak
 */
public class MidletBurung extends MIDlet {

    private HitTheBird bird;
    private Display display;

    public MidletBurung() {
        
    }
    
    
    public void startApp() {
        display= Display.getDisplay(this);
        bird=new HitTheBird();
        bird.start();
        display.setCurrent(bird);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
