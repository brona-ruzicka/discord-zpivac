package com.brona.zpivac.dev.audio;

import com.brona.zpivac.audio.DiscordAudioManager;
import com.brona.zpivac.audio.DiscordAudioPlayer;
import com.brona.zpivac.dev.util.LoopingQueue;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Collection;
import java.util.List;


public class GuildAudio {

    public static final Duration DISCONNECT_DURATION = Duration.ofSeconds(5);

    protected final GatewayDiscordClient client;
    protected final DiscordAudioPlayer player;

    protected final LoopingQueue<AudioTrack> queue;

    protected Mono<VoiceConnection> voiceConnectionMono;
    protected final Sinks.Empty<Void> onDisconnectSink;

    public GuildAudio(GatewayDiscordClient client, Snowflake channelId) {
        this.client = client;
        this.player = DiscordAudioManager.createPlayer();

        this.player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                if (endReason.mayStartNext) {
                    playInternal(queue.pollAndPeek());
                }

            }
        });

        this.queue = new GuildLoopingQueue();

        this.voiceConnectionMono = client.getChannelById(channelId)
                .cast(VoiceChannel.class)
                .flatMap(channel -> channel.join()
                        .withProvider(player.createProvider())
                        .withSelfDeaf(true)
                ).retry(1).cache();

        this.voiceConnectionMono.flatMapMany(VoiceConnection::stateEvents)
                .filter(VoiceConnection.State.DISCONNECTED::equals)
                .flatMap(a -> this.disconnect())
                .subscribe();

        this.onDisconnectSink = Sinks.empty();
    }

    public GatewayDiscordClient getClient() {
        return client;
    }

    public LoopingQueue<AudioTrack> getQueue() {
        return queue;
    }

    public AudioTrack currentTrack() { return player.getPlayingTrack(); }

    public int getVolume() {
        return player.getVolume();
    }

    public void setVolume(int volume) {
        player.setVolume(volume);
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public void setPaused(boolean value) {
        player.setPaused(value);
    }

    public Mono<Void> disconnect() {
        return voiceConnectionMono.flatMap(VoiceConnection::disconnect)
                .doOnSuccess(v -> {
                    voiceConnectionMono = Mono.empty();
                    player.destroy();
                    queue.clear();
                    onDisconnectSink.tryEmitEmpty();
                })
                .then();
    }

    public Mono<Void> onDisconnect() {
        return onDisconnectSink.asMono();
    }

    public void skip() {
        playInternal(queue.pollAndPeek());
    }

    protected void playInternal(AudioTrack track) {
        player.playTrack(track);
    }


    protected class GuildLoopingQueue extends LoopingQueue<AudioTrack> {

        @Override
        public AudioTrack poll() {
            LoopingElement<AudioTrack> value = delegate.poll();

            if (value == null)
                return null;

            if (value.getRepeats() < 0) {
                delegate.add(new LoopingElement<>(value.get().makeClone(), value.getRepeats()));
            } else if (value.getRepeats() > 0) {
                delegate.add(new LoopingElement<>(value.get().makeClone(), value.getRepeats() - 1));
            }

            return value.get();
        }

        @Override
        public void add(AudioTrack track, int position, int repeatCount) {
            super.add(track, position, repeatCount);

            if (position == 0 && !isPaused())
                playInternal(this.peek());
        }

        @Override
        public void addAll(Collection<AudioTrack> tracks, int position, int repeatCount) {
            super.addAll(tracks, position, repeatCount);

            if (position == 0 && !isPaused())
                playInternal(this.peek());
        }

        @Override
        public void move(int position, int newPosition) {
            super.move(position, newPosition);

            if (position == 0 || newPosition == 0 && !isPaused())
                playInternal(this.peek());
        }

        @Override
        public void moveAll(int position, int count, int newPosition) {
            super.moveAll(position, count, newPosition);

            if (position == 0 || newPosition == 0 && !isPaused())
                playInternal(this.peek());
        }

        @Override
        public AudioTrack remove(int position) {
            AudioTrack track = super.remove(position);

            if (position == 0 && !isPaused())
                playInternal(this.peek());

            return track;
        }

        @Override
        public List<AudioTrack> removeAll(int position, int count) {
            List<AudioTrack> tracks = super.removeAll(position, count);

            if (position == 0 && !isPaused())
                playInternal(this.peek());

            return tracks;
        }

        @Override
        public void clear() {
            super.clear();
            playInternal(null);
        }
    }

}
