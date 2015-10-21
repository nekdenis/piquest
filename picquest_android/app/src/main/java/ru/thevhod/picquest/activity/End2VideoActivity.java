package ru.thevhod.picquest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import ru.thevhod.picquest.R;

public class End2VideoActivity extends Activity {

    public static final void startActivity(Context context) {
        Intent i = new Intent(context, End2VideoActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
    }

    private void initView() {
        VideoView view = (VideoView) findViewById(R.id.video_view);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.quest_end_second;
        view.setVideoURI(Uri.parse(path));
        view.start();
    }
}