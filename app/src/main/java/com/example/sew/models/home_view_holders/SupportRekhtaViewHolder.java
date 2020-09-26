package com.example.sew.models.home_view_holders;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sew.R;
import com.example.sew.activities.BaseActivity;
import com.example.sew.common.JustifiedTextView;
import com.example.sew.helpers.MyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupportRekhtaViewHolder extends BaseHomeViewHolder {


    @BindView(R.id.imgSupportRekhta)
    ImageView imgSupportRekhta;
    @BindView(R.id.txtSupportRekhtaTitle)
    TextView txtSupportRekhtaTitle;
    @BindView(R.id.txtSupportRekhtaDescription)
    TextView txtSupportRekhtaDescription;
    @BindView(R.id.txtDonateNow)
    TextView txtDonateNow;
    @BindView(R.id.layDonateViaPaytm)
    LinearLayout layDonateViaPaytm;
    @BindView(R.id.txtDonateViaPaytm)
    TextView txtDonateViaPaytm;

    @OnClick(R.id.layDonateNow)
    void onDonateNowClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getActivity().getString(R.string.donate_rekhta_url)));
        getActivity().startActivity(browserIntent);
    }
    @OnClick(R.id.layDonateViaPaytm)
    void onDonateViaPaytmClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getActivity().getString(R.string.donate_rekhta_via_paytm)));
        getActivity().startActivity(browserIntent);
    }

    public static SupportRekhtaViewHolder getInstance(View convertView, BaseActivity baseActivity) {
        SupportRekhtaViewHolder videoViewHolder;
        if (convertView == null) {
            convertView = getInflatedView(R.layout.cell_home_support_rekhta, baseActivity);
            videoViewHolder = new SupportRekhtaViewHolder(convertView, baseActivity);
        } else
            videoViewHolder = (SupportRekhtaViewHolder) convertView.getTag();
        videoViewHolder.setConvertView(convertView);
        convertView.setTag(videoViewHolder);
        return videoViewHolder;
    }

    private SupportRekhtaViewHolder(View convertView, BaseActivity baseActivity) {
        super(baseActivity);
        ButterKnife.bind(this, convertView);
    }


    public BaseHomeViewHolder loadData() {
        txtDonateNow.setText(MyHelper.getString(R.string.donate_now));
        txtDonateViaPaytm.setText(MyHelper.getString(R.string.donate_via_paytm));
        txtSupportRekhtaDescription.setText(MyHelper.getString(R.string.support_rekhta_details));
        txtSupportRekhtaTitle.setText(MyHelper.getString(R.string.support_rekhta));
        justifyText(txtSupportRekhtaDescription);
        return this;
    }
    private void justifyText(TextView textView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            JustifiedTextView.justify(textView);
        }
    }
}
