package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class PingCommand implements CommandBase {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event
                .createReply()
                .withEmbeds(
                        EmbedCreateSpec
                                .builder()
                                .color(Color.SEA_GREEN)
                                .title("Pong!")
                                .build()
                )
                .then();
    }

}
