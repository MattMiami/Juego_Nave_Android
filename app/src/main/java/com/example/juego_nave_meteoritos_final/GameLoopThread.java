package com.example.juego_nave_meteoritos_final;

import android.annotation.SuppressLint;
import android.graphics.Canvas;

public class GameLoopThread extends Thread {

    static final long FPS = 40;
    private GameView gameView;
    private boolean estaActivo;


    public GameLoopThread(GameView gameView) {
        this.gameView = gameView;
    }

    public void setEjecucion(boolean ejecutar){
        estaActivo = ejecutar;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while(estaActivo){
            Canvas canvas = null;
            startTime = System.currentTimeMillis();
            try{
                canvas = gameView.getHolder().lockCanvas();
                synchronized (gameView.getHolder()){
                    gameView.onDraw(canvas);
                }
            }finally {
                if(canvas != null){
                    gameView.getHolder().unlockCanvasAndPost(canvas);
                }

            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);
            } catch (Exception e) {}
        }
    }


}
