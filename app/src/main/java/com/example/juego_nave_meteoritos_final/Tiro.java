package com.example.juego_nave_meteoritos_final;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Tiro extends Sprite {

    public int x;
    public int y;


    public Tiro(GameView gameView, Bitmap tiro, int filasBitmap, int columnasBitmap, int x, int y, int xVelocidad, boolean vivo) {
        super(gameView, tiro, filasBitmap, columnasBitmap);

        this.x = x;
        this.y = y;
        this.xVelocidad = xVelocidad;
        this.vivo = vivo;
    }


    @Override
    protected void update() {


        if (x >= gameView.getWidth() - ancho - xVelocidad || x + xVelocidad <= 0) {
            vivo = false;
        } else {
            x = x + xVelocidad;
        }

        //frameColumnasActual = getNextAnimationColumn();

    }

    @Override
    public void onDraw(Canvas canvas) {
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
        return ++frameColumnasActual % columnasBitmap;
    }
}
