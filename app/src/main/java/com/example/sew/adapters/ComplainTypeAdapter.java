package com.example.sew.adapters;

import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.models.ComplainType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComplainTypeAdapter extends BaseMyAdapter {


    private ArrayList<ComplainType> complainTypes;
   private ArrayList<String> selectedTypeId = new ArrayList<>();

    public ComplainTypeAdapter(BaseActivity activity, ArrayList<ComplainType> complainTypes) {
        super(activity);
        this.complainTypes = complainTypes;
    }

    @Override
    public int getCount() {
        return complainTypes.size();
    }

    @Override
    public ComplainType getItem(int i) {
        return complainTypes.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ComplainType complainType = getItem(position);
        if (convertView == null)
            convertView = generateNewView(R.layout.cell_complain_ype);
        TextView txtComplainType = (TextView) convertView.findViewById(R.id.txtComplainType);
        final ImageView imgUncheck = (ImageView) convertView.findViewById(R.id.imgUnCheck);

        imgUncheck.setTag(position);

        if (isSelected(position)) {
            imgUncheck.setColorFilter(ContextCompat.getColor(getActivity(), R.color.switch_color), PorterDuff.Mode.SRC_IN);
            imgUncheck.setImageResource(R.drawable.checked);
        } else {
            imgUncheck.setColorFilter(getActivity().getAppIconColor(), PorterDuff.Mode.SRC_IN);
            imgUncheck.setImageResource(R.drawable.unchecked);
            selectedTypeId.remove(complainType.getTypeId());
        }
        imgUncheck.setOnClickListener(view -> {
            selectedPosition = (int) view.getTag();
            selectedTypeId.add(complainType.getTypeId());
            notifyDataSetChanged();
        });
        txtComplainType.setText(complainType.getName());
        return convertView;
    }

    public View generateNewView(int layoutId) {
        return getActivity().getLayoutInflater().inflate(layoutId, null);
    }


    public ArrayList<String> getSelectedTypeIds() {
        return selectedTypeId;
    }

    int selectedPosition = -1;

    private boolean isSelected(int position) {
        return position == selectedPosition;
    }
}
