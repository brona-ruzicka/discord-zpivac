package com.brona.zpivac.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.Collections;

public final class DiscordAudioManager {

    private DiscordAudioManager() { }

    protected static final AudioPlayerManager AUDIO_MANAGER;

    static {
        AUDIO_MANAGER = new DefaultAudioPlayerManager();
        AUDIO_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AUDIO_MANAGER.getConfiguration().setFilterHotSwapEnabled(true);
        AudioSourceManagers.registerRemoteSources(AUDIO_MANAGER);
    }

    public static Mono<DiscordAudioLoadResult> load(String identifier) {
        return Mono.create(sink -> AUDIO_MANAGER
                .loadItem(identifier, createHandler(identifier, sink))
        );
    }

    public static Mono<DiscordAudioLoadResult> loadOrdered(Object orderingKey, String identifier) {
        return Mono.create(sink -> AUDIO_MANAGER
                .loadItemOrdered(orderingKey, identifier, createHandler(identifier, sink))
        );
    }

    private static AudioLoadResultHandler createHandler(String identifier, MonoSink<DiscordAudioLoadResult> sink) {
        return new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                sink.success(new DiscordAudioLoadResult(
                        identifier,
                        false,
                        false,
                        Collections.singletonList(track)
                ));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                sink.success(new DiscordAudioLoadResult(
                        identifier,
                        playlist.getTracks().isEmpty(),
                        playlist.isSearchResult(),
                        playlist.getTracks()
                ));
            }

            @Override
            public void noMatches() {
                sink.success(new DiscordAudioLoadResult(
                        identifier,
                        true,
                        false,
                        Collections.emptyList()
                ));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sink.error(exception);
            }
        };
    }

    public static DiscordAudioPlayer createPlayer() {
        return new DiscordAudioPlayer(AUDIO_MANAGER.createPlayer());
    }

}
