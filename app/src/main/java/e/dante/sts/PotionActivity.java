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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PotionActivity extends AppCompatActivity implements PotionHelper.Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potion);

        new PotionHelper().getPotions(this);
    }

    @Override
    public void gotPotions(ArrayList<Potion> potions) {
        ListView list_view = findViewById(R.id.potion_list_view);
        list_view.setAdapter(new PotionsAdapter(this, R.layout.potion_item, potions));
    }

    @Override
    public void gotPotionsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private class PotionsAdapter extends ArrayAdapter<Potion> {
        private int resource;
        private ArrayList<Potion> potions;

        public PotionsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Potion> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.potions = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            Potion item = potions.get(position);

            String name = item.getName();
            String imgUrl = item.getImgUrl();
            String description = item.getDescription();

            ImageView imgView = convertView.findViewById(R.id.potion_img);
            TextView nameView = convertView.findViewById(R.id.potion_name);
            TextView desView = convertView.findViewById(R.id.potion_description);

            nameView.setText(name);
            desView.setText(description);
            Picasso.get().load(imgUrl).into(imgView);

            return convertView;
        }
    }
}
