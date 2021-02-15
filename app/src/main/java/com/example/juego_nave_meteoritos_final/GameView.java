package com.example.juego_nave_meteoritos_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {


    private final Bitmap spriteNave;
    private final Bitmap spriteTiro;
    private Fondo fondoUno;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private int x = 0;
    private int xVelocidad = 10;
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
        spriteNave = BitmapFactory.decodeResource(getResources(), R.drawable.sprite1);
        nave = new SpriteNave(this, spriteNave, context);

        spriteTiro = BitmapFactory.decodeResource(getResources(), R.drawable.tirosazules);
        tiro = new Tiro(this, spriteTiro, context);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(fondoUno.imagenFondo, fondoUno.x, fondoUno.y, null);
        nave.onDraw(canvas);
        tiro.onDraw(canvas);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (event.getX() > MainActivity.ancho / 2) {


                }
        }
        return true;
    }


}
