package e.dante.sts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        findViewById(R.id.keywords_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, ExpandableActivity.class);
                intent.putExtra("type", "keywords");
                startActivity(intent);
            }
        });

        findViewById(R.id.potions_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, ExpandableActivity.class);
                intent.putExtra("type", "potions");
                startActivity(intent);
            }
        });

        findViewById(R.id.events_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, ExpandableActivity.class);
                intent.putExtra("type", "events");
                startActivity(intent);
            }
        });
    }
}
