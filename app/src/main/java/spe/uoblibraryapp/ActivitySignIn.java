package spe.uoblibraryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.InputStream;

import stanford.androidlib.SimpleActivity;


public class ActivitySignIn extends SimpleActivity {

    private String TAG = "SignIn";
    private ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pBar = findProgressBar(R.id.loginProgressBar);

        getSupportActionBar().setTitle("Single Sign On");

        WebView mywebview = findViewById(R.id.loginWebView);

        // Clear all user cache previously store, this will force the user to login again.
        mywebview.clearCache(true);
        mywebview.clearHistory();
        clearCookies();

        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setLoadWithOverviewMode(true);
        mywebview.getSettings().setUseWideViewPort(true);
        mywebview.getSettings().setSupportZoom(true);

        mywebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        mywebview.setInitialScale(1);
        mywebview.loadUrl(Constants.UserAuth.oAuthUrl());
        mywebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String URL = request.getUrl().toString();
                if (URL == null || URL.startsWith("http://") || URL.startsWith("https://"))
                    return false;
                else {
                    // TODO need to change this... never actually checks if the url received is the url expected.
                    if (!isAuthorisationDenied(URL)) {
                        processAuthorisationString(URL);
                        // Successful
                        Toast.makeText(getApplicationContext(), "Sign In Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ActivityHome.class));
                        finish()
                        return true;
                    } else {

                        // TODO redirect user to start of activity rather than finish it.
                        // User Denied Request
                        Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
//                        finish();
                        //I guess try again... // TODO Are we sure? if it fails repeatedly then the view constantly opens/closes forever. :/
                        return true;
                    }
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, url);
                if (url.equals("https://authn.sd02.worldcat.org/wayf/metaauth-ui/cmnd/protocol/samlpost"))
                    // Inject CSS when page is done loading
                    injectCSS(view);
                pBar.setVisibility(View.GONE);

                super.onPageFinished(view, url);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pBar.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        // Dont do anything when back button is pressed?

        // TODO could we change back button to work with webview?
    }

    private boolean isAuthorisationDenied(String s) {
        return s.contains(Constants.UserAuth.authFailureUrl);
    }

    private void processAuthorisationString(String s) {

        // TODO: Could this be a loop? store is a hash table, that way its future proof if they change the order of url arguments
        int startOfToken = s.indexOf("access_token=") + 13;
        int endOfToken = s.indexOf("&principalID=");
        String authorisationToken = s.substring(startOfToken, endOfToken);

        int startOfPrincipalID = endOfToken + 13;
        int endOfPrincipalID = s.indexOf("&principalIDNS");
        String principalID = s.substring(startOfPrincipalID, endOfPrincipalID);

        int startOfExpires_at = s.indexOf("&expires_at=") + 12;
        int endOfExpires_at = s.indexOf("Z&refresh_token=");
        String authorisationTokenExpiry = s.substring(startOfExpires_at, endOfExpires_at);

        int startOfRefreshToken = endOfExpires_at + 16;
        int endOfRefreshToken = s.indexOf("&refresh_token_expires_in=");
        String refreshToken = s.substring(startOfRefreshToken, endOfRefreshToken);

        int startOfRefreshTokenExpires_at = s.indexOf("&refresh_token_expires_at=") + 26;
        int endOfRefreshTokenExpires_at = s.length() - 1;
        String refreshTokenExpiry = s.substring(startOfRefreshTokenExpires_at, endOfRefreshTokenExpires_at);

        //------put in sharedPreferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        pref.edit().putString("authorisationToken", authorisationToken).apply();
        pref.edit().putString("authorisationTokenExpiry", authorisationTokenExpiry).apply();
        pref.edit().putString("principalID", principalID).apply();
        pref.edit().putString("refreshToken", refreshToken).apply();
        pref.edit().putString("refreshTokenExpiry", refreshTokenExpiry).apply();
    }

    public void clearCookies() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    private void injectCSS(WebView mywebview){
        try {
            InputStream inputStream = getAssets().open("black.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            mywebview.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
