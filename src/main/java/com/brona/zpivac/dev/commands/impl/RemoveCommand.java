package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class RemoveCommand implements CommandBase {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(

                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    try {

                        String arguments = event.getArguments().get();
                        String[] split = arguments.split("\\s+");

                        if (split.length > 0) {

                            int position = split[0].equals("all") ? -1 : Integer.parseInt(split[0]) - 1;

                            if (position == -1) {
                                guildAudio.getQueue().clear();
                            } else {
                                guildAudio.getQueue().remove(position);
                            }

                            return event
                                    .editReply()
                                    .withEmbeds(
                                            EmbedCreateSpec
                                                    .builder()
                                                    .color(Color.SEA_GREEN)
                                                    .title("Looping set successfully")
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
                                                    .title("Requires an id parameter")
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
                                                .title("An error occurred during removing")
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
                                        .title("An error occurred during removing")
                                        .build()
                        )
                        .then()
                )

        );
    }

}
