package com.example.sew.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.sew.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


public abstract class BaseSocialLoginActivity extends BaseActivity {
    private static final String SOCIAL_TYPE_FACEBOOK = "Facebook";
    private static final String SOCIAL_TYPE_GOOGLE = "Google";
    private final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    public abstract void onSocialLoginSuccess(String id, String email, String accessToken, String firstName, String lastName, File profileImage, String socialType);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private CallbackManager callbackManager;
    private boolean isFbInitialized;
    private boolean isGoogleInitialized;

    private void initGoogleSignIn() {
        if (isGoogleInitialized)
            return;
        isGoogleInitialized = true;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_signin_id_token))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//               .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]


    }
    public void noEmailFound() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Rekhta")
                .setMessage("We are unable to fetch your email id, please use Google plus or create an account with email id")
                .setPositiveButton("Okay", null)
                .create().show();
        return;
    }
    private void initFB() {
        if (isFbInitialized)
            return;
        isFbInitialized = true;
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                //prepare fields with email
                String[] requiredFields = new String[]{"email", "first_name", "last_name", "picture.type(large)"};
                Bundle parameters = new Bundle();
                parameters.putString("fields", TextUtils.join(",", requiredFields));

                GraphRequest request = new GraphRequest(loginResult.getAccessToken(), "me", parameters, null, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            GraphRequest.GraphJSONObjectCallback callbackEmail = new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject me, GraphResponse response) {
                                    if (me != null){
                                        id = me.optString("id");
                                        email = me.optString("email");
                                        accessToken = loginResult.getAccessToken().getToken();
                                        firstName = me.optString("first_name");
                                        lastName = me.optString("last_name");

                                        String photoUrl = null;
                                        if (me.has("picture") && me.optJSONObject("picture").has("data")) {
                                            photoUrl = me.optJSONObject("picture").optJSONObject("data").optString("url");
                                        }

                                        if(TextUtils.isEmpty(email)){
                                            dismissDialog();
                                            noEmailFound();
                                        }else if (!TextUtils.isEmpty(photoUrl)) {
                                            //showDialog();
                                            saveFile(photoUrl, new DownloadStatusListenerV1() {
                                                @Override
                                                public void onDownloadComplete(DownloadRequest downloadRequest) {
                                                    dismissDialog();
                                                    onSocialLoginSuccess(id, email, accessToken, firstName, lastName, new File(downloadRequest.getDestinationURI().getPath()), SOCIAL_TYPE_FACEBOOK);
                                                }

                                                @Override
                                                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                                    dismissDialog();
                                                    onSocialLoginSuccess(id, email, accessToken, firstName, lastName, null, SOCIAL_TYPE_FACEBOOK);
                                                }

                                                @Override
                                                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {

                                                }
                                            });
                                        } else
                                            onSocialLoginSuccess(id, email, accessToken, firstName, lastName, null, SOCIAL_TYPE_FACEBOOK);

                                        LoginManager.getInstance().logOut();
                                    }
                                }
                            };

                            callbackEmail.onCompleted(response.getJSONObject(), response);
                        }

                    }
                });

                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void doFBLogin() {
        initFB();
        LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
    }

    public void doGoogleLogin() {
        initGoogleSignIn();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager == null || !callbackManager.onActivityResult(requestCode, resultCode, data)) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    private String id;
    private String email;
    private String accessToken;
    private String firstName;
    private String lastName;

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {

        Log.i("", "GooglehandleSignInResult:" + result.isSuccess() + " " + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            id = acct.getId();
            email = acct.getEmail();
            accessToken = acct.getIdToken();
            firstName = acct.getDisplayName();
            lastName = acct.getDisplayName();
            try {
                firstName = acct.getDisplayName().split(" ")[0];
                lastName = acct.getDisplayName().split(" ")[1];
            } catch (Exception e) {
                Log.e("", e.getMessage());
            }

            try {
                // finally logout because onStart method always tries to login otherwise
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (acct.getPhotoUrl() != null && !TextUtils.isEmpty(acct.getPhotoUrl().toString())) {
                        //showDialog();
                        saveFile(acct.getPhotoUrl().toString(), new DownloadStatusListenerV1() {
                            @Override
                            public void onDownloadComplete(DownloadRequest downloadRequest) {
                                dismissDialog();
                                onSocialLoginSuccess(id, email, accessToken, firstName, lastName, new File(downloadRequest.getDestinationURI().getPath()), SOCIAL_TYPE_GOOGLE);
                            }

                            @Override
                            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                                dismissDialog();
                                onSocialLoginSuccess(id, email, accessToken, firstName, lastName, null, SOCIAL_TYPE_GOOGLE);
                            }

                            @Override
                            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {

                            }
                        });
                    } else
                        onSocialLoginSuccess(id, email, accessToken, firstName, lastName, null, SOCIAL_TYPE_GOOGLE);
                }
            });


        } else {
            showToast("Unable to fetch data, Proceed manually");
            //showToast("Error occurred, please try later");
            Log.i("", "Google login doesnt work. Try running sample Test actvity class ");
        }
    }

    private void saveFile(String url, DownloadStatusListenerV1 downloadStatusListener) {
        final String destPath = String.format("%s/%s", getExternalCacheDir() != null ? getExternalCacheDir().toString() : getCacheDir().toString(), URLUtil.guessFileName(url, null, null));
        File file = new File(destPath);
        if (file.exists())
            file.delete();
        Uri destinationUri = Uri.fromFile(new File(destPath));
        Uri downloadUri = Uri.parse(url);
        ThinDownloadManager thinDownloadManager = new ThinDownloadManager();
        try {
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setPriority(DownloadRequest.Priority.HIGH)
                    .setDestinationURI(destinationUri)
                    .setDownloadContext(getActivity())//Optional
                    .setStatusListener(downloadStatusListener);
            thinDownloadManager.add(downloadRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
}
