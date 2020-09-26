package com.example.sew.models.home_view_holders;

import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;


import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.helpers.MyHelper;
import com.example.sew.models.DidYouKnow;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class DidYouKowViewHolder extends BaseHomeViewHolder {

    @BindView(R.id.txtDidYouKnowHeader)
    TextView txtDidYouKnowHeader;
    @BindView(R.id.txtHTMLContent)
    TextView txtHTMLContent;

    public DidYouKowViewHolder(BaseActivity baseActivity) {
        super(baseActivity);
    }

    private DidYouKowViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }

    private boolean isLoaded;
    private DidYouKnow didYouKnow;

    public DidYouKowViewHolder loadData(final DidYouKnow didYouKnow) {
        if (!isLoaded) {
            this.didYouKnow = didYouKnow;
            isLoaded = true;
            updateUI();
        }
        return this;
    }

    private void updateUI() {
        txtDidYouKnowHeader.setText(MyHelper.getString(R.string.did_you_know_header));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtHTMLContent.setText(Html.fromHtml(didYouKnow.getHtmlContentRender(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtHTMLContent.setText(Html.fromHtml(didYouKnow.getHtmlContentRender()));
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            txtHTMLContent.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
//        }
        txtHTMLContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static DidYouKowViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        DidYouKowViewHolder didYouKowViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_did_you_know, baseActivity);
            didYouKowViewHolder = new DidYouKowViewHolder(convertView, baseActivity);
        } else
            didYouKowViewHolder = (DidYouKowViewHolder) convertView.getTag();
        didYouKowViewHolder.setConvertView(convertView);
        convertView.setTag(didYouKowViewHolder);
        return didYouKowViewHolder;
    }
}
