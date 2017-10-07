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

        parser.parseFrom("//unused data {data}")
                .subscribe(data ->
                        Toast.makeText(this, "Parsed", Toast.LENGTH_SHORT).show()
                );
    }
}
