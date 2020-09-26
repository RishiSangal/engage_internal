package com.example.sew.fragments;

import com.example.sew.models.ContentType;
import com.example.sew.models.PoetDetail;

public class PoetGhazalFragment extends BasePoetGhazalFragment {

    public static BasePoetProfileFragment getInstance(PoetDetail poetDetail, ContentType contentType) {
        return getInstance(poetDetail, contentType, new PoetGhazalFragment());
    }

    @Override
    public String getContentTypeId() {
        return getContentType().getContentId();
    }
}
