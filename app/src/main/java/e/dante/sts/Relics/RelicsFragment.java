package e.dante.sts.Relics;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import e.dante.sts.R;

public class RelicsFragment extends Fragment implements RelicHelper.Callback {
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_relics, container, false);

        new RelicHelper().getRelics(this);
        return myView;
    }

    @Override
    public void gotRelics(ArrayList<Relic> relics) {
        ListView list_view = myView.findViewById(R.id.relic_list_view);
        list_view.setAdapter(new RelicsAdapter(getContext(), R.layout.item_relic, relics));
        list_view.setOnItemClickListener(new RelicsItemClickListener());
    }

    @Override
    public void gotRelicsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
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

            ImageView relic_img_view = convertView.findViewById(R.id.card_item_img);
            TextView nameView = convertView.findViewById(R.id.card_item_name);
            TextView descriptionView = convertView.findViewById(R.id.card_item_des);

            nameView.setText(name);
            descriptionView.setText(description);
            Log.d("gotCards", "img url: " + imgUrl);
            Picasso.get().load(imgUrl).into(relic_img_view);

            return convertView;
        }
    }

    private class RelicsItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.d("relic listener", "init");
//
//            Intent intent = new Intent(RelicsActivity.this, RelicDetailActivity.class);
//            intent.putExtra("relic", (Relic) parent.getItemAtPosition(position));
//
//            Log.d("relic listener", "start intent");
//            startActivity(intent);
        }
    }
}
