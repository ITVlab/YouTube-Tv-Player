package news.androidtv.youtubetvplayer;

import android.app.Activity;
import android.os.Bundle;

import news.androidtv.libs.player.YouTubePlayerView;

/**
 * Created by Nick on 10/27/2016.
 */

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_youtube);
        ((YouTubePlayerView) findViewById(R.id.player_youtube)).loadVideo("MeB3eYk1Ze0");
    }
}
