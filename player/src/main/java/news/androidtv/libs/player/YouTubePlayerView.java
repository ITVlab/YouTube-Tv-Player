package news.androidtv.libs.player;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by Nick on 10/27/2016.
 */

public class YouTubePlayerView extends WebView {
    public YouTubePlayerView(Context context) {
        super(context);
    }

    private void startPlayingVideo() {
        runJS("yt.player.getPlayerByElement('player').playVideo()");
    }
}
