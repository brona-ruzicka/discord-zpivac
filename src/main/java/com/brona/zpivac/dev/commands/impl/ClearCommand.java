package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class ClearCommand implements CommandBase {

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(
                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    guildAudio.getQueue().clear();

                    return event
                            .editReply()
                            .withEmbeds(
                                    EmbedCreateSpec
                                            .builder()
                                            .color(Color.SEA_GREEN)
                                            .title("Queue successfully cleared")
                                            .build()
                            )
                            .then();
                }).orElse(event
                        .editReply()
                        .withEmbeds(
                                EmbedCreateSpec
                                        .builder()
                                        .color(Color.RED)
                                        .title("An error occurred during clearing")
                                        .build()
                        )
                        .then()
                )
                        .onErrorResume(__ -> event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.RED)
                                                .title("An error occurred during clearing")
                                                .build()
                                )
                                .then()
                        )

        );
    }
}
