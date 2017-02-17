package news.androidtv.libs.player;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Nick on 10/27/2016.
 */

public class WebInterface {
    private AbstractWebPlayer mAbstractWebPlayer;
    private Context mContext;

    /** Instantiate the interface and set the context */
    protected WebInterface(Context c, AbstractWebPlayer abstractWebPlayer) {
        mContext = c;
        mAbstractWebPlayer = abstractWebPlayer;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void videoEnded() {
        mAbstractWebPlayer.getWebEventsListener().onVideoStatusEnded();
    }

    @JavascriptInterface
    public void updatePosition(int position) {
        mAbstractWebPlayer.getWebEventsListener().onPositionChanged(position);
    }

    @JavascriptInterface
    public void updateDuration(int position) {
        mAbstractWebPlayer.getWebEventsListener().onReceivedDuration(position);
    }

    @JavascriptInterface
    public void onError(String error) {
        mAbstractWebPlayer.getWebEventsListener().onError(error);
    }
}
