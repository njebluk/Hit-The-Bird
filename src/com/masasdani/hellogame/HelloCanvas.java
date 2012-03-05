/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.masasdani.hellogame;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author badak
 */
public class HelloCanvas extends GameCanvas implements Runnable{

    Graphics g;
    private int width;
    private int heigth;
    //private Image image = Image.createImage("/");
    
    public HelloCanvas() {
        super(false);
        setFullScreenMode(true);
        g=getGraphics();
        width=getWidth();
        heigth=getHeight();
    }

    public void start(){
        new Thread(this).start();
    }
    
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        while(isNotGameOver()){
            drawBackgroud();
            getUserInput();
            updateScreen();
            try {
                Thread.sleep(1000/30);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isNotGameOver() {
        return true;
    }

    private void drawBackgroud() {
        g.setColor(0,255,0);
        g.fillRect(0, 0, width, heigth);
    }

    private void getUserInput() {
        
    }

    private void updateScreen() {
        flushGraphics();
    }
    
}
