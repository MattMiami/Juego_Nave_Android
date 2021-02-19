package com.example.juego_nave_meteoritos_final;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Fondo {

    int x = 0, y = 0;
    Bitmap imagenFondo;

    Fondo(int pantallaX, int pantallaY, Resources resources) {

        imagenFondo = BitmapFactory.decodeResource(resources, R.drawable.tierra);
        imagenFondo = Bitmap.createScaledBitmap(imagenFondo, pantallaX, pantallaY, false);

    }
}
