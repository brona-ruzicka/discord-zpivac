package com.brona.zpivac.dev.audio;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class GuildAudioRegistry {

    private GuildAudioRegistry() { }

    protected static final Map<Snowflake, GuildAudio> MAP = new HashMap<>();

    public static Optional<GuildAudio> get(Snowflake guildId) {
        return Optional.ofNullable(MAP.get(guildId));
    }

    public static GuildAudio create(GatewayDiscordClient client, Snowflake guildId, Snowflake channelId) {
        return MAP.computeIfAbsent(guildId, __ -> {
            GuildAudio guildAudio = new GuildAudio(client, channelId);
            guildAudio.onDisconnect().doOnSuccess(___ ->  MAP.remove(guildId)).subscribe();
            return guildAudio;
        });
    }


}
