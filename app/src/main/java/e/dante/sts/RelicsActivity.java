package e.dante.sts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelicsActivity extends AppCompatActivity implements RelicHelper.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relics);

        new RelicHelper().getRelics(this);
    }

    @Override
    public void gotRelics(ArrayList<Relic> relics) {
        ListView list_view = findViewById(R.id.relic_list_view);
        list_view.setAdapter(new RelicsAdapter(this, R.layout.relic_item, relics));
        list_view.setOnItemClickListener(new RelicsItemClickListener());
    }

    @Override
    public void gotRelicsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private class RelicsAdapter extends ArrayAdapter<Relic> {
        private int resource;
        private ArrayList<Relic> relics;

        public RelicsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Relic> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.relics = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            Relic item = relics.get(position);

            String name = item.getName();
            String description = item.getDescription();
            String imgUrl = item.getImgUrl();

            ImageView relic_img_view = convertView.findViewById(R.id.relic_img);
            TextView nameView = convertView.findViewById(R.id.relic_name);
            TextView descriptionView = convertView.findViewById(R.id.relic_description);

            nameView.setText(name);
            descriptionView.setText(description);
            Log.d("gotCards", "img url: " + imgUrl);
            Picasso.get().load(imgUrl).into(relic_img_view);

            return convertView;
        }
    }

    private class RelicsItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //TODO make items clickable
        }
    }
}
