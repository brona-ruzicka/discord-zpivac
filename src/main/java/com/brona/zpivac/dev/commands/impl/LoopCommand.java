package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import com.brona.zpivac.dev.util.LoopingQueue;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class LoopCommand implements CommandBase {
    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(

                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    try {

                        String arguments = event.getArguments().get();
                        String[] split = arguments.split("\\s+");

                        if (split.length > 0) {

                            int position = split[0].equals("all") ? -1 : (Integer.parseInt(split[0]) - 1);
                            int repeatCount;

                            if (split.length > 1) {
                                repeatCount = Integer.parseInt(split[1]) - 1;
                            } else {
                                repeatCount = LoopingQueue.REPEAT_INFINITELY;
                            }


                            if (position == -1) {
                                guildAudio.getQueue().setRepeatAll(0, guildAudio.getQueue().size(), repeatCount);
                            } else {
                                guildAudio.getQueue().setRepeat(position, repeatCount);
                            }

                        } else {
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
