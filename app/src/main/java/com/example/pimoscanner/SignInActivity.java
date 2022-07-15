package com.example.pimoscanner;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esri.arcgisruntime.io.JsonEmbeddedException;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalInfo;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.OAuthConfiguration;
import com.example.pimoscanner.account.AccountManager;
import com.example.pimoscanner.dialogs.ProgressDialogFragment;
import com.example.pimoscanner.util.StringUtils;

import org.apache.http.client.HttpResponseException;

import java.net.MalformedURLException;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    public static final String TAG = SignInActivity.class.getSimpleName();

    private static final String MSG_OBTAIN_CLIENT_ID = "You have to provide a client id in order to do OAuth sign in. You can obtain a client id by registering the application on https://developers.arcgis.com.";

    private static final String HTTPS = "https://";

    private static final String HTTP = "http://";
    private static final String TAG_PROGRESS_DIALOG = "TAG_PROGRESS_DIALOG";
    private EditText mPortalUrlEditText;
    private View mContinueButton;
    private String mPortalUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_in );

        mPortalUrlEditText = findViewById(R.id.sign_in_activity_portal_url_edittext);
        mPortalUrlEditText.addTextChangedListener(this);

        mContinueButton = findViewById(R.id.sign_in_activity_continue_button);
        mContinueButton.setOnClickListener(this);
        mContinueButton.setEnabled(!mPortalUrlEditText.getText().toString().trim().isEmpty());

        View cancelButton = findViewById(R.id.sign_in_activity_cancel_button);
        cancelButton.setOnClickListener(this);
        mPortalUrl = mPortalUrlEditText.getText().toString().trim();

        // Set up an authentication handler
        // to be used when loading remote
        // resources or services.
        try {
            OAuthConfiguration oAuthConfiguration = new OAuthConfiguration("https://www.arcgis.com",getString( R.string.client_id), getString(
                    R.string.redirect_uri));
            DefaultAuthenticationChallengeHandler authenticationChallengeHandler = new DefaultAuthenticationChallengeHandler(
                    this);
            AuthenticationManager.setAuthenticationChallengeHandler(authenticationChallengeHandler);
            AuthenticationManager.addOAuthConfiguration(oAuthConfiguration);
        } catch (MalformedURLException e) {
            Log.i(TAG,"OAuth problem : " + e.getMessage());
            Toast.makeText(this, "The was a problem authenticating against the portal.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        final Activity activity = this;
        switch (view.getId()) {
            case R.id.sign_in_activity_continue_button :
                // determine what type of authentication is required to sign in
                // to the specified portal
                mPortalUrl = mPortalUrlEditText.getText().toString().trim();
                if (mPortalUrl.startsWith(HTTP)) {
                    mPortalUrl = mPortalUrl.replace(HTTP, HTTPS);
                }else{
                    mPortalUrl = HTTPS + mPortalUrl;
                }
                final Portal portal = new Portal(mPortalUrl);
                portal.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        if (portal.getLoadStatus() == LoadStatus.LOADED) {
                            PortalInfo portalInformation = portal.getPortalInfo();
                            if (portalInformation.isSupportsOAuth()) {
                                signInWithOAuth();
                            }
                        }else{
                            String errorMessage = portal.getLoadError().getMessage();
                            String message = "Error accessing " + mPortalUrl + ". " + errorMessage +".";
                            Integer errorCode = getErrorCode(portal.getLoadError());
                            if (errorCode != null){
                                message = message + " Error code " + errorCode.toString();
                            }
                            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                            Log.e(TAG, message);
                        }
                    }
                });
                portal.loadAsync();
                break;
            case R.id.sign_in_activity_cancel_button :
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }

        // update the enabled state of the Continue button
        String url = s.toString().trim();
        mContinueButton.setEnabled(StringUtils.isNotEmpty(url));
    }

    /**
     * Signs into the portal using OAuth2.
     */
    private void signInWithOAuth() {

        if (mPortalUrl.startsWith(HTTP)) {
            mPortalUrl = mPortalUrl.replace(HTTP, HTTPS);
        }

        // Are we already signed in?
        if (AccountManager.getInstance().getPortal() != null) {
            Log.i(TAG, "Already signed into to Portal.");
            return;
        }
        final ProgressDialogFragment mProgressDialog;
        String clientId = getString(R.string.client_id);

        if (StringUtils.isEmpty(clientId)) {
            Toast.makeText(this, MSG_OBTAIN_CLIENT_ID, Toast.LENGTH_SHORT).show();
            return;
        }
        // default handler
        final Activity activity = this;
        final Portal portal = new Portal(mPortalUrl, true);
        mProgressDialog = ProgressDialogFragment.newInstance(getString(R.string.verifying_portal));
        mProgressDialog.show(getFragmentManager(), TAG_PROGRESS_DIALOG);
        portal.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (portal.getLoadStatus() == LoadStatus.LOADED) {
                    mProgressDialog.dismiss();
                    AccountManager.getInstance().setPortal(portal);
                    finish();
                }else {
                    mProgressDialog.dismiss();
                    Throwable t = portal.getLoadError();
                    Integer errorCode = getErrorCode(t);
                    String errorMessage = portal.getLoadError().getMessage();
                    String message = "Portal error: " + errorMessage;
                    System.out.println ("ERROR" + errorMessage);
                    if (errorCode!=null){
                        // Append error message with relevant error code.
                        errorMessage = message + " Error code " + errorCode.toString();
                        //403 thrown when user hits Cancel in Auth Popup window so we don't display a toast.
                        if (errorCode !=403){
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                            System.out.println ("ERROR2" + errorMessage);
                        }
                    }else{ // No error code
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                    }
                    Log.e(TAG, "Error signing in to portal: " + errorMessage );
                    System.out.println ("ERROR3" + errorMessage);
                    finish();
                }
            }
        });
        portal.loadAsync();

    }
    /**
     * Helper method that returns an error code from an http response
     * @param t Throwable
     * @return Integer representing error code
     */
    private Integer getErrorCode(Throwable t){
        Integer error = null;
        if (t instanceof JsonEmbeddedException){
            error = ((JsonEmbeddedException)t).getCode();
            System.out.println ("ERROR4" + error);
        } else if (t instanceof HttpResponseException){
            error= ((HttpResponseException) t).getStatusCode();
            System.out.println ("ERROR5" + error);
        }
        return error;
    }
}