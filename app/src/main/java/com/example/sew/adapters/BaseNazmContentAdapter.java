package com.example.sew.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.MyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sew.common.MyConstants.NAZM_50_FAMOUS__ID;
import static com.example.sew.common.MyConstants.NAZM_BEGINNER_ID;
import static com.example.sew.common.MyConstants.NAZM_EDITOR_CHOICE_ID;
import static com.example.sew.common.MyConstants.NAZM_HUMOR_ID;


abstract class BaseNazmContentAdapter extends BaseMyAdapter {

    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT = 1;
    private String targetId;

    BaseNazmContentAdapter(BaseActivity activity, String targetId) {
        super(activity);
        this.targetId = targetId;
    }

    void loadDataForGhazalHeader(GhazalHeaderViewHolder viewHolder) {
        if (viewHolder == null)
            return;
        if (NAZM_50_FAMOUS__ID.equalsIgnoreCase(targetId)) {
            viewHolder.txtTop100.setVisibility(View.VISIBLE);
            viewHolder.txtTop100Description.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtTop100.setVisibility(View.GONE);
            viewHolder.txtTop100Description.setVisibility(View.GONE);
        }
        viewHolder.txtTop100.setText(String.valueOf(50));
        viewHolder.txtTop100Description.setText(MyHelper.getString(R.string.famousdescriotion_50));
        String headerTitle = "";
        if (NAZM_50_FAMOUS__ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.top_50_nazms);
        } else if (NAZM_BEGINNER_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.beginners);
        } else if (NAZM_EDITOR_CHOICE_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.editors_choice);
        } else if (NAZM_HUMOR_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.humoursatire);
        }
        viewHolder.txtTitle.setText(headerTitle);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class GhazalHeaderViewHolder {
        @BindView(R.id.txtTop100)
        TextView txtTop100;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtTop100Description)
        TextView txtTop100Description;

        GhazalHeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

