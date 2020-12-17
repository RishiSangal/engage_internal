package com.example.sew.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.sew.R;
import com.example.sew.adapters.CustomListAdapter;
import com.example.sew.apis.BaseServiceable;
import com.example.sew.apis.GetAllfavoriteId;
import com.example.sew.apis.PostDeviceRegisterForPush;
import com.example.sew.apis.PostLogin;
import com.example.sew.apis.PostSocialLogin;
import com.example.sew.common.AppErrorMessage;
import com.example.sew.helpers.MyHelper;
import com.example.sew.helpers.MyService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class LoginActivity extends BaseSocialLoginActivity {
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
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.layForgotPassword)
    LinearLayout layForgotPassword;
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
    @BindView(R.id.txtSkipLogin)
    TextView txtSkipLogin;
    @BindView(R.id.sign_up)
    TextView txtSignUp;
    @BindView(R.id.laySignUp)
    LinearLayout laySignUp;
    @BindView(R.id.autoComplete)
    AutoCompleteTextView autoComplete;

    @OnEditorAction(R.id.edPassword)
    void onKeyboardAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyBoard();
            checkValidationAndLogin();
        }
    }

    @OnFocusChange(R.id.autoComplete)
    void onFocusChanged(boolean focused) {
        if (focused) {
            showSuggestions();
        }
    }

    public void showSuggestions() {
        String content = getEditTextData(autoComplete);
        autoComplete.setText(String.format("%s ", content));
        autoComplete.setText(content);
    }

    @OnTextChanged(R.id.edEmail)
    void onEmailChanged() {
        layEmail.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    @OnTextChanged(R.id.edPassword)
    void onPasswordChanged() {
        layPassword.setBackground(getResources().getDrawable(R.drawable.textlayouthint));
    }

    public static Intent getInstance(Activity activity) {
        return new Intent(activity, LoginActivity.class);
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
                    if (socialLogin.isValidResponse()) {
                        getAllFavoriteIds();
//                        startActivity(HomeActivity.getInstance(getActivity()));
//                        finish();
                    } else {
                        dismissDialog();
                        showToast(socialLogin.getErrorMessage());
                    }
                });
    }

    public void getAllFavoriteIds() {
        new GetAllfavoriteId().runAsync((BaseServiceable.OnApiFinishListener<GetAllfavoriteId>) getAllFavoriteIds -> {
            dismissDialog();
            if (getAllFavoriteIds.isValidResponse()) {
                ArrayList<String> favIds = getAllFavoriteIds.getAllFavoriteIds();
                registerDeviceForPush(false);
                MyService.setUserLoggedin(true);
                startActivity(HomeActivity.getInstance(getActivity(), false));
            } else {
                showToast(getAllFavoriteIds.getErrorMessage());
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        edEmail.setOnEditorActionListener((v, actionId, event) -> {
            requestFocus(edPassword);
            return true;
        });
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {
                    if (isOpen && autoComplete.isFocused())
                        showSuggestions();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoComplete.setAdapter(new CustomListAdapter(getActivity(), R.layout.cell_word_suggestion_history, MyService.getSearchKeywordHistory()));
    }

    @OnClick({R.id.btnLogin, R.id.layForgotPassword, R.id.layFacebookLogin, R.id.layGoogleLogin, R.id.txtSkipLogin, R.id.laySignUp, R.id.view_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                hideKeyBoard();
                checkValidationAndLogin();
                break;
            case R.id.layForgotPassword:
                startActivity(ForgotPasswordActivity.getInstance(getActivity()));
                break;
            case R.id.layFacebookLogin:
                doFBLogin();
                break;
            case R.id.layGoogleLogin:
                doGoogleLogin();
                break;
            case R.id.txtSkipLogin:
                registerDeviceForPush(true);
                startActivity(HomeActivity.getInstance(getActivity(), false));
//                startActivity(HomeActivity.getInstance(getActivity()));
//                finish();
                break;
            case R.id.laySignUp:
                startActivity(SignupActivity.getInstance(getActivity()));
                finish();
                break;
            case R.id.view_login:
                hideKeyBoard();
        }
    }

    private void registerDeviceForPush(boolean IsSkipLogin) {
        new PostDeviceRegisterForPush().
                runAsync((BaseServiceable.OnApiFinishListener<PostDeviceRegisterForPush>) postDeviceRegisterForPush -> {
                    if (postDeviceRegisterForPush.isValidResponse()) {
                        if (!IsSkipLogin)
                            syncFavoriteIfNecessary();
//                        startActivity(HomeActivity.getInstance(getActivity(),false));
                        finish();
                    }
//                    else
//                        showToast(postDeviceRegisterForPush.getErrorMessage());
                });
    }

    private void checkValidationAndLogin() {
        String email = getEditTextData(edEmail);
        String password = getEditTextData(edPassword);
        boolean isValidEmail = MyHelper.isValidEmail(email);
        boolean isValidPassword = MyHelper.isValidPassword(password);
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            alertError(layEmail, layPassword);
            showToast(AppErrorMessage.please_enter_email_password);
        } else if (!isValidEmail) {
            if (TextUtils.isEmpty(email)) {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_email_id);
            } else {
                alertError(layEmail);
                showToast(AppErrorMessage.please_enter_valid_email_id);
            }
        } else if (TextUtils.isEmpty(password)) {
            alertError(layPassword);
            showToast(AppErrorMessage.please_enter_password);
        }
//        else if (!isValidPassword) {
//            if (TextUtils.isEmpty(password)) {
//                alertError(layPassword);
//                showToast(AppErrorMessage.please_enter_password);
//            }
//            else {
//                alertError(layPassword);
//                showPasswordErrorDialog(password);
//            }
//        }
        else {
            showDialog();
            new PostLogin()
                    .setEmail(email)
                    .setPassword(password)
                    .runAsync((BaseServiceable.OnApiFinishListener<PostLogin>) postLogin -> {
                        if (postLogin.isValidResponse()) {
                            getAllFavoriteIds();
//                            syncFavoriteIfNecessary();
//                            startActivity(HomeActivity.getInstance(getActivity()));
//                            finish();
                        } else {
                            dismissDialog();
                            showToast(postLogin.getErrorMessage());
                        }
                    });
        }
    }

    private void alertError(View... views) {
        for (View view : views)
            if (view != null)
                view.setBackground(getResources().getDrawable(R.drawable.text_error_layout));
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
}