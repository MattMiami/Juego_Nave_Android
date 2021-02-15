package com.example.juego_nave_meteoritos_final;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SpriteNave implements SensorEventListener {

    public static SensorManager sensorManager;
    public static Sensor sensor;
    float sensibilidadSensor;

    //Direcciones de las animaciones del sprite
    int[] DIRECTION_TO_ANIMATION_MAP = { 1, 0, 2 };

    private GameView gameView;
    private Bitmap nave;
    private static final int NAVE_FILAS = 3;
    private static final int NAVE_COLUMNAS = 3;
    private int xVelocidad = 20;
    private int yVelocidad = 20;
    private int x = 0;
    private int y = 0;
    private int frameActual = 0;
    private int ancho;
    private int alto;



    public SpriteNave(GameView gameView, Bitmap nave, Context context) {
        this.gameView = gameView;
        this.nave = nave;

        //Dividimo el Sprite de la nave entre sus fila y columnas para obetener las direfentes animaciones
        this.ancho = nave.getWidth() / NAVE_COLUMNAS;
        this.alto = nave.getHeight() / NAVE_FILAS;


        //Inicializamos el sensor manager y creamos una instancia del acelerometro, creamos un float para controlar la sensibilidad
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        sensibilidadSensor = (float) 3.5;

    }


    //Damos velocidad al objeto y marcamos los límites para que no salga de la pantalla
    private void update(){
        if (x >= MainActivity.ancho - ancho - xVelocidad || x + xVelocidad <= -ancho/200) {
            xVelocidad = -xVelocidad;
        }
        else {
            x = x + xVelocidad;
        }
        if (y > MainActivity.alto - alto - yVelocidad || y + yVelocidad < 0) {
            yVelocidad = -yVelocidad;
        }else{
            y = y + yVelocidad;
        }

        frameActual = ++frameActual % NAVE_COLUMNAS;
    }


    public void onDraw(Canvas canvas) {
        update();
        int srcX = frameActual * ancho;
        int srcY = getAnimationRow() * alto;
        Rect src = new Rect(srcX, srcY, srcX + ancho, srcY + alto);
        Rect dst = new Rect(x, y, x + ancho, y + alto);
        canvas.drawBitmap(nave, src , dst, null);
    }

    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xVelocidad, yVelocidad) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % NAVE_FILAS;
        return DIRECTION_TO_ANIMATION_MAP[direction];

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            //Le doy menos velocidad al movimiento de la nave en X que en el eje Y
            float x, y;
            x = Math.round(event.values[1]);
            if (x > 6) {
                x = (float) 6;
            } else if (x < -6) {
                x = (float) -6;
            }

            y = Math.round(event.values[0]);
            if (y > 9) {
                y = (float) 9;
            } else if (y < -9) {
                y = (float) -9;
            }

           this.xVelocidad = (int) (x * sensibilidadSensor);
           this.yVelocidad = (int) (y * sensibilidadSensor);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
