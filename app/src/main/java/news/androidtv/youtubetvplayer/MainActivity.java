package news.androidtv.youtubetvplayer;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.media.tv.companionlibrary.TvPlayer;

import news.androidtv.libs.player.YouTubePlayerView;

/**
 * Created by Nick on 10/27/2016.
 */
public class MainActivity extends Activity {
    private YouTubePlayerView mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_youtube);
        mPlayer = ((YouTubePlayerView) findViewById(R.id.player_youtube));
        mPlayer.loadVideo("kjEmkdrdRtI");
        mPlayer.registerCallback(new TvPlayer.Callback() {
            @Override
            public void onCompleted() {
                super.onCompleted();
                mPlayer.loadVideo("kjEmkdrdRtI");
            }
        });
    }
}
