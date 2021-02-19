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

public class SpriteNave extends Sprite implements SensorEventListener {

    public static SensorEventListener event;
    public static SensorManager sensorManager;
    public static Sensor sensor;
    float sensibilidadSensor;
    Bitmap nave;


    public SpriteNave(GameView gameView, Bitmap nave, int filasBitmap, int columnasBitmap, Context context) {
        super(gameView, nave, filasBitmap, columnasBitmap);

        this.nave = nave;
        //Posicion de la nave al empezar
        x = (gameView.getWidth() - ancho) / 4;
        y = (gameView.getHeight() - alto) / 2;

        //Inicializamos el sensor manager y creamos una instancia del acelerometro, creamos un float para controlar la sensibilidad
        event = this;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        //Con la sensibilidad podremos controlar la velocidad de la nave como veremos un poco más abajo
        sensibilidadSensor = (float) 5;

    }



    @Override
    protected void update() {
        //Limitamos el movimiento de la nave al ancho y al alto de la pantalla
        if (x >= gameView.getWidth() - ancho / 2 - xVelocidad || x + xVelocidad <= -ancho / 2) {
            xVelocidad = -xVelocidad;
        } else {
            x = x + xVelocidad;
        }
        if (y > gameView.getHeight() - alto - yVelocidad || y + yVelocidad < 0) {
            yVelocidad = -yVelocidad;
        } else {
            y = y + yVelocidad;
        }
        frameColumnasActual = getNextAnimationRow();
    }

    @Override
    protected int getNextAnimationRow() {
        //Alternamos las animaciones del sprite
        int[] DIRECCION = {1, 0, 2};
        double dirDouble = (Math.atan2(xVelocidad, yVelocidad) / (Math.PI / 2) + 2);
        int direccion = (int) Math.round(dirDouble) % filasBitmap;
        return DIRECCION[direccion];

    }

    @Override
    public void onDraw(Canvas canvas) {
        //actualizamos el "dibujo de la nave, pintandolo donde se encuetre en ese momento"
        update();
        int srcX = frameColumnasActual * ancho;
        int srcY = getNextAnimationRow() * alto;
        Rect src = new Rect(srcX, srcY, srcX + ancho, srcY + alto);
        Rect dst = new Rect(x, y, x + ancho, y + alto);
        canvas.drawBitmap(nave, src, dst, null);
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
            //Aqui igualamos la velocidad a el movimiento del objeto entre los ejex X e Y por la sensibilidadSensor.
            this.xVelocidad = (int) (x * sensibilidadSensor);
            this.yVelocidad = (int) (y * sensibilidadSensor);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public boolean colisionaCon(float x2, float y2) {
        return x2 > x && x2 < x + ancho && y2 > y && y2 < y + alto;
    }
}
