package e.dante.sts.Combos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.Event.Event;
import e.dante.sts.GlobalFunctions;
import e.dante.sts.R;

public class ComboListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<Combo> dataChild;

    public ComboListAdapter(Context context, ArrayList<Combo> dataChild) {
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Combo combo = (Combo) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.combo_child, parent, false);
        }
        TextView nameView = convertView.findViewById(R.id.combo_child_name_view);
        nameView.setText(combo.getUserId());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Combo combo = (Combo) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.combo_group, parent, false);
        }
        TextView noteView = convertView.findViewById(R.id.combo_group_notes_view);
        noteView.setText(combo.getNote());

        ListView cardListView = convertView.findViewById(R.id.combo_group_card_list_view);
        cardListView.setAdapter(new ComboAdapter(context, R.layout.item_combo, combo.getComboCards(), "Cards"));

        ListView relicListView = convertView.findViewById(R.id.combo_group_relic_list_view);
        relicListView.setAdapter(new ComboAdapter(context, R.layout.item_combo, combo.getComboRelics(), "Relics"));

        return convertView;
    }

    private class ComboAdapter extends ArrayAdapter<String> {
        private int resource;
        private List<String> combos;
        private String type;

        public ComboAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, String type) {
            super(context, resource, objects);
            this.resource = resource;
            this.combos = objects;
            this.type = type;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            final String comboName = combos.get(position);

            //TODO make this clickable
            TextView nameView = convertView.findViewById(R.id.combo_name_view);
            nameView.setText(comboName);

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
