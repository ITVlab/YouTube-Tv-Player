package news.androidtv.libs.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Nick on 10/27/2016.
 */

public abstract class AbstractWebPlayer extends WebView {
    private static final String TAG = AbstractWebPlayer.class.getSimpleName();
    private static final boolean DEBUG = true;

    private WebEventsListener mListener;

    public AbstractWebPlayer(Context context) {
        super(context);
        initialize(context);
    }

    public AbstractWebPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public AbstractWebPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public AbstractWebPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        mListener = new WebEventsListener() {
            @Override
            public void onWindowLoad() {
                if (DEBUG) {
                    Log.d(TAG, "Page has finished loading");
                }
                onPlayVideo();
            }
        };
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(false);
        getSettings().setSupportMultipleWindows(false);
        setWebViewClient(new WebPlayerClient(this, mListener));
        getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.35 Safari/537.36"); //Claim to be a desktop
        setKeepScreenOn(true);
    }

    public void setVideoUrlTo(String url) {
        if (DEBUG) {
            Log.d(TAG, "Loading URL " + url);
        }
        loadUrl(url);
    }

    protected void runJavascript(final String js) {
        post(new Runnable() {
            @Override
            public void run() {
                if (DEBUG) {
                    if (js.length() < 50) {
                        Log.d(TAG, "Execute " + js);
                    } else {
                        Log.d(TAG, "Execute " + js.substring(0, 49));
                    }
                }
                loadUrl("javascript:try { " + js + "} catch(error) { Android.onError(error.message) }");
            }
        });
    }

    protected abstract void onPlayVideo();

    public interface WebEventsListener {
        void onWindowLoad();
    }
}
