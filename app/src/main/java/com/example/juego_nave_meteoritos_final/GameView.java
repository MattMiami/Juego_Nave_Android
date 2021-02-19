package com.example.juego_nave_meteoritos_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView {

    private Inicio inicio;
    private Fondo fondoUno;
    private  PantallaGameOver gameOver;
    private PantallaWin winGame;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    //Elementos
    private SpriteNave nave;
    private Tiro tiro;
    private Bitmap spriteNave;
    private Bitmap spriteTiro;
    public static List<Meteoritos> listaMeteoritos = new ArrayList<Meteoritos>();
    private Bitmap explosion;
    private Bitmap explosionNave;
    public static List<ExplosionTemp> listaExplosiones = new ArrayList<>();
    //Sonido
    private int sonidoTiro, sonidoExplosion, sonidoExpNave, sonidoGameOver, sonidoWin;
    private SoundPool sonidos;
    private MediaPlayer mediaPlayer;
    //Contador puntuacion
    private int puntos = 0;
    private Paint lapiz;
    private Typeface typeface = Typeface.create("Calibri",Typeface.BOLD);

    public GameView(Context context) {
        super(context);

        lapiz =  new Paint();
        //Musica de fondo
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context,R.raw.principal);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
        //Sonidos cortos
        sonidos = new SoundPool(4, AudioManager.STREAM_MUSIC,0);
        sonidoTiro = sonidos.load(context, R.raw.tiro,1);
        sonidoExplosion = sonidos.load(context, R.raw.explosion,2);
        sonidoExpNave = sonidos.load(context, R.raw.explosionnave,3);
        sonidoGameOver = sonidos.load(context, R.raw.gameover,4);
        sonidoWin = sonidos.load(context, R.raw.win,4);

        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("WrongCall")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                //creamos los objetos que van a intervenir
                inicio = new Inicio(GameView.this, true);
                fondoUno = new Fondo(MainActivity.ancho, MainActivity.alto, getResources());
                spriteTiro = BitmapFactory.decodeResource(getResources(), R.drawable.tirogrande);
                explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
                explosionNave = BitmapFactory.decodeResource(getResources(), R.drawable.explosionnave);

                inicio.setIniciarJuego(true);
                crearNave();
                tiroNave(false);
                crearMeteoritos();
                gameOver(false);
                winGame(false);

                    //Lanzamos el game loop del juego
                    gameLoopThread.start();
                    gameLoopThread.setEjecucion(true);

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

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
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        if (!inicio.iniciarJuego) {

            if (gameOver.finalJuego) {
                canvas.drawBitmap(gameOver.gameover, gameOver.x, gameOver.y, null);
                listaExplosiones.clear();

            } else if (winGame.ganarJuego) {
                canvas.drawBitmap(winGame.win, winGame.x, winGame.y, null);
                listaExplosiones.clear();
            } else {
                canvas.drawBitmap(fondoUno.imagenFondo, fondoUno.x, fondoUno.y, null);

                //Pintamos la nave simpre que este viva
                if (nave.vivo) {
                    nave.onDraw(canvas);

                }
                //Pintamos el tiro de la nave si esta se encuentra disponible
                if (tiro.vivo) {
                    tiro.onDraw(canvas);
                }

                   //Recorremos la lista de meteoritos, pintando los que esten en la lista solamente
                   for (Meteoritos m : listaMeteoritos) {
                       m.onDraw(canvas);
                       //Si alguno meteorito se sale de la pantalla sin ser destruido perdemos el juego
                       if (m.x <= this.getX()-100) {
                           gameOver(true);
                           mediaPlayer.stop();
                           puntos = 0;
                       }
                   }

                //Verificamos las colisiones entre nuestros objetos, para no dibujar los que hayan desaparecido
                colisiones();

                //Recorremos la lista de Sprites temporales para dibujarlos solo cuando sea necesario
                for (int i = listaExplosiones.size() - 1; i >= 0; i--) {
                    listaExplosiones.get(i).onDraw(canvas);
                }

                //Puntos
                if (puntos == 0) {
                } else if (puntos > 0 && puntos <= 200) {
                    lapiz.setColor(Color.WHITE);
                    lapiz.setTextAlign(Paint.Align.CENTER);
                    lapiz.setTypeface(typeface);
                    lapiz.setTextSize(30);
                    canvas.drawText("+" + puntos, MainActivity.ancho / 8, MainActivity.alto / 8, lapiz);
                } else if (puntos > 200 && puntos <= 500) {
                    lapiz.setColor(Color.YELLOW);
                    lapiz.setTextAlign(Paint.Align.CENTER);
                    lapiz.setTypeface(typeface);
                    lapiz.setTextSize(30);
                    canvas.drawText("+" + puntos, MainActivity.ancho / 8, MainActivity.alto / 8, lapiz);
                } else {
                    lapiz.setColor(Color.GREEN);
                    lapiz.setTextAlign(Paint.Align.CENTER);
                    lapiz.setTypeface(typeface);
                    lapiz.setTextSize(30);
                    canvas.drawText("+" + puntos, MainActivity.ancho / 8, MainActivity.alto / 8, lapiz);
                }
            }
        }else{
            inicio.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Si tocamos en la mitad derecha de la pantalla dispararemos
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (!tiro.vivo && nave.vivo) {
                    if (event.getX() > MainActivity.ancho / 2) {
                        tiroNave(true);
                        sonidos.play(sonidoTiro,0,1,0,0,1);
                    }
                }


            case MotionEvent.ACTION_BUTTON_PRESS:
                //Si finalizar el juego es True
                if(gameOver.finalJuego){
                    listaMeteoritos.clear();
                    crearNave();
                    tiroNave(false);
                    crearMeteoritos();
                    gameOver(false);
                    mediaPlayer = MediaPlayer.create(getContext(),R.raw.principal);
                    mediaPlayer.start();
                }
                //Si ganar el Juego es true
                if (winGame.ganarJuego){
                    listaMeteoritos.clear();
                    crearNave();
                    tiroNave(false);
                    crearMeteoritos();
                    winGame(false);
                    mediaPlayer = MediaPlayer.create(getContext(),R.raw.principal);
                    mediaPlayer.start();
                }
                if (inicio.iniciarJuego){
                    inicio.iniciarJuego=false;
                    listaMeteoritos.clear();
                    crearNave();
                    tiroNave(false);
                    crearMeteoritos();
                    winGame(false);

                }

        }
        return true;
    }

    //Creamos la nave
    private void crearNave() {
        spriteNave = BitmapFactory.decodeResource(getResources(), R.drawable.prueba2);
        this.nave = new SpriteNave(this, spriteNave, 3, 3, getContext());
    }

    //Creamos el tiro, pasamos un booleano como parámetro para controlar el tiro sobre el Canvas
    private void tiroNave(boolean vivo) {
        int columnas = 1;
        int filas = 1;
        //situamos el tiro cerca de la cabeza de la nave
        int x = nave.getX() + nave.getAncho();
        int y = nave.getY() + nave.getAlto() / 2;
        int velocidadXtiro = 30;
        this.tiro = new Tiro(this, spriteTiro, filas, columnas, x, y, velocidadXtiro, vivo);

    }

    //Creamos meteoritos y los añadimos a la lista, asignado nuestros drawables
    private void crearMeteoritos() {
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito2, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito3, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito4, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito5, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito2, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito3, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito4, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito5, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito2, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito3, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito4, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito5, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito2, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito3, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito4, true));
        listaMeteoritos.add(crearMeteorito(R.drawable.meteorito5, true));
    }

    //Para crear un meteorito
    private Meteoritos crearMeteorito(int resource, boolean vivo) {
        int columnas = 1;
        int filas = 1;
        Bitmap meteoritos = BitmapFactory.decodeResource(getResources(), resource);
        return new Meteoritos(this, meteoritos, filas, columnas, vivo);
    }

    private void colisiones() {
        synchronized (getHolder()) {
            //Si un tiro colisiona con un meteorito
            if (tiro.vivo) {
                for (int i = listaMeteoritos.size() - 1; i >= 0; i--) {
                    Meteoritos m = listaMeteoritos.get(i);
                    if (m.colisionaCon(tiro.x, tiro.y)) {
                        tiro.setVivo(false);
                        listaMeteoritos.remove(m);
                        listaExplosiones.add(new ExplosionTemp(this, explosion,tiro.x, tiro.y, listaExplosiones));
                        sonidos.play(sonidoExplosion,1,1,0,0,1);
                        //Puntuación, si la puntuacion es igual a 900 ganamos el juego
                        puntos += 100;
                        if (puntos == 900){
                            winGame(true);
                            puntos = 0;
                            mediaPlayer.stop();
                            sonidos.play(sonidoWin,1,1,0,0,1);
                        }
                        break;
                    }
                }
            }

            //si la nave colisiona con un meteorito
            if (nave.vivo) {
                for (int i = 0; i < listaMeteoritos.size(); i++) {
                    Meteoritos m = listaMeteoritos.get(i);
                    if (nave.colisionaCon(m.x, m.y)) {
                        nave.setVivo(false);
                        tiro.setVivo(false);
                        m.setVivo(false);
                        listaExplosiones.add(new ExplosionTemp(this, explosionNave,m.x, m.y,listaExplosiones));
                        sonidos.play(sonidoExpNave,1,1,1,0,1);
                        gameOver(true);
                        puntos = 0;
                        sonidos.play(sonidoGameOver,1,1,0,0,1);
                        mediaPlayer.stop();
                        break;
                    }
                }
            }
        }
    }



    private void gameOver(boolean b) {
        int x = MainActivity.ancho;
        int y = MainActivity.alto;
        this.gameOver = new PantallaGameOver(x, y,getResources(), b);
    }

    private void winGame(boolean b) {
        int x = MainActivity.ancho;
        int y = MainActivity.alto;
        this.winGame = new PantallaWin(x, y,getResources(), b);
    }

    // Controladores de la musica
    public void reproducirMusica(){
        mediaPlayer.start();
    }

    public void pausarMusica(){
        mediaPlayer.pause();
    }

    public void liberarRecursosMusica(){
        mediaPlayer.release();
        sonidos.release();
    }

    public void detenerMusica(){
        mediaPlayer.stop();
    }

}
