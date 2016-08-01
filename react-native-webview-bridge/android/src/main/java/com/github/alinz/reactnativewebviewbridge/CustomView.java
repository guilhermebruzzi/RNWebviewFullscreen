package com.github.alinz.reactnativewebviewbridge;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.facebook.react.uimanager.ThemedReactContext;

public class CustomView extends LinearLayout {
    private VideoEnabledWebView webView;
    private VideoEnabledWebChromeClient webChromeClient;

    public CustomView(ThemedReactContext context, AttributeSet attrs, Activity activity) {
        super(context, attrs);
        init(activity);
    }

    public CustomView(ThemedReactContext context, Activity activity) {
        this(context, null, activity);
    }

    public void init(final Activity activity) {
        Log.e("CustomView", "" + R.layout.custom_layout);

        final View root = inflate(getContext(), R.layout.custom_layout, this);
        webView = (VideoEnabledWebView)findViewById(R.id.webView);

        // Initialize the VideoEnabledWebChromeClient and set event handlers
        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
        ViewGroup videoLayout = (ViewGroup)findViewById(R.id.videoLayout); // Your own view, read class comments

        webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, null, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                // Your code...
            }
        };

        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
        {
            @Override
            public void toggledFullscreen(boolean fullscreen)
            {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen)
                {
                    WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    activity.getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }

                    new Thread() {
                        @Override
                        public void run() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                }
                            });
                        }
                    }.start();

//                    root.invalidate();
//                    root.requestLayout();
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else
                {
                    WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    activity.getWindow().setAttributes(attrs);
                    if (android.os.Build.VERSION.SDK_INT >= 14)
                    {
                        //noinspection all
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }

            }
        });

        webView.setWebChromeClient(webChromeClient);
        // Call private class InsideWebViewClient
        webView.setWebViewClient(new CustomView.InsideWebViewClient());

        // Navigate anywhere you want, but consider that this classes have only been tested on YouTube's mobile site
        webView.loadUrl("http://m.youtube.com");
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        // Force links to be opened inside WebView and not in Default Browser
        // Thanks http://stackoverflow.com/a/33681975/1815624
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

//    @Override
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if( keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }else{
//            return super.onKeyPreIme(keyCode, event);
//        }
//    }

}
