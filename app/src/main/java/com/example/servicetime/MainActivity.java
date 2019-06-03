package com.example.servicetime;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
  ServiceTimeData serviceTimeData1 = new ServiceTimeData();
  ServiceTimeData serviceTimeData2 = new ServiceTimeData();
  ServiceTimeData serviceTimeData3 = new ServiceTimeData();
  TextView textView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final Button action1 = findViewById(R.id.action1);
    final Button action2 = findViewById(R.id.action2);
    final Button action3 = findViewById(R.id.action3);
    Button discard1 = findViewById(R.id.discard1);
    Button discard2 = findViewById(R.id.discard2);
    Button discard3 = findViewById(R.id.discard3);
    Button clear = findViewById(R.id.button3);
    Button copy = findViewById(R.id.button);

    textView = findViewById(R.id.textView);

    loadFromMemory();

    action1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonAction(serviceTimeData1, action1);
      }
    });

    discard1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        serviceTimeData1.setStarted(false);
        resetButton(action1);
      }
    });

    action2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonAction(serviceTimeData2, action2);
      }
    });

    discard2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        serviceTimeData2.setStarted(false);
        resetButton(action2);
      }
    });

    action3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        buttonAction(serviceTimeData3, action3);
      }
    });

    discard3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        serviceTimeData3.setStarted(false);
        resetButton(action3);
      }
    });

    clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User clicked OK button
            textView.setText("");
            serviceTimeData1.setStarted(false);
            serviceTimeData2.setStarted(false);
            serviceTimeData3.setStarted(false);
            saveToMemory();
            resetButton(action1);
            resetButton(action2);
            resetButton(action3);
          }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

      }
    });

    copy.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", textView.getText());
        clipboard.setPrimaryClip(clip);
      }
    });
  }

  void buttonAction(ServiceTimeData serviceTimeData, Button button) {
    if(serviceTimeData.isStarted()) {
      long service = System.currentTimeMillis() - serviceTimeData.start_time;
      textView.append(service+",\n");
      saveToMemory();
      resetButton(button);
      serviceTimeData.setStarted(false);
    } else {
      serviceTimeData.setStart_time(setButton(button));
      serviceTimeData.setStarted(true);
    }
  }

  void resetButton(Button target) {
    target.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreenTint)));
    target.setText(getText(R.string.start));
  }

  long setButton(Button target) {
    target.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorRedTint)));
    target.setText(getText(R.string.stop));
    return System.currentTimeMillis();
  }

  void saveToMemory() {
    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
    pref.edit().putString("service_time", textView.getText().toString()).apply();
  }

  @SuppressLint("SetTextI18n")
  void loadFromMemory() {
    SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
    String data = pref.getString("service_time", "");
    if (data != null && !data.equalsIgnoreCase("")) {
      textView.setText(data.trim()+'\n');
    }
  }

  private class ServiceTimeData {
    boolean started = false;
    long start_time;

    boolean isStarted() {
      return started;
    }

    void setStart_time(long start_time) {
      this.start_time = start_time;
    }

    void setStarted(boolean started) {
      this.started = started;
    }
  }
}
