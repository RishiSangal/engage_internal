package com.example.sew.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.MyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sew.common.MyConstants.GHAZAL_100_FAMOUS__ID;
import static com.example.sew.common.MyConstants.GHAZAL_BEGINNER_ID;
import static com.example.sew.common.MyConstants.GHAZAL_EDITOR_CHOICE_ID;
import static com.example.sew.common.MyConstants.GHAZAL_HUMOR_ID;


abstract class BaseGhazalContentAdapter extends BaseMyAdapter {

    final int VIEW_TYPE_HEADER = 0;
    final int VIEW_TYPE_CONTENT = 1;
    private String targetId;

    BaseGhazalContentAdapter(BaseActivity activity, String targetId) {
        super(activity);
        this.targetId = targetId;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    void loadDataForGhazalHeader(GhazalHeaderViewHolder viewHolder) {
        if (viewHolder == null)
            return;
        if (targetId.contentEquals(GHAZAL_100_FAMOUS__ID)) {
            viewHolder.txtTop100.setVisibility(View.VISIBLE);
            viewHolder.txtTop100Description.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtTop100.setVisibility(View.GONE);
            viewHolder.txtTop100Description.setVisibility(View.GONE);
        }
        viewHolder.txtTop100.setText(String.valueOf(100));
        viewHolder.txtTop100Description.setText(MyHelper.getString(R.string.a_selection_of_100_most));
        String headerTitle = "";
        if (GHAZAL_100_FAMOUS__ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.most_famous_ghazals_100);
        } else if (GHAZAL_BEGINNER_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.beginners);
        } else if (GHAZAL_EDITOR_CHOICE_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.editors_choice);
        } else if (GHAZAL_HUMOR_ID.equalsIgnoreCase(targetId)) {
            headerTitle = MyHelper.getString(R.string.humoursatire);
        }
        viewHolder.txtTitle.setText(headerTitle);
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

