package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class MoveCommand implements CommandBase {
    @Override
    public String getName() {
        return "move";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(

                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    try {

                        String arguments = event.getArguments().get();
                        String[] split = arguments.split("\\s+");

                        if (split.length >= 2) {

                            int oldPos = Integer.parseInt(split[0]) - 1;
                            int newPos = Integer.parseInt(split[1]) - 1;

                            guildAudio.getQueue().move(oldPos, newPos);

                            return event
                                    .editReply()
                                    .withEmbeds(
                                            EmbedCreateSpec
                                                    .builder()
                                                    .color(Color.SEA_GREEN)
                                                    .title("Song moved successfully")
                                                    .build()
                                    )
                                    .then();

                        } else {

                            return event
                                    .editReply()
                                    .withEmbeds(
                                            EmbedCreateSpec
                                                    .builder()
                                                    .color(Color.RED)
                                                    .title("Two arguments required")
                                                    .build()
                                    )
                                    .then();
                        }

                    } catch (Exception e) {
                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.RED)
                                                .title("An error occurred during looping")
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
                                        .title("An error occurred during looping")
                                        .build()
                        )
                        .then()
                )

        );
    }
}
