package app.android.box.waveprotocol.org.androidwave.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import app.android.box.waveprotocol.org.androidwave.R;

/**
 * Created by roellk on 8/9/2015.
 */
public class SearchActivity extends Activity{
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        back = (ImageButton)findViewById(R.id.button_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewWavectivity = new Intent("app.android.box.waveprotocol.org.androidwave.INBOXACTIVITY");
                startActivity(openNewWavectivity);
                //test
                //setContentView(R.layout.activity_new_wave);
            }
        });
    }
}
