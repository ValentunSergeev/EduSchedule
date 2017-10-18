package com.valentun.eduschedule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.valentun.parser.Parser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parser parser = new Parser();

        String json = TestUtils.getTestData(this);

        parser.parseFrom(json)
                .subscribe(school -> {
                    Toast.makeText(this, String.valueOf("Parse time: " +
                                    parser.getParseTime() / 1000.0 +
                                    "seconds"),
                            Toast.LENGTH_LONG).show();
                });
    }
}
