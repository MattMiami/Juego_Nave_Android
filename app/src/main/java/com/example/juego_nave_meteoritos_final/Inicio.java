package com.example.juego_nave_meteoritos_final;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Inicio {
    private GameView gameView;
    private Paint lapiz = new Paint();
    boolean iniciarJuego;

    public Inicio(GameView gameView, boolean iniciarJuego){
        this.gameView = gameView;
        this.iniciarJuego = iniciarJuego;
        lapiz.setColor(Color.WHITE);
        lapiz.setTextSize(60);
        lapiz.setTextAlign(Paint.Align.CENTER);

    }

    @SuppressLint("WrongCall")
    public void draw(Canvas canvas) {
        Bitmap asterotides = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.meteorito);
        //canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(asterotides, MainActivity.ancho/4, MainActivity.alto/6,null);
        canvas.drawText("Salva a la tierra",MainActivity.ancho/2,MainActivity.alto/6,lapiz);
        canvas.drawText("Pulsa para empezar",MainActivity.ancho/2,MainActivity.alto/8,lapiz);
    }

    public boolean isIniciarJuego() {
        return iniciarJuego;
    }

    public void setIniciarJuego(boolean iniciarJuego) {
        this.iniciarJuego = iniciarJuego;
    }
}
