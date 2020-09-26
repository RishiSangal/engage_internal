package com.example.sew.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.sew.R;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.PostDeviceRegisterForPush;
import com.example.sew.apis.PostSignup;
import com.example.sew.apis.PostSocialLogin;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.helpers.MyHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class SignupActivity extends BaseSocialLoginActivity {

    @BindView(R.id.edName)
    TextInputEditText edName;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.layName)
    LinearLayout layName;
    @BindView(R.id.edEmail)
    TextInputEditText edEmail;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.layEmail)
    LinearLayout layEmail;
    @BindView(R.id.edPassword)
    TextInputEditText edPassword;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.layPassword)
    LinearLayout layPassword;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.txtFacebook)
    TextView txtFacebook;
    @BindView(R.id.layFacebookLogin)
    RelativeLayout layFacebookLogin;
    @BindView(R.id.ripple)
    MaterialRippleLayout ripple;
    @BindView(R.id.txtGoogle)
    TextView txtGoogle;
    @BindView(R.id.layGoogleLogin)
    RelativeLayout layGoogleLogin;
    @BindView(R.id.txtLogIn)
    TextView txtLogIn;
    @BindView(R.id.layLogin)
    LinearLayout layLogin;
    @BindView(R.id.view_login)
    LinearLayout viewLogin;

    @OnTextChanged(R.id.edEmail)
    void onEmailChanged() {
        layEmail.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    @OnTextChanged(R.id.edPassword)
    void onPasswordChanged() {
        layPassword.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    @OnTextChanged(R.id.edName)
    void onNameChanged() {
        layName.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    @OnEditorAction({R.id.edEmail, R.id.edName, R.id.edPassword})
    boolean onEditorAction(KeyEvent keyEvent, int actionId, TextView view) {
        switch (view.getId()) {
            case R.id.edEmail:
                if ((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    requestFocus(edPassword);
                    return true;
                }
                break;
            case R.id.edName:
                if ((keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    requestFocus(edEmail);
                    return true;
                }
                break;
            case R.id.edPassword:
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyBoard();
                    validateAndSignUp();
                }
                break;
        }
        return false;
    }

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, SignupActivity.class);
    }

    @Override
    public void onSocialLoginSuccess(String id, String email, String accessToken, String firstName, String lastName, File profileImage, String socialType) {
        showDialog();
        new PostSocialLogin()
                .setEmail(email)
                .setExternalAccessToken(accessToken)
                .setProvider(socialType)
                .setUserName(String.format(Locale.getDefault(), "%s %s", firstName, lastName))
                .runAsync((BaseServiceable.OnApiFinishListener<PostSocialLogin>) socialLogin -> {
                    dismissDialog();
                    if (socialLogin.isValidResponse()) {
                        startActivity(HomeActivity.getInstance(getActivity(),false));
                        finish();
                    } else
                        showToast(socialLogin.getErrorMessage());
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.layFacebookLogin, R.id.layGoogleLogin, R.id.layLogin, R.id.btnSignUp, R.id.view_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layFacebookLogin:
                doFBLogin();
                break;
            case R.id.layGoogleLogin:
                doGoogleLogin();
                break;
            case R.id.layLogin:
                startActivity(LoginActivity.getInstance(getActivity()));
                finish();
                break;
            case R.id.btnSignUp:
                validateAndSignUp();
                break;
            case R.id.view_login:
                hideKeyBoard();
        }
    }

    private void validateAndSignUp() {
        String name = getEditTextData(edName);
        String email = getEditTextData(edEmail);
        String password = getEditTextData(edPassword);
        boolean isValidName = MyHelper.isValidName(name);
        boolean isValidEmail = MyHelper.isValidEmail(email);
        boolean isValidPassword = MyHelper.isValidPassword(password);
        if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            alertError(layName, layEmail, layPassword);
            showToast(AppErrorMessage.please_enter_all_field_signup);
        }
        else if (TextUtils.isEmpty(name)&&TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if (!isValidPassword) {
                if (TextUtils.isEmpty(password)) {
                    alertError(layPassword);
                    showToast(AppErrorMessage.please_enter_password);
                }
                else {
                    alertError(layPassword);
                    showPasswordErrorDialog(password);
                }
            }else {
                alertError(layName, layEmail);
                showToast(AppErrorMessage.please_enter_name_and_mailid);
            }
        }else if(TextUtils.isEmpty(name)&&TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)){
            if (!isValidEmail) {
                if (TextUtils.isEmpty(email)) {
                    alertError(layEmail);
                    showToast(AppErrorMessage.please_enter_email_id);
                } else {
                    alertError(layEmail);
                    showToast(AppErrorMessage.please_enter_valid_email_id);
                }
            } else{
                alertError(layName, layPassword);
                showToast(AppErrorMessage.please_enter_name_and_password);
            }
        }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)){
            if (!isValidName) {
                if(TextUtils.isEmpty(name)){
                    alertError(layName);
                    showToast(AppErrorMessage.please_enter_your_name_signup);
                }else {
                    alertError(layName);
                    showToast(AppErrorMessage.please_enter_valid_name_signup);
                }
            } else{
                alertError(layEmail, layPassword);
                showToast(AppErrorMessage.please_enter_email_password);
            }
        }else if(TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            if (!isValidEmail) {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_valid_email_id);

            }else if (!isValidPassword) {
                alertError(layPassword);
                showPasswordErrorDialog(password);
            }else{
                alertError(layName);
                showToast(AppErrorMessage.please_enter_your_name_signup);
            }
        }else if(TextUtils.isEmpty(email)&&!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)){
            if (!isValidName) {
                alertError(layName);
                showToast(AppErrorMessage.please_enter_valid_name_signup);

            }else if (!isValidPassword) {
                alertError(layPassword);
                showPasswordErrorDialog(password);
            }else{
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_email_id);
            }
        }else if(TextUtils.isEmpty(password)&&!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)){
            if (!isValidName) {
                alertError(layName);
                showToast(AppErrorMessage.please_enter_valid_name_signup);

            }else if (!isValidEmail) {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_valid_email_id);
            }else{
                alertError(layPassword);
                showToast(AppErrorMessage.please_enter_password);
            }
        }
        else if (!isValidName) {
            if(TextUtils.isEmpty(name)){
                alertError(layName);
                showToast(AppErrorMessage.please_enter_your_name_signup);
            }else {
                alertError(layName);
                showToast(AppErrorMessage.please_enter_valid_name_signup);
            }
        }
        else if (!isValidEmail) {
            if (TextUtils.isEmpty(email)) {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_email_id);
            } else {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_valid_email_id);
            }
        }
        else if (!isValidPassword) {
            if (TextUtils.isEmpty(password)) {
                alertError(layPassword);
                showToast(AppErrorMessage.please_enter_password);
            }
            else {
                alertError(layPassword);
                showPasswordErrorDialog(password);
            }
        }
        else {
            showDialog();
            new PostSignup()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(name)
                    .runAsync((BaseServiceable.OnApiFinishListener<PostSignup>) postSignUp -> {
                        dismissDialog();
                        if (postSignUp.isValidResponse()) {
                            registerDeviceForPush();
                        } else
                            showToast(postSignUp.getErrorMessage());
                    });
        }
    }
    private void registerDeviceForPush() {
        new PostDeviceRegisterForPush().
                runAsync((BaseServiceable.OnApiFinishListener<PostDeviceRegisterForPush>) postDeviceRegisterForPush -> {
                    if (postDeviceRegisterForPush.isValidResponse()) {
                        startActivity(HomeActivity.getInstance(getActivity(),false));
                        finish();
                    }
                });
    }
    public void showPasswordErrorDialog(String password) {
        boolean hasPassLen = false;
        boolean hasNumber = false;
        boolean hasUppercase = false;
        if (password.length() > 5)
            hasPassLen = true;
        if (MyHelper.passwordPatternNumber.matcher(password).find())
            hasNumber = true;
        if (MyHelper.passwordPatternUpperCase.matcher(password).find())
            hasUppercase = true;
        // custom dialog
        final Dialog dialog = new Dialog(new ContextThemeWrapper(this, R.style.DialogAnimation));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password_validation_alert);
        ImageView pass_lengthIcon = dialog.findViewById(R.id.pass_lengthIcon);
        ImageView upperCaseImage = dialog.findViewById(R.id.uppercaseIcon);
        ImageView numberImage = dialog.findViewById(R.id.numberIcon);
        TextView closeText = dialog.findViewById(R.id.textView2);
        if (hasPassLen)
            pass_lengthIcon.setImageResource(R.drawable.tick);
        if (hasUppercase)
            upperCaseImage.setImageResource(R.drawable.tick);
        if (hasNumber)
            numberImage.setImageResource(R.drawable.tick);
        dialog.setCanceledOnTouchOutside(true);
        closeText.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void alertError(View... views) {
        for (View view : views)
            if (view != null)
                view.setBackground(getResources().getDrawable(R.drawable.text_error_layout));
    }
}
