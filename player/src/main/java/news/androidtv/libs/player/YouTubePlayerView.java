package news.androidtv.libs.player;

import android.content.Context;
import android.media.PlaybackParams;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;

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
    private String mVideoId;
    private List<Callback> mCallbackList = new ArrayList<>();

    private Runnable checkPlaybackStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVideoPlaying) {
                runJavascript("if (player.getPlayerState() == 0) { Android.videoEnded(); }");
                runJavascript("Android.updatePosition(player.getCurrentTime());");
//                new Handler(Looper.getMainLooper()).postDelayed(this, 100);
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
        setVideoUrlTo("https://www.youtube.com/embed/" + videoId);

    }

    @Override
    protected void onPlayVideo() {
        // Now first, we need to do more JS injection to get the right scripts.
       /* runJavascript("var tag = document.createElement('script');\n" +
                "tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);");
        runJavascript("function onYouTubeIframeAPIReady() { window.player = new YT.Player('player', {\n" +
                "          height: '100%',\n" +
                "          width: '100%',\n" +
                "          videoId: '" + mVideoId + "',\n" +
                "          events: { 'onReady': function() { player.playVideo() } }" +
                "        }); console.log('111'); }");*/

        runJavascript("player.playVideo();");
        runJavascript("Android.updateDuration(player.getDuration());");
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
        runJavascript("player.playVideo()");
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
        // FIXME to implement
    }

    @Override
    public void setVolume(float volume) {
        runJavascript("player.setVolume("+ ((int) (volume * 100))
                + ")");
    }

    @Override
    public void pause() {
        runJavascript("player.pauseVideo()");
        for (Callback callback : mCallbackList) {
            callback.onPaused();
        }
    }

    @Override
    public void skip(long ms) {
        runJavascript("player.seekTo(" + ms + ", true)");
    }
}
