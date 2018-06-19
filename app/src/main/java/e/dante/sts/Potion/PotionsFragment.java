package e.dante.sts.Potion;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import e.dante.sts.R;

public class PotionsFragment extends Fragment implements PotionHelper.Callback {
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_potion, container, false);

        new PotionHelper().getPotions(this);

        return myView;
    }

    @Override
    public void gotPotions(ArrayList<Potion> potions) {
        ListView list_view = myView.findViewById(R.id.potion_list_view);
        list_view.setAdapter(new PotionsAdapter(getContext(), R.layout.item_potion, potions));
    }

    @Override
    public void gotPotionsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
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

            ImageView imgView = convertView.findViewById(R.id.card_item_img);
            TextView nameView = convertView.findViewById(R.id.card_item_name);
            TextView desView = convertView.findViewById(R.id.card_item_des);

            nameView.setText(name);
            desView.setText(description);
            Picasso.get().load(imgUrl).into(imgView);

            return convertView;
        }
    }
}
