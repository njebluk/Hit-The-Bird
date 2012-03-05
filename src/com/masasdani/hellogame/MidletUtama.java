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
public class MidletUtama extends MIDlet {
    private HelloCanvas canvas;
    private Display display;
    private HitTheBird bird;
    
    
    public MidletUtama() {
        display=Display.getDisplay(this);
    }
    
    public void startApp() {
        //canvas=new HelloCanvas();
        //canvas.start();
        //display.setCurrent(canvas);
        bird=new HitTheBird();
        bird.start();
        display.setCurrent(bird);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
