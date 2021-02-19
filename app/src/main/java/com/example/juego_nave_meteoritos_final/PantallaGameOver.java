package com.example.juego_nave_meteoritos_final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PantallaGameOver {

    int x = 0, y = 0;
    boolean finalJuego;
    Bitmap gameover;


    /*He creado una clase para crear "movimiento" en el fondo del juego
         Al constructor le pasamos los ejes 'x' e 'y' además del drawable de mi imagen de fondo
     */
    PantallaGameOver(int pantallaX, int pantallaY, Resources resources, boolean finalJuego) {

        gameover = BitmapFactory.decodeResource(resources, R.drawable.gameoverfondo);
        gameover = Bitmap.createScaledBitmap(gameover, pantallaX, pantallaY, false);

        this.finalJuego = finalJuego;


    }

    public boolean isFinalJuego() {
        return finalJuego;
    }

    public void setFinalJuego(boolean finalJuego) {
        this.finalJuego = finalJuego;
    }
}
