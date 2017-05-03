# YouTube Tv Player
A YouTube player for Android TV

## Get it
You can easily add this library to your app through JitPack.

`build.gradle`:

    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }

`app/build.gradle`:

    dependencies {
        compile 'com.github.itvlab:youtube-tv-player:0.2.1'
    }

## How does this work?
Since the standard Android YouTube SDK doesn't work for video playback, this uses a `WebView` and loads the YouTube URLs using their embedded URL. Then, playback is controlled through JavaScript.

## Tv Input Framework / Live Channels
This project includes the [TIF Companion Library](github.com/googlesamples/androidtv-sample-inputs) and automatically implements the `TvPlayer` interface, so it's easy to include this in your Live Channel app.

## How to use in my project
The `YouTubePlayerView` is an extension of the `AbstractWebPlayer` and is both a media player (implementing `TvPlayer`) and a view. You can instantiate it, load a YouTube video id, and include an event handler to do things once I am done.

    mPlayer = ((YouTubePlayerView) findViewById(R.id.player_youtube));
    mPlayer.loadVideo("kjEmkdrdRtI");
    mPlayer.registerCallback(new TvPlayer.Callback() {
        @Override
        public void onCompleted() {
            super.onCompleted();
            mPlayer.loadVideo("kjEmkdrdRtI");
        }
    });
    
### How can I use this for my Live Channels app?
You can create a `YouTubePlayerView` as the session's overlay view. Then, when you want to play a program, load the desired video and enable the overlay view. This will start playing your video without needing to handle a surface while giving you full access to playback controls.

#### DVR
If you want to add DVR functionality, you can simply save bookmarks to the desired video and play it later without having to download it and do disk management.

### Can I use this for Vimeo or other players?
Sure! You can create a new class which extends `AbstractWebPlayer` and implement the necessary methods. Do a pull request with a different player and it'll be added to the library.

## Example
You can check out [SubChannel](http://github.com/itvlab/subchannel) to see an example of this library in action.

## Known Issues
* Missing some advanced YouTube features.
* Some videos won't play due to copyright restrictions. This isn't handled.