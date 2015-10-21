package ru.thevhod.picquest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.thevhod.picquest.R;

public class ChooseTheEndActivity extends Activity {

    public static final void startActivity(Context context) {
        Intent i = new Intent(context, ChooseTheEndActivity.class);
        context.startActivity(i);
    }

    private Button firstEndButton;
    private Button secondEndButton;
    private Button repeatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_the_end);
        initView();
        initListeners();
    }

    private void initView() {
        firstEndButton = (Button) findViewById(R.id.first_end_button);
        secondEndButton = (Button) findViewById(R.id.second_end_button);
        repeatButton = (Button) findViewById(R.id.repeat_button);
    }

    private void initListeners() {
        firstEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End1VideoActivity.startActivity(ChooseTheEndActivity.this);
            }
        });
        secondEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                End2VideoActivity.startActivity(ChooseTheEndActivity.this);
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartVideoActivity.startActivity(ChooseTheEndActivity.this);
            }
        });
    }
}
