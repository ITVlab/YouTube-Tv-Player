package news.androidtv.libs.player;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Nick on 10/27/2016.
 */

public class YouTubePlayerView extends AbstractWebPlayer {
    public YouTubePlayerView(Context context) {
        super(context);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loadVideo(String videoId) {
        setVideoUrlTo("https://www.youtube.com/embed/" + videoId);
    }

    @Override
    protected void onPlayVideo() {
        runJavascript("yt.player.getPlayerByElement('player').playVideo()");
    }
}
