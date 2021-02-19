package com.example.juego_nave_meteoritos_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static com.example.juego_nave_meteoritos_final.SpriteNave.event;
import static com.example.juego_nave_meteoritos_final.SpriteNave.sensorManager;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    DisplayMetrics metrics = new DisplayMetrics();
    public static int ancho;
    public static int alto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Para reproducir a pantalla completa
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Para obtener el alto y el ancho de la pantalla.
        ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        ancho = metrics.widthPixels;
        alto = metrics.heightPixels;

        //Le pasamos a nuestro objeto gameView el contexto
        gameView = new GameView(this);

        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null && event != null) {
            sensorManager.unregisterListener(event);
        }
        gameView.pausarMusica();
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && event != null) {
            sensorManager.registerListener(event, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
        gameView.reproducirMusica();



    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sensorManager != null && event != null) {
            sensorManager.unregisterListener(event);
        }
        gameView.detenerMusica();
        gameView.liberarRecursosMusica();
        finish();
    }


}