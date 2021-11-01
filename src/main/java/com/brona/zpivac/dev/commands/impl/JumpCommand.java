package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class JumpCommand implements CommandBase {

    @Override
    public String getName() {
        return "jump";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(

                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    try {

                        int position = Integer.parseInt(event.getArguments().get().trim()) - 1;

                        if (position > 0)
                            guildAudio.getQueue().moveAll(0, position,
                                    guildAudio.getQueue().size() - position
                            );

                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.SEA_GREEN)
                                                .title("Successfully jumped to position " + (position + 1))
                                                .build()
                                )
                                .then();

                    } catch (Exception e) {
                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.RED)
                                                .title("Requires an number index parameter")
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
