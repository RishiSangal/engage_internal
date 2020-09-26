package com.example.sew.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.PostSubmitCritique;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.common.Enums;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.example.sew.views.TitleTextViewType2;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;


public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.name)
    TextInputLayout tilName;
    @BindView(R.id.email)
    TextInputLayout tilEmail;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.edEmail)
    EditText edEmail;
    @BindView(R.id.spnFeedbackType)
    AppCompatSpinner spnFeedbackType;
    @BindView(R.id.edFeedBack)
    EditText edFeedBack;
    @BindView(R.id.txtFeedbackCounter)
    TitleTextViewType2 txtFeedbackCounter;
    @BindView(R.id.txtSendFeedbackTitle)
    TextView txtSendFeedbackTitle;
    @BindView(R.id.txtFeedback)
    TextView txtFeedback;
    @BindView(R.id.viewEmail)
    View viewEmail;
    private LinkedList<Object> feedbackOptions;
    private HintAdapter<Object> hintAdapter;

    @OnTextChanged(R.id.edFeedBack)
    void onSearchTextChanged() {
        txtFeedbackCounter.setText(String.format(Locale.getDefault(), "%d/360", edFeedBack.getText().toString().length()));
    }

    @OnClick(R.id.laySendFeedback)
    void onFeedbackClick() {

    }

    public static Intent getInstance(BaseActivity activity) {
        return new Intent(activity, FeedbackActivity.class);
    }

    @Override
    public void onLanguageChanged() {
        super.onLanguageChanged();
        updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        updateUI();
    }

    String mSuggestion, mFeedback, mReportBug;
    private String selectedFeedbackType;

    public void creteFeedbackSubjects() {
        mSuggestion = MyHelper.getString(R.string.share_your_ideas);
        mFeedback = MyHelper.getString(R.string.feedback);
        mReportBug = MyHelper.getString(R.string.reportbug);
        feedbackOptions = new LinkedList<>(Arrays.asList(mFeedback, mReportBug));
        HintSpinner<Object> hintSpinner1 = new HintSpinner<>(spnFeedbackType,
                hintAdapter = new HintAdapter<>(this,
                        MyHelper.getString(R.string.select_option),
                        feedbackOptions), (position, itemAtPosition) -> {
            selectedFeedbackType = itemAtPosition.toString();
        });
        hintSpinner1.init();
    }

    private void updateUI() {
        setHeaderTitle(MyHelper.getString(R.string.feedback));
        creteFeedbackSubjects();
        txtSendFeedbackTitle.setText(MyHelper.getString(R.string.send));
        txtFeedback.setText(MyHelper.getString(R.string.feedback));
        txtFeedbackCounter.setText(String.format(Locale.getDefault(), "%d/360", edFeedBack.getText().toString().length()));
        edFeedBack.setHint(MyHelper.getString(R.string.write_feedback_here));
       // edEmail.setHint(MyHelper.getString(R.string.email));
       // edName.setHint(MyHelper.getString(R.string.name));
        tilEmail.setHint(MyHelper.getString(R.string.email));
        tilName.setHint(MyHelper.getString(R.string.name));
//        edFeedBack.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        edFeedBack.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        edEmail.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        edName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//
//        tilName.setHintAnimationEnabled(false);
//        tilName.setGravity(getLanguageSpecificGravity());
//        edName.setGravity(getLanguageSpecificGravity());
//        tilEmail.setGravity(getLanguageSpecificGravity());
//        edEmail.setGravity(getLanguageSpecificGravity());
//        edFeedBack.setGravity(getLanguageSpecificGravity());

//        tilName.setTextDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
//        tilName.setLayoutDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.LAYOUT_DIRECTION_RTL: View.LAYOUT_DIRECTION_LTR);
//
//        tilEmail.setHintAnimationEnabled(false);
//        tilEmail.setTextDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.TEXT_DIRECTION_RTL : View.TEXT_DIRECTION_LTR);
//        tilEmail.setLayoutDirection(MyService.getSelectedLanguage() == Enums.LANGUAGE.URDU ? View.LAYOUT_DIRECTION_RTL: View.LAYOUT_DIRECTION_LTR);

        if (MyService.isUserLogin()) {
            edEmail.setVisibility(View.GONE);
            viewEmail.setVisibility(View.GONE);
            edName.setText(MyService.getUser().getDisplayName());
            edName.setEnabled(false);
        } else {
            edEmail.setVisibility(View.VISIBLE);
            edName.setEnabled(true);
        }
    }


    @OnClick(R.id.laySendFeedback)
    public void onSendFeedbackClicked() {
        String email, name, message;
        message = getEditTextData(edFeedBack);
        if (MyService.isUserLogin()) {
            email = MyService.getEmail();
            name = MyService.getUser().getDisplayName();
        } else {
            email = getEditTextData(edEmail);
            name = getEditTextData(edName);
            if (TextUtils.isEmpty(name)) {
                showToast(AppErrorMessage.please_enter_your_name);
                return;
            } else if (!MyHelper.isValidEmail(email)) {
                if (TextUtils.isEmpty(email))
                    showToast(AppErrorMessage.please_enter_your_email);
                else
                    showToast(AppErrorMessage.please_enter_valid_email_address);
                return;
            }
        }
        if (TextUtils.isEmpty(selectedFeedbackType))
            showToast(AppErrorMessage.please_select_a_feedback_option);
        else
            if (TextUtils.isEmpty(message))
            showToast(AppErrorMessage.Please_enter_your_feedback);
        else {
            showDialog();
            new PostSubmitCritique().setTypeOfQuery(Enums.CRITIQUE_TYPE.FEEDBACK)
                    .setMessage(message)
                    .setName(name)
                    .setEmail(email)
                    .setSubject(selectedFeedbackType)
                    .runAsync((BaseServiceable.OnApiFinishListener<PostSubmitCritique>) submitCritique -> {
                        dismissDialog();
                        if (submitCritique.isValidResponse()) {
                            finish();
                            showToast(MyHelper.getString(R.string.thankyou_for_your_feedback));
                            //ShowThankYouForCritique();
                        } else
                            showToast(submitCritique.getErrorMessage());
                    });
        }
    }

    public void ShowThankYouForCritique() {
        final Dialog critiqueThankYouDialog = new Dialog(this, R.style.Theme_Dialog);
        critiqueThankYouDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        critiqueThankYouDialog.setContentView(R.layout.critique_thanks_dialog_template);
        critiqueThankYouDialog.setCanceledOnTouchOutside(false);
        TextView thankYouText = critiqueThankYouDialog.findViewById(R.id.thanksCritiqueText);
        thankYouText.setOnClickListener(view -> critiqueThankYouDialog.dismiss());
        critiqueThankYouDialog.show();
        critiqueThankYouDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                critiqueThankYouDialog.dismiss();
                t.cancel();
            }
        }, 2000);
    }

}
