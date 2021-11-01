package com.brona.zpivac.dev.commands.core;

import discord4j.extension.commands.event.CommandEvent;
import reactor.core.publisher.Mono;

public interface CommandBase {

    String getName();

    default String getDescription() {
        return "Unspecified command";
    }

    Mono<Void> execute(CommandEvent<?> event);

}
