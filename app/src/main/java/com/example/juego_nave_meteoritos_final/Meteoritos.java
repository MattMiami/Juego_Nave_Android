package com.example.juego_nave_meteoritos_final;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

public class Meteoritos extends Sprite{

    int x;
    int y;

    public Meteoritos(GameView gameView, Bitmap bitmap, int filasBitmap, int columnasBitmap, boolean vivo) {
        super(gameView, bitmap, filasBitmap, columnasBitmap);

        Random rnd = new Random(System.currentTimeMillis());

        //Situamos a cada meteorito fuera de la pantalla al empezar, en una posicion aleatoria comprendida en el alto del dispositivo
        this.x = (int) (gameView.getWidth()+Math.random()*1);
        this.y = (int) (Math.random()*gameView.getHeight());

        //Damos velocidades aleatorias a cada objeto meteorito
        this.xVelocidad = rnd.nextInt(5)+ 4;
        this.yVelocidad = rnd.nextInt(10) - 5;
        this.vivo = vivo;
    }


    @Override
    protected void update() {
        //Aqui le damos el movimiento sobre el eje X al meteorito, decrementando su eje x para que vaya al sentido contrario que la nave

        if (x > gameView.getWidth()) {
            vivo = false;
        } else {
            x = x - xVelocidad;
        }
        //Evitamos que los meteoritos excedan el alto pantalla
        if(y>gameView.getHeight()-alto*2){
            y = gameView.getHeight()-alto*2;
        }


    }

    @Override
    public void onDraw(Canvas canvas) {
        //Actualizamos el "movimiento" del tiro, usando el método anterior
        update();
        int srcX = frameColumnasActual * ancho;
        int srcY = frameFilasActual * alto;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + ancho;
        src.bottom = srcY + alto;
        dst.left = this.x;
        dst.top = this.y;
        dst.right = this.x + ancho;
        dst.bottom = this.y + alto;

        canvas.drawBitmap(bitmap, src, dst, null);
    }


    @Override
    protected int getNextAnimationColumn() {
        //Si tienes un sprite utilizarlo aqui
        return ++frameColumnasActual % columnasBitmap;
    }

    @Override
    public boolean colisionaCon(float x2, float y2) {
        return x2 > x && x2 < x + ancho && y2 > y && y2 < y + alto;
    }



}
