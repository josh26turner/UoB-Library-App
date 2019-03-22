package spe.uoblibraryapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class ActivitySignIn extends AppCompatActivity {

    private String TAG = "SignIn";
    private ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pBar = findViewById(R.id.loginProgressBar);

        getSupportActionBar().setTitle("Single Sign On");

        WebView loginWebView = findViewById(R.id.loginWebView);

        // Clear all user cache previously store, this will force the user to login again.
        loginWebView.clearCache(true);
        loginWebView.clearHistory();
        clearCookies();

        loginWebView.getSettings().setJavaScriptEnabled(true);
        loginWebView.getSettings().setLoadWithOverviewMode(true);
        loginWebView.getSettings().setUseWideViewPort(true);
        loginWebView.getSettings().setSupportZoom(true);

        loginWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });

        loginWebView.setInitialScale(1);
        loginWebView.loadUrl(Constants.UserAuth.oAuthUrl());
        loginWebView.setWebViewClient(new WebViewClient() {
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
                        finish();
                        return true;
                    } else {

                        // TODO: redirect user to start of activity rather than finish it.
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
        WebView mywebview = findViewById(R.id.loginWebView);
        Log.v("Tag", "Log: " + mywebview.getUrl());
        Log.v("Tag", "Con: " + Constants.UserAuth.oAuthUrl());

        // Dont do anything when back button is pressed?

        if (mywebview.getUrl().equals("https://authn.sd02.worldcat.org/wayf/metaauth-ui/cmnd/protocol/samlpost")){
            finish();

        }
        else if (mywebview.getUrl().equals(Constants.UserAuth.oAuthUrl())){
            finish();
        }
        else{
            mywebview.loadUrl(Constants.UserAuth.oAuthUrl());
            Toast.makeText(getApplicationContext(), "blyn1", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAuthorisationDenied(String s) {
        return s.contains(Constants.UserAuth.authFailureUrl);
    }

    private Map<String, String> getURLParams(String url){
        Log.e(TAG, url);

        String[] things = url.split(Pattern.quote("#"));


        String[] params = things[1].split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }


    private void processAuthorisationString(String s) {
        Map<String, String> params = getURLParams(s);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        pref.edit().putString("authorisationToken", params.get("access_token")).apply();
        pref.edit().putString("authorisationTokenExpiry", params.get("expires_at").replace("Z", "")).apply();
        pref.edit().putString("principalID", params.get("principalID")).apply();
        pref.edit().putString("principalIDNS", params.get("principalIDNS")).apply();
        pref.edit().putString("refreshToken", params.get("refresh_token")).apply();
        pref.edit().putString("refreshTokenExpiry", params.get("refresh_token_expires_at").replace("Z", "")).apply();
    }

    public void clearCookies() {
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();
    }

    private void injectCSS(WebView loginWebView){
        try {
            InputStream inputStream = getAssets().open("black.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            loginWebView.loadUrl("javascript:(function() {" +
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
