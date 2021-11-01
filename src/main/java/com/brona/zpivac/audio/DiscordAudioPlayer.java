package com.brona.zpivac.audio;

import com.sedmelluq.discord.lavaplayer.filter.PcmFilterFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DiscordAudioPlayer implements AudioPlayer {

    protected final AudioPlayer delegate;

    public DiscordAudioPlayer(AudioPlayer delegate) {
        this.delegate = delegate;
    }

    @Override
    public AudioTrack getPlayingTrack() {
        return delegate.getPlayingTrack();
    }

    @Override
    public void playTrack(AudioTrack track) {
        delegate.playTrack(track);
    }

    @Override
    public boolean startTrack(AudioTrack track, boolean noInterrupt) {
        return delegate.startTrack(track, noInterrupt);
    }

    @Override
    public void stopTrack() {
        delegate.stopTrack();
    }

    @Override
    public int getVolume() {
        return delegate.getVolume();
    }

    @Override
    public void setVolume(int volume) {
        delegate.setVolume(volume);
    }

    @Override
    public void setFilterFactory(PcmFilterFactory factory) {
        delegate.setFilterFactory(factory);
    }

    @Override
    public void setFrameBufferDuration(Integer duration) {
        delegate.setFrameBufferDuration(duration);
    }

    @Override
    public boolean isPaused() {
        return delegate.isPaused();
    }

    @Override
    public void setPaused(boolean value) {
        delegate.setPaused(value);
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    @Override
    public void addListener(AudioEventListener listener) {
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(AudioEventListener listener) {
        delegate.removeListener(listener);
    }

    @Override
    public void checkCleanup(long threshold) {
        delegate.checkCleanup(threshold);
    }

    @Override
    public AudioFrame provide() {
        return delegate.provide();
    }

    @Override
    public AudioFrame provide(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return delegate.provide(timeout, unit);
    }

    @Override
    public boolean provide(MutableAudioFrame targetFrame) {
        return delegate.provide(targetFrame);
    }

    @Override
    public boolean provide(MutableAudioFrame targetFrame, long timeout, TimeUnit unit) throws TimeoutException, InterruptedException {
        return delegate.provide(targetFrame, timeout, unit);
    }

    public DiscordAudioProvider createProvider() {
        return new DiscordAudioProvider(this);
    }

}
