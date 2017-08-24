package com.example;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grit.gilog.GILog;
import com.grit.mdm.R;
public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1254;
    private String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        signIn();
    }

    private void signIn() {
        if(mGoogleApiClient==null) {
            GoogleSignInOptions gso =
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        handleAuthFailure();
    }

    private void handleAuthFailure() {
        String title = "Authentication Failed";
        String message = "Google Sign-In Failed.";
        String button1Text = "Retry";
        new AlertDialog.Builder(this).setMessage(message).setTitle(title).setCancelable(false)
                                     .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                         @Override
                                         public void onCancel(final DialogInterface dialog) {
                                         }
                                     }).setPositiveButton(button1Text,
                                                          new DialogInterface.OnClickListener() {
                                                              public void onClick(
                                                                      final DialogInterface dialog,
                                                                      final int whichButton) {
                                                                  signIn();
                                                              }
                                                          }).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        GILog.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            GILog.d(TAG, "gso result success - " + acct.getEmail());
            handleAuthSuccess(acct.getEmail());
        } else {
            GILog.d(TAG, "gso result failure");
            handleAuthFailure();
        }
    }

    private void handleAuthSuccess(final String email) {
        GILog.d(TAG, "gso result success - " + email);
    }
}
