package e.dante.sts.Opinions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.R;

/*
    This is an adapter for the expendable list of opinions.
 */

public class OpinionListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Opinion> dataChild;

    public OpinionListAdapter(Context context, ArrayList<Opinion> dataChild) {
        this.context = context;
        this.dataChild = dataChild;
    }

    @Override
    public int getGroupCount() {
        return this.dataChild.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.dataChild.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dataChild.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    // Create the groupView, this is the object you will see when it is not clicked
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Opinion opinion = (Opinion) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.opinion_child, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.combo_child_name_view);
        nameView.setText(opinion.getUserName());

        TextView scoreView = convertView.findViewById(R.id.combo_child_rating_score);

        if (isExpanded) {
            ((ImageView) convertView.findViewById(R.id.opinion_open_indicator)).
                    setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
        } else {
            ((ImageView) convertView.findViewById(R.id.opinion_open_indicator)).
                    setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        }


        if (opinion.getScore() != null) {
            scoreView.setText(opinion.getScore() + "/5");
        }
        else {
            scoreView.setText("No Score");
            ((ImageView) convertView.findViewById(R.id.combo_child_rating_star)).setImageResource(R.drawable.star_empty);
        }
        return convertView;
    }

    @Override
    // Create the childview, this is the object that will be shown when the parent is clicked
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Opinion opinion = (Opinion) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.opinion_group, parent, false);
        }
        TextView noteView = convertView.findViewById(R.id.global_opinions_notes_text_view);
        noteView.setText(opinion.getNote());

        Log.d("getChildView", "length: " + opinion.getComboCards().size());
        Log.d("getChildView", "length: " + opinion.getComboRelics().size());
        Log.d("getChildView", "length: " + opinion.getAntiComboCards().size());
        Log.d("getChildView", "length: " + opinion.getAntiComboRelics().size());

        ListView comboCardsList = convertView.findViewById(R.id.global_opinions_combo_cards_list);
        ListView comboRelicsList = convertView.findViewById(R.id.global_opinions_combo_relics_list);
        ListView antiComboCardsList = convertView.findViewById(R.id.global_opinions_anti_combo_cards_list);
        ListView antiComboRelicsList = convertView.findViewById(R.id.global_opinions_anti_combo_relics_list);

        comboListAdapter comboCardsAdapter = new comboListAdapter(context, R.layout.item_combo_simple, opinion.getComboCards());
        comboListAdapter comboRelicsAdapter = new comboListAdapter(context, R.layout.item_combo_simple, opinion.getComboRelics());
        comboListAdapter antiComboCardsAdapter = new comboListAdapter(context, R.layout.item_combo_simple, opinion.getAntiComboCards());
        comboListAdapter antiComboRelicsAdapter = new comboListAdapter(context, R.layout.item_combo_simple, opinion.getAntiComboRelics());

        comboCardsList.setAdapter(comboCardsAdapter);
        comboRelicsList.setAdapter(comboRelicsAdapter);
        antiComboCardsList.setAdapter(antiComboCardsAdapter);
        antiComboRelicsList.setAdapter(antiComboRelicsAdapter);

        return convertView;
    }

    private class comboListAdapter extends ArrayAdapter {
        private int resource;
        private List<String> opinions;

        public comboListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.opinions = objects;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            String item = opinions.get(position);

            TextView nameView = convertView.findViewById(R.id.combo_name_view);
            nameView.setText(item);

//            nameView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    gFunctions.getInfo(comboName, type);
//                }
//            });
            return convertView;
        }
    }
}
