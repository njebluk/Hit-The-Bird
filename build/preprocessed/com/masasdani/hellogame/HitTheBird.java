/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.masasdani.hellogame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;

/**
 *
 * @author badak
 */
public class HitTheBird extends GameCanvas implements Runnable{

    GameDesign design;
    LayerManager layerManager;
    TiledLayer background;
    TiledLayer road;
    Sprite orang;
    Sprite bird;
    Sprite rock;
    Sprite explosion;
    private int width;
    private int height;
    private int currentX;
    private int kecepatanOrang=2;
    
    //property burung
    private int kecepatanBird=3;
    
    //properti rock
    private Random random;
    private int rockXSpeed;
    private int rockYSpeed;
    private boolean isRockThrown=false;
    private int gravity = 2;
    private final static boolean DIRECTION_LEFT = false;
    private final static boolean DIRECTION_RIGHT = true;
    private boolean rockDirection;
    
    private boolean birdHitByRock=false;
    
    
    private Player backgoundSound;
    private Player birdSound;
    private Player explotionSound;
    private int score =0;
    
    public HitTheBird() {
        super(false);
        width=getWidth();
        height=getHeight();
        setFullScreenMode(true);
        design=new GameDesign();
        layerManager=new LayerManager();
        random=new Random(System.currentTimeMillis());
        //InputStream streamBgSound= getClass().getResourceAsStream("/nflonfox.wav");
        InputStream streamBirdSound= getClass().getResourceAsStream("/bird.WAV");
        InputStream streamExplotionSound= getClass().getResourceAsStream("/boom.wav");
        try {
            backgoundSound =Manager.createPlayer(streamExplotionSound, "audio/x-wav");
            backgoundSound.prefetch();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
        
    }

    public void start(){
        layerManager.setViewWindow(0, 0, width, height);
        loadRoad();
        loadBird();
        loadRock();
        loadExplotion();
        loadOrang();
        loadBackground();
        backgoundSound.setLoopCount(1000);
        try {
            backgoundSound.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new Thread(this).start();
        new BirdThread().start();
    }
    
    private int getRandom(int bound){
        return Math.abs(random.nextInt() % bound);
    }
    
    private void moveRock(){
        rock.setVisible(true);
        if(rockYSpeed > -25) rockYSpeed = rockYSpeed - gravity;
        if(rockDirection == DIRECTION_LEFT) rock.move(-rockXSpeed, -rockYSpeed);
        else if(rockDirection == DIRECTION_RIGHT) rock.move(rockXSpeed, -rockYSpeed);
        if(rock.getRefPixelY() > height){
            isRockThrown=false;
            rock.setVisible(false);
        }
    }
    
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        while(true){
            getUserInput();
            if(isRockThrown) moveRock();
            updateScreen();
            try {
                Thread.sleep(1000/300);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(rock.collidesWith(bird, true)) birdHitByRock=true;
        }
    }

    private void getUserInput() {
        int keyState=getKeyStates();
        if((keyState & RIGHT_PRESSED)!=0){
            if(orang.getX()<(road.getWidth()-orang.getWidth())){
                orang.setTransform(Sprite.TRANS_NONE);
                orang.move(kecepatanOrang, 0);
                orang.nextFrame();
                moveRoad(orang.getX()+kecepatanOrang);
                rockDirection = DIRECTION_RIGHT;
            }
        }
        if((keyState & LEFT_PRESSED) !=0){
            if(orang.getX()>0){
                orang.setTransform(Sprite.TRANS_MIRROR);
                orang.move(-2, 0);
                orang.nextFrame();
                moveRoad(orang.getX()-kecepatanOrang);
                rockDirection=DIRECTION_LEFT;
            }
        }
        if((keyState & UP_PRESSED)!=0){
            int i=orang.getY();
            int top=i+5;
            for(i=orang.getY();i<=top;i++){
                orang.move(0, -2);
            }
        }        
        if((keyState & FIRE_PRESSED)!=0){
            if(!isRockThrown){
                isRockThrown=true;
                rock.setPosition(orang.getX() +rock.getWidth() /2, orang.getY() + rock.getHeight()/2);
                rock.setVisible(true);
                rockYSpeed = getRandom(10) + 25;
                System.out.println(rockYSpeed);
                rockXSpeed = getRandom(4) + 3;
                System.out.println(rockXSpeed);
            }
        }
    }

    private void updateScreen() {
        Graphics g=getGraphics();
        g.setColor(0, 0, 0);
        //g.setFont(Font.getFont());
        g.drawString("score :"+score, 0, 0, 0);
        
        layerManager.paint(g, 0, 0);
        flushGraphics();
    }

    private void loadBackground() {
        try {
            background = design.getBackground2();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        layerManager.append(background);
    }

    private void loadRoad() {
        try {
            road=design.getRoad();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        layerManager.append(road);
    }

    private void loadOrang() {
        try {
            Image image=Image.createImage("/duck.png");
            orang =new Sprite(image, 30, 30);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        while(!orang.collidesWith(road, true)){
            orang.move(0, 1);
        }
        orang.defineReferencePixel(15, 15);//setengah dimensi gambar
        layerManager.append(orang);
    }
    
    private void moveRoad(int x){
        if(x < road.getX()){
            currentX=road.getX();
        }else if(x > road.getX() + road.getWidth() - getWidth()){
            currentX= road.getX() + road.getWidth() - getWidth();
        }else{
            currentX = x;
        }
        layerManager.setViewWindow(currentX, 0, width, height);
    }

    private void loadBird() {
        try {
            Image birdImage=Image.createImage("/bird.png");
            bird=new Sprite(birdImage, 33, 31);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        bird.defineReferencePixel(15, 15);
        bird.setPosition(0, 30);
        layerManager.append(bird);
    }

    private void loadRock() {
        try {
            Image rockImage=Image.createImage("/rock.png");
            rock=new Sprite(rockImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        rock.defineReferencePixel(6, 6);
        rock.setVisible(false);
        layerManager.append(rock);
    }

    private void loadExplotion() {
        try {
            Image image=Image.createImage("/explosion.png");
            explosion=new Sprite(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        explosion.defineReferencePixel(37, 37);
        explosion.setVisible(false);
        layerManager.append(explosion);
    }
    
    class BirdThread extends Thread{
        
        public void run(){
            while(true){
                if(birdHitByRock) explodeBird();
                else moveBird();
                try {
                    Thread.sleep(1000/30);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        private void explodeBird(){
            if(bird.isVisible()){
                bird.setVisible(false);
                explosion.setPosition(bird.getX() - bird.getWidth() / 2, bird.getY() - bird.getHeight() /2);
                explosion.setVisible(true);
                explosion.setFrame(0);
            }else{
                explosion.nextFrame();
                explosion.setVisible(false);
                birdHitByRock=false;
                bird.setVisible(true);
                bird.setPosition(0, 30);
                bird.setTransform(Sprite.TRANS_NONE);
            }
        }

        private void moveBird() {
            bird.move(kecepatanBird, 0);
            bird.nextFrame();
            if(bird.getX() < 0){
                kecepatanBird *= -1;
                bird.move(0, 10);
                bird.setTransform(Sprite.TRANS_NONE);
            }else if(bird.getX()>width){
                kecepatanBird *= -1;
                bird.move(0, 10);
                bird.setTransform(Sprite.TRANS_MIRROR);
                bird.setTransform(Sprite.TRANS_MIRROR);
            }
        }
    }
    
}
