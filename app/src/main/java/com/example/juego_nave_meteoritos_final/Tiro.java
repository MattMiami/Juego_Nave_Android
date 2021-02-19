package com.example.juego_nave_meteoritos_final;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Tiro extends Sprite {

    public int x;
    public int y;
    Bitmap tiro;

    public Tiro(GameView gameView, Bitmap tiro, int filasBitmap, int columnasBitmap, int x, int y, int xVelocidad, boolean vivo) {
        super(gameView, tiro, filasBitmap, columnasBitmap);

        this.x = x;
        this.y = y;
        this.xVelocidad = xVelocidad;
        this.vivo = vivo;
        this.tiro = tiro;
    }


    @Override
    protected void update() {
    //Aqui le damos el moviemiento sobre el eje X al tiro de la nave haciendo que se salga de la pantalla
        if (x >= gameView.getWidth() + ancho - xVelocidad || x + xVelocidad <= 0) {
            vivo = false;
        } else {
            x = x + xVelocidad;

        }

        //frameColumnasActual = getNextAnimationColumn();

    }

    @Override
    public void onDraw(Canvas canvas) {
        //Actualizamos el "movimiento" del tiro, usando el método anterior
        update();
        int srcX = frameColumnasActual * ancho;
        int srcY = getNextAnimationRow() * alto;
        Rect src = new Rect(srcX, srcY, srcX + ancho, srcY + alto);
        Rect dst = new Rect(x, y, x + ancho, y + alto);
        canvas.drawBitmap(tiro, src, dst, null);
    }




    @Override
    protected int getNextAnimationColumn() {
        //Si tienes un sprite utilizarlo aqui
        return ++frameColumnasActual % columnasBitmap;
    }



}
