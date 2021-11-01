package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.audio.DiscordAudioManager;
import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.object.VoiceState;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class PlayCommand implements CommandBase {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(
                event.getMember().map(member ->
                        member.getVoiceState()
                                .map(VoiceState::getChannelId)
                                .flatMap(Mono::justOrEmpty)
                                .map(id -> GuildAudioRegistry.create(
                                        event.getClient(),
                                        event.getGuildId().get(),
                                        id
                                ))
                                .flatMap(guildAudio -> DiscordAudioManager
                                            .load("ytsearch: " + event.getArguments().get())
                                        .flatMap(result -> result.isEmpty() ? event
                                                .editReply()
                                                .withEmbeds(
                                                        EmbedCreateSpec
                                                                .builder()
                                                                .color(Color.RED)
                                                                .title("An error occurred during playing")
                                                                .build()
                                                )
                                                .then()
                                        : Mono.fromRunnable(() -> guildAudio.getQueue().add(result.getList().get(0)))
                                                .then(event
                                                        .editReply()
                                                        .withEmbeds(
                                                                EmbedCreateSpec
                                                                        .builder()
                                                                        .color(Color.SEA_GREEN)
                                                                        .title("Successfully queued **" + result.getList().get(0).getInfo().title + "**")
                                                                        .build()
                                                        )
                                                )
                                                .then()
                                        )
                                )
                        )
                        .orElse(event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.RED)
                                                .title("An error occurred during playing")
                                                .build()
                                )
                                .then()
                        )

        );
    }

}
