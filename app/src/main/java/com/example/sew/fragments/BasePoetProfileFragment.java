package com.example.sew.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sew.R;
import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;

import org.json.JSONException;
import org.json.JSONObject;

public class BasePoetProfileFragment extends BaseFragment {

    public final void setPoetHeaderContent(PoetDetail poetDetail) {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_poets, container, false);
    }

    public static BasePoetProfileFragment getInstance(@NonNull PoetDetail poetDetail, BasePoetProfileFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(POET_DETAIL_OBJ, poetDetail.getJsonObject().toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BasePoetProfileFragment getInstance(@NonNull PoetDetail poetDetail, ContentType contentType, BasePoetProfileFragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(POET_DETAIL_OBJ, poetDetail.getJsonObject().toString());
        bundle.putString(CONTENT_TYPE_OBJ, contentType.getJsonObject().toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    private PoetDetail poetDetail;

    public PoetDetail getPoetDetail() {
        if (poetDetail != null)
            return poetDetail;
        try {
            if (getArguments() != null) {
                return poetDetail = new PoetDetail(new JSONObject(getArguments().getString(POET_DETAIL_OBJ, "{}")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poetDetail = new PoetDetail(null);
    }

    private ContentType contentType;

    public ContentType getContentType() {
        if (contentType != null)
            return contentType;
        try {
            if (getArguments() != null) {
                return contentType = new ContentType(new JSONObject(getArguments().getString(CONTENT_TYPE_OBJ, "{}")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentType = new ContentType(null);
    }
}
