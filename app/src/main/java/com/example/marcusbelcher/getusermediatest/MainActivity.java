package com.example.marcusbelcher.getusermediatest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    private WebView mWebView;
    private String mCustomURL = "https://marcusbelcher.github.io/wasm-asm-camera-webgl-test/index.html";
    private String mWebrtcURL = "https://webrtc.github.io/samples/src/content/getusermedia/gum/";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_custom:
                    mWebView.loadUrl(mCustomURL);
                    return true;
                case R.id.navigation_webrtc:
                    mWebView.loadUrl(mWebrtcURL);
                    return true;
                case R.id.navigation_refresh:
                    mWebView.reload();
                    return true;
                case R.id.navigation_open:
                    openBrowserWithURL(mWebView.getUrl());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);

        checkForAndAskForPermissions();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    createWebView();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void createWebView() {

        mWebView = (WebView) findViewById(R.id.webview);
        setUpWebViewDefaults(mWebView);
        mWebView.loadUrl(mCustomURL);
        mWebView.setWebChromeClient(new WebChromeClient() {

            public boolean onConsoleMessage(ConsoleMessage m) {
                Log.d("getUserMedia, WebView", m.message() + " -- From line "
                        + m.lineNumber() + " of "
                        + m.sourceId());

                return true;
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                // getActivity().
                MainActivity.this.runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        // Below isn't necessary, however you might want to:
                        // 1) Check what the site is and perhaps have a blacklist
                        // 2) Have a pop up for the user to explicitly give permission
                        if(request.getOrigin().toString().equals("https://marcusbelcher.github.io/") ||
                                request.getOrigin().toString().equals("https://webrtc.github.io/")) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }
        });
    }

    private void checkForAndAskForPermissions() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
            Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CAMERA)) {

                //

            } else ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            // Permission has already been granted
            createWebView();
        }
    }

    private void setUpWebViewDefaults(WebView webView) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.clearCache(true);
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient());
    }

    private void openBrowserWithURL(String url) {

        Uri uri = Uri.parse("googlechrome://navigate?url=" + url);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        if (i.resolveActivity(MainActivity.this.getPackageManager()) == null) {
            i.setData(Uri.parse(url));
        }

        MainActivity.this.startActivity(i);
    }
}
