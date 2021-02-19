package com.example.juego_nave_meteoritos_final;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

public class ExplosionTemp {

    private float x;
    private float y;
    private Bitmap explosion;
    private int vida = 20;


    private List<ExplosionTemp> listaExpTemp;

    public ExplosionTemp(GameView gameView, Bitmap explosion, float x, float y, List<ExplosionTemp> listaExpTemp) {
        //Cuando le pasemos al parametro la posicion x e y,
        // haremos los siguientes calculos para posicionar la imagen justo en el centro del sprite de donde viene el evento
        this.x = Math.min(Math.max(x - explosion.getWidth() / 2f, 0), gameView.getWidth() - explosion.getWidth());
        this.y = Math.min(Math.max(y - explosion.getHeight() / 2f, 0), gameView.getHeight() - explosion.getHeight());

        this.explosion = explosion;
        this.listaExpTemp = listaExpTemp;
    }

    //Pintamos la explosion y actualizamos
    public void onDraw(Canvas canvas){
        update();
        canvas.drawBitmap(explosion, x, y, null);

    }

    //Disminuimos la vida del sprite hasta hacerlo desaparecer
    protected void update() {
        if (--vida < 1){
            listaExpTemp.remove(this);
        }
    }

}
