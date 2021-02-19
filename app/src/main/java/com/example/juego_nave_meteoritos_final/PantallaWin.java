package com.example.juego_nave_meteoritos_final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PantallaWin {

    int x = 0, y = 0;
    boolean ganarJuego;
    Bitmap win;


    /*He creado una clase para crear "movimiento" en el fondo del juego
         Al constructor le pasamos los ejes 'x' e 'y' además del drawable de mi imagen de fondo
     */
    PantallaWin(int pantallaX, int pantallaY, Resources resources, boolean finalJuego) {

        win = BitmapFactory.decodeResource(resources, R.drawable.win);
        win = Bitmap.createScaledBitmap(win, pantallaX, pantallaY, false);

        this.ganarJuego = finalJuego;


    }

    public boolean isFinalJuego() {
        return ganarJuego;
    }

    public void setFinalJuego(boolean finalJuego) {
        this.ganarJuego = finalJuego;
    }
}
