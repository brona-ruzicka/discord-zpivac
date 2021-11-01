package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class NowCommand implements CommandBase {

    @Override
    public String getName() {
        return "now";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(
                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    if (guildAudio.currentTrack() == null) {
                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.SEA_GREEN)
                                                .title("Nothing is playing currently")
                                                .build()
                                )
                                .then();
                    } else {

                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.SEA_GREEN)
                                                .title("Song **" + guildAudio.currentTrack().getInfo().title + "** is playing currently")
                                                .description(guildAudio.currentTrack().getInfo().uri)
                                                .build()
                                )
                                .then();
                    }
                }).orElse(event
                        .editReply()
                        .withEmbeds(
                                EmbedCreateSpec
                                        .builder()
                                        .color(Color.RED)
                                        .title("An error occurred during checking which song is playing now")
                                        .build()
                        )
                        .then()
                )

        );
    }
}
