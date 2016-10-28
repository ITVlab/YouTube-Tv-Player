package news.androidtv.libs.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

/**
 * Created by Nick on 10/27/2016.
 */

public class YouTubePlayerView extends AbstractWebPlayer implements MediaControls {
    private boolean isVideoPlaying;

    private Runnable checkPlaybackStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVideoPlaying) {
                runJavascript("if (yt.player.getPlayerByElement('player').getPlayerState() == 0) { Android.videoEnded(); }");
                new Handler(Looper.getMainLooper()).postDelayed(this, 100);
            }
        }
    };

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
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
    }

    protected void onEndVideo() {
        isVideoPlaying = false;
    }

    @Override
    public void play() {
        runJavascript("yt.player.getPlayerByElement('player').playVideo()");
    }

    @Override
    public void pause() {
        runJavascript("yt.player.getPlayerByElement('player').pauseVideo()");
    }

    @Override
    public void skip(long ms) {
        runJavascript("yt.player.getPlayerByElement('player').seekTo(" + ms + ", true)");
    }
}
