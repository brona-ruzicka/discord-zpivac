package com.brona.zpivac.audio;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class DiscordAudioProvider extends AudioProvider {

    protected final AudioPlayer audioPlayer;
    protected final MutableAudioFrame frame;

    public DiscordAudioProvider(AudioPlayer audioPlayer) {
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
        this.audioPlayer = audioPlayer;
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(this.getBuffer());
    }

    @Override
    public boolean provide() {
        final boolean didProvide = this.audioPlayer.provide(this.frame);
        if (didProvide) {
            this.getBuffer().flip();
        }
        return didProvide;
    }

}
