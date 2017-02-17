package news.androidtv.libs.player;

import android.content.Context;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.Looper;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.Surface;

import com.google.android.media.tv.companionlibrary.TvPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The YouTubePlayerView is an extension of the {@link AbstractWebPlayer} class and an
 * implementation of the {@link com.google.android.media.tv.companionlibrary.TvPlayer} to work with
 * Tv Input Framework applications.
 *
 * Created by Nick on 10/27/2016.
 */
public class YouTubePlayerView extends AbstractWebPlayer implements PlaybackControls {
    private boolean isVideoPlaying;
    private List<Callback> mCallbackList = new ArrayList<>();

    private Runnable checkPlaybackStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVideoPlaying) {
                runJavascript("if (yt.player.getPlayerByElement('player').getPlayerState() == 0) { Android.videoEnded(); }");
                runJavascript("Android.updatePosition(yt.player.getPlayerByElement('player').getCurrentTime());");
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
        runJavascript("yt.player.getPlayerByElement('player').playVideo();");
        runJavascript("Android.updateDuration(yt.player.getPlayerByElement('player').getDuration());");
        setVolume(1);
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
        for (Callback callback : mCallbackList) {
            callback.onStarted();
        }
    }

    protected void onEndVideo() {
        isVideoPlaying = false;
        for (Callback callback : mCallbackList) {
            callback.onCompleted();
        }
    }

    @Override
    public void play() {
        runJavascript("yt.player.getPlayerByElement('player').playVideo()");
        for (Callback callback : mCallbackList) {
            callback.onResumed();
        }
    }

    @Override
    public void registerCallback(Callback callback) {
        if (mCallbackList == null) {
            mCallbackList = new ArrayList<>();
        }
        mCallbackList.add(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        if (mCallbackList != null) {
            mCallbackList.remove(callback);
        }
    }

    @Override
    public void seekTo(long position) {
        skip(position);
    }

    @Override
    public void setPlaybackParams(PlaybackParams params) {
        // FIXME to implement
    }

    @Override
    public void setVolume(float volume) {
        runJavascript("yt.player.getPlayerByElement('player').setVolume("+ volume * 100 +")");
    }

    @Override
    public void pause() {
        runJavascript("yt.player.getPlayerByElement('player').pauseVideo()");
        for (Callback callback : mCallbackList) {
            callback.onPaused();
        }
    }

    @Override
    public void skip(long ms) {
        runJavascript("yt.player.getPlayerByElement('player').seekTo(" + ms + ", true)");
    }
}
