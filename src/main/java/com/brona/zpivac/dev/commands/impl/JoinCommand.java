package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.object.VoiceState;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class JoinCommand implements CommandBase {

    @Override
    public String getName() {
        return "join";
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
                                .then(event
                                        .editReply()
                                        .withEmbeds(
                                                EmbedCreateSpec
                                                        .builder()
                                                        .color(Color.SEA_GREEN)
                                                        .title("Joined successfully")
                                                        .build()
                                        )
                                )
                                .onErrorResume(__ -> event
                                        .editReply()
                                        .withEmbeds(
                                                EmbedCreateSpec
                                                        .builder()
                                                        .color(Color.RED)
                                                        .title("An error occurred during joining")
                                                        .build()
                                        ))
                                .then()
                )
                .orElse(event
                        .editReply()
                        .withEmbeds(
                                EmbedCreateSpec
                                        .builder()
                                        .color(Color.RED)
                                        .title("This command can be used only in server channels")
                                        .build()
                        )
                        .then()
                )
        );
    }

}
