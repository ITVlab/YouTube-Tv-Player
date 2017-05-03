package news.androidtv.libs.player;

import android.content.Context;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

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
    private static final String TAG = YouTubePlayerView.class.getSimpleName();

    private boolean isVideoStarted;
    private boolean isVideoPlaying;
    private String mVideoId;
    private List<Callback> mCallbackList = new ArrayList<>();

    private Runnable checkPlaybackStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVideoStarted) {
                runJavascript("if (player && player.ended) { Android.videoEnded(); }");
                runJavascript("if (player) { Android.updatePosition(player.currentTime); }");
                new Handler(Looper.getMainLooper()).postDelayed(this, 200);
            }
            if (isVideoPlaying) {
                runJavascript("if (player) { player.play() }"); // Verify that our video should be playing.
            }
            if (!isVideoPlaying) {
                runJavascript("if (player) { player.pause() }"); // Verify that our video should be paused.
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
        mVideoId = videoId;
        setVideoUrlTo("https://www.youtube.com/embed/" + videoId + "?enablejsapi=1&html5=1");

    }

    @Override
    protected void onPlayVideo() {
        // Now first, we need to do more JS injection to get the right element.
        Log.d(TAG, "Get ready to play.");
        runJavascript("window.player = document.querySelector('.html5-main-video');");
        runJavascript("player.click();");
        runJavascript("player.play();");
        runJavascript("Android.updateDuration(player.getDuration());");
        setVolume(1);
        isVideoStarted = true;
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
        for (Callback callback : mCallbackList) {
            callback.onStarted();
        }
    }

    protected void onEndVideo() {
        isVideoStarted = false;
        isVideoPlaying = false;
        for (Callback callback : mCallbackList) {
            callback.onCompleted();
        }
    }

    @Override
    public void play() {
        runJavascript("player.playVideo()");
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runJavascript("player.playbackRate = " + params.getSpeed());
        }
    }

    @Override
    public void setVolume(float volume) {
        runJavascript("player.volume = " + volume);
    }

    @Override
    public void pause() {
        runJavascript("player.pause()");
        isVideoPlaying = false;
        for (Callback callback : mCallbackList) {
            callback.onPaused();
        }
    }

    @Override
    public void skip(long ms) {
        runJavascript("player.currentTime = " + (ms * 1000));
    }

    public String getVideoId() {
        return mVideoId;
    }
}
