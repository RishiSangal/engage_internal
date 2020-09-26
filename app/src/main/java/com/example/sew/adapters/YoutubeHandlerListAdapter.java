package com.example.sew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sew.R;
import com.example.sew.activities.YoutubeHandler;
import com.example.sew.helpers.MyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YoutubeHandlerListAdapter extends RecyclerView.Adapter<YoutubeHandlerListAdapter.YoutubeHandlerListHolder> {

    private Context mContext;
    private JSONArray arrayList;
    private View view;
    private JSONObject obj;
    private int selectedPosition = -1;
    private Boolean textColor = false;

    public YoutubeHandlerListAdapter(Context c, JSONArray list, int selectedPosition) {
        mContext = c;
        arrayList = list;
        this.selectedPosition = selectedPosition;

    }

    @Override
    public YoutubeHandlerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.video_list_template, parent, false);
        return new YoutubeHandlerListHolder(view);
    }

    @Override
    public void onBindViewHolder(final YoutubeHandlerListHolder holder, final int position) {
        try {
            if (position == arrayList.length()) {
                holder.divi.setVisibility(View.GONE);
            }
            obj = arrayList.getJSONObject(position);

            holder.mPerformedBy.setText(MyHelper.getString(R.string.performed_by));

            holder.videoAuthorName.setText(obj.getString("AN").toUpperCase());
            Glide.with(mContext)
                    .load("https://rekhta.org" + obj.getString("IU"))
                    .into(holder.authorIamge);

            if ((selectedPosition == position))
                holder.videoAuthorName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            else {
                holder.videoAuthorName.setTextColor(mContext.getResources().getColor(R.color.black));
            }

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        YoutubeHandler.playVideo(arrayList.getJSONObject(position).getString("YI"), position);
                        holder.videoAuthorName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                        selectedPosition = position;
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.length();
    }

    public class YoutubeHandlerListHolder extends RecyclerView.ViewHolder {
        private final View divi;
        private View videoView;
        private TextView videoAuthorName;
        private ImageView authorIamge;
        private final TextView mPerformedBy;

        public YoutubeHandlerListHolder(View itemView) {
            super(itemView);
            videoView = itemView;
            mPerformedBy = (TextView) itemView.findViewById(R.id.performtxt);
            videoAuthorName = (TextView) itemView.findViewById(R.id.videAuthor);
            authorIamge = (ImageView) itemView.findViewById(R.id.ImageOfAuthor);
            divi = itemView.findViewById(R.id.divi);
        }
    }

}
