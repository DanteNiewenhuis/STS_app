package e.dante.sts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.keywords_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.potions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RelicsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.guide_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.database_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataScraper().execute();
            }
        });
    }
}
