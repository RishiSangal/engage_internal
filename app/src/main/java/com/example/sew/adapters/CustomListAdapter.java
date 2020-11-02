package com.example.sew.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.sew.R;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter {

    private List<String> dataList;
    private Context mContext;
    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;
    private int layoutResId;

    public CustomListAdapter(Context context, int resource, List<String> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        this.layoutResId = resource;
        mContext = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutResId, parent, false);
        }
        TextView strName = (TextView) view.findViewById(R.id.textView);
        ImageView imgClose = (ImageView) view.findViewById(R.id.imgClose);
        View view1 = (View) view.findViewById(R.id.view);
        if (dataList.size() > 0) {
            if (dataList.size() == 1)
                view1.setVisibility(View.GONE);
            else
                view1.setVisibility(View.VISIBLE);
        }

        strName.setText(getItem(position));
        view.setTag(getItem(position));
        imgClose.setTag(getItem(position));
        imgClose.setOnClickListener(v -> {
            String storeString = (String) v.getTag();
            warningPopup(storeString);
        });
        return view;
    }

    private void warningPopup(String storeString) {
        new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom))
                .setTitle(MyHelper.getString(R.string.rekhta))
                .setMessage(MyHelper.getString(R.string.do_you_want_to_delete))
                .setPositiveButton(MyHelper.getString(R.string.yes), (dialog, which) -> {
                    dataList.remove(storeString);
                    notifyDataSetChanged();
                    MyService.deleteSearchKeywordHistory(storeString);
                    dialog.dismiss();
                })
                .setNegativeButton(MyHelper.getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();



    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<String>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<String>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}