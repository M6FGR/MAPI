package m6fgr.mapi.client.sound.api;

public interface IConfigurableSound {
    IConfigurableSound pause();
    IConfigurableSound resume();
    IConfigurableSound stop(float fadeOutSeconds);

    IConfigurableSound setVolume(float volume);
    IConfigurableSound setPitch(float pitch);
    IConfigurableSound setSpeed(float speedMultiplier);
    IConfigurableSound seekTo(double seconds);
    IConfigurableSound updateLoopBounds(double startSeconds, double endSeconds);

    boolean isPlaying();
    boolean isPaused();
    double getCurrentTimestamp();
}