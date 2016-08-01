package com.github.alinz.reactnativewebviewbridge;

import android.app.Activity;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

public class FullScreenWebViewBridgeManager extends SimpleViewManager<CustomView> {
    private static final String REACT_CLASS = "RCTFullScreenWebViewBridge";
    private final Activity mActivity;

    public FullScreenWebViewBridgeManager(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected CustomView createViewInstance(ThemedReactContext themedReactContext) {
        return new CustomView(themedReactContext, mActivity);
    }

}
