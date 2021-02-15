package com.example.juego_nave_meteoritos_final;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {

    protected int filasBitmap = 3;
    protected int columnasBitmap = 3;
    protected Rect src;
    protected Rect dst;

    protected GameView gameView;
    protected Bitmap bitmap;
    protected int x = 0;
    protected int y = 0;
    protected int xVelocidad;
    protected int yVelocidad;
    protected int frameColumnasActual = 0;
    protected int frameFilasActual = 0;
    protected int ancho;
    protected int alto;
    protected boolean vivo;

    public Sprite(GameView gameView, Bitmap bitmap, int filasBitmap, int columnasBitmap) {
        //Depende de cada sprite
        this.filasBitmap = filasBitmap;
        this.columnasBitmap = columnasBitmap;

        //Lo mismo para todos los sprites
        this.ancho = bitmap.getWidth() / columnasBitmap;
        this.alto = bitmap.getHeight() / filasBitmap;
        this.gameView = gameView;
        this.bitmap = bitmap;

        // Inicializa los rectangulos donde se va a dibujar la imagen
        int srcX = frameColumnasActual * ancho;
        int srcY = frameFilasActual * alto;
        src = new Rect(srcX, srcY, srcX + ancho, srcY + alto);
        dst = new Rect(x, y, x + ancho, y + alto);

        this.x = 30;
        this.y = 30;

        xVelocidad = 0;
        yVelocidad = 0;
        vivo = true;
    }

    public int getFilasBitmap() {
        return this.filasBitmap;
    }
    public int getColumnasBitmap() {
        return this.columnasBitmap;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
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

    public boolean isVivo() {
        return vivo;
    }
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    protected void update() {
        if (x >= gameView.getWidth() - ancho - xVelocidad || x + xVelocidad <= 0) {
            xVelocidad = -xVelocidad;
        }
        x = x + xVelocidad;
        if (y >= gameView.getHeight() - alto - yVelocidad || y + yVelocidad <= 0) {
            yVelocidad = -yVelocidad;
        }
        y = y + yVelocidad;

        frameColumnasActual = ++frameColumnasActual % columnasBitmap;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = frameColumnasActual * ancho;
        int srcY = frameFilasActual * alto;
        src.left=srcX;
        src.top = srcY;
        src.right = srcX + ancho;
        src.bottom = srcY + alto;
        dst.left=x;
        dst.top=y;
        dst.right=x+ ancho;
        dst.bottom=y+ alto;
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    protected int getNextAnimationRow() {
        return 0;
    }

    protected int getNextAnimationColumn() {
        return 0;
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + ancho && y2 > y && y2 < y + alto;
    }

    public boolean isCollition(Sprite sprite2) {
        // Para detectar la colision entre dos imagenes
        if(
                this.x < sprite2.getX() + sprite2.getAncho() &&
                        this.x + this.ancho > sprite2.getX() &&
                        this.y + this.alto > sprite2.getY() &&
                        this.y < sprite2.getY() + sprite2.getAlto()
        )
        {
            return true;
        }
        else {
            return false;
        }
    }
}
