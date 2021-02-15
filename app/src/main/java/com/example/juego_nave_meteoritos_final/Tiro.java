package com.example.juego_nave_meteoritos_final;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tiro {

    int[] DIRECTION_TO_ANIMATION_MAP = {1};

    private GameView gameView;
    private Bitmap tiro;
    private static final int TIRO_FILAS = 1;
    private static final int TIRO_COLUMNAS = 2;
    private int xVelocidad = 40;
    private int yVelocidad = 40;
    public int x = 0;
    public int y = 0;
    private int frameActual = 0;
    private int ancho;
    private int alto;

    public Tiro(GameView gameView, Bitmap tiro, Context context) {
        this.gameView = gameView;
        this.tiro = tiro;

        //Dividimo el Sprite de la nave entre sus fila y columnas para obetener las direfentes animaciones
        this.ancho = tiro.getWidth()/TIRO_COLUMNAS;
        this.alto = tiro.getHeight()/TIRO_FILAS;

        ancho = (int) (ancho*1000);
        alto = (int) (alto*1000);

    }

    //Damos velocidad al objeto y marcamos los límites para que no salga de la pantalla
    private void update(){
        if (x >= MainActivity.ancho - ancho - xVelocidad || x + xVelocidad <= -ancho/200) {
            xVelocidad = -xVelocidad;
        }
        else {
            x = x + xVelocidad;
        }
        if (y > MainActivity.alto - alto - yVelocidad || y + yVelocidad < 0) {
            yVelocidad = -yVelocidad;
        }else{
            y = y + yVelocidad;
        }

        frameActual = getAnimationRow();
    }

    public void onDraw(Canvas canvas) {
        update();

        canvas.drawBitmap(tiro, MainActivity.ancho/2 , MainActivity.alto/2, null);
    }

    private int getAnimationRow() {
        return ++frameActual % TIRO_COLUMNAS;

    }


    public static int getTiroFilas() {
        return TIRO_FILAS;
    }

    public static int getTiroColumnas() {
        return TIRO_COLUMNAS;
    }

    public int getxVelocidad() {
        return xVelocidad;
    }

    public void setxVelocidad(int xVelocidad) {
        this.xVelocidad = xVelocidad;
    }

    public int getyVelocidad() {
        return yVelocidad;
    }

    public void setyVelocidad(int yVelocidad) {
        this.yVelocidad = yVelocidad;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}
