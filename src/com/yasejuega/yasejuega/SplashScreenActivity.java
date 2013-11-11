package com.yasejuega.yasejuega;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class SplashScreenActivity extends Activity {

  private long splashDelay = 4000; //6 segundos

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_screen_activity);

    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();//Destruimos esta activity para prevenit que el usuario retorne aqui presionando el boton Atras.
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, splashDelay);//Pasado los 6 segundos dispara la tarea
  }

}