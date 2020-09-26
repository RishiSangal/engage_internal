package com.example.sew.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.PostForgotPassword;
import com.example.sew.helpers.MyHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class ForgotPasswordActivity extends BaseActivity {


    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.edEmail)
    TextInputEditText edEmail;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.layEmail)
    LinearLayout layEmail;
    @BindView(R.id.txtErrorMessage)
    TextView txtErrorMessage;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.activity_forget_password)
    LinearLayout activityForgetPassword;

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, ForgotPasswordActivity.class);
    }

    @OnTextChanged(R.id.edEmail)
    void onEmailChanged() {
        layEmail.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    @OnEditorAction(R.id.edEmail)
    boolean onEditorActionEmail(KeyEvent keyEvent, int actionId) {
        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            automaticHideErrorMsg();
            hideKeyBoard();
            if (btnSubmit.getText().toString().contentEquals(getString(R.string.fortgot_password_login_again))) {
                startActivity(LoginActivity.getInstance(getActivity()));
                finish();
            } else {
                checkAndSendPasswordToMail();
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imgBack, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (btnSubmit.getText().toString().contentEquals(getString(R.string.fortgot_password_login_again))) {
                    startActivity(LoginActivity.getInstance(getActivity()));
                    finish();
                } else
                    checkAndSendPasswordToMail();
                break;
        }
    }

    private void checkAndSendPasswordToMail() {
        hideKeyBoard();
        String email = getEditTextData(edEmail);
        if (TextUtils.isEmpty(email) || !MyHelper.isValidEmail(email))
            alertError(layEmail);
        else {
            showDialog();
            new PostForgotPassword()
                    .setEmail(email)
                    .runAsync((BaseServiceable.OnApiFinishListener<PostForgotPassword>) forgotPassword -> {
                        dismissDialog();
                        if (forgotPassword.isValidResponse()) {
                            txtErrorMessage.setVisibility(View.VISIBLE);
                            edEmail.setVisibility(View.GONE);
                            txtErrorMessage.setTextColor(getPrimaryTextColor());
                            txtErrorMessage.setText(R.string.success_email_sent);
                            layEmail.setVisibility(View.GONE);
                            btnSubmit.setText(getString(R.string.fortgot_password_login_again));
                        } else {
                            alertError(layEmail);
                            showToast(forgotPassword.getErrorMessage());
                        }
                    });
        }
    }

    private void alertError(View... views) {
        for (View view : views)
            if (view != null)
                view.setBackground(getResources().getDrawable(R.drawable.text_error_layout));
    }

    public void automaticHideErrorMsg() {
        new Handler().postDelayed(this::hideErrorMsgs, 3000);
    }

    public void hideErrorMsgs() {
        txtErrorMessage.setVisibility(View.GONE);
    }
}
