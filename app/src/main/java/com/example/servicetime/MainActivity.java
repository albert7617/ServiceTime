package com.example.servicetime;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  boolean started = false;
  long start_time;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final Button add = findViewById(R.id.start_stop);
    Button discard = findViewById(R.id.discard);
    Button clear = findViewById(R.id.button3);
    Button copy = findViewById(R.id.button);
    final TextView textView = findViewById(R.id.textView);


    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(started) {
          long service = System.currentTimeMillis() - start_time;
          textView.append(service+",\n");
          add.setText("Start");
          started = false;
        } else {
          start_time = System.currentTimeMillis();
          add.setText("Stop");
          started = true;
        }
      }
    });

    discard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        started = false;
        add.setText("Start");
      }
    });

    clear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        textView.setText("");
        started = false;
        add.setText("Start");
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
}
