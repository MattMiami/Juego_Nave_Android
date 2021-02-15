package com.example.juego_nave_meteoritos_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {

    private Bitmap spriteNave;
    private Bitmap spriteTiro;
    private Fondo fondoUno;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private SpriteNave nave;
    private Tiro tiro;


    public GameView(Context context) {
        super(context);

        gameLoopThread = new GameLoopThread(GameView.this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("WrongCall")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {

                spriteTiro = BitmapFactory.decodeResource(getResources(), R.drawable.tirogrande);
                crearNave();
                tiroNave(true);

                gameLoopThread.start();
                gameLoopThread.setEjecucion(true);

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setEjecucion(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });

        fondoUno = new Fondo(MainActivity.ancho, MainActivity.alto, getResources());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(fondoUno.imagenFondo, fondoUno.x, fondoUno.y, null);
        nave.onDraw(canvas);
        tiro.onDraw(canvas);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        //Si tocamos en la mitad derecha de la pantalla dispararemos
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (event.getX() > MainActivity.ancho / 2) {
                    tiroNave(true);
                }
        }
        return true;
    }

    //Creamos la nave
    private void crearNave(){
        spriteNave = BitmapFactory.decodeResource(getResources(), R.drawable.prueba2);
        this.nave = new SpriteNave(this, spriteNave, 3,3,getContext());
    }

    //Creamos el tiro
    private void tiroNave(boolean vivo){
        int columnas = 2;
        int filas = 1;

        int x = nave.getX()+nave.getAncho()/*/2 - (spriteTiro.getWidth()/columnas)*/;
        int y = nave.getY()+nave.getAlto()/2/* - (spriteTiro.getHeight()/filas)*/;
        int velocidadXtiro = 30;
        this.tiro = new Tiro(this,spriteTiro,filas,columnas,x,y,velocidadXtiro,vivo);
//        Log.e("eje", "Varoles creado Y " + y);

    }


}
