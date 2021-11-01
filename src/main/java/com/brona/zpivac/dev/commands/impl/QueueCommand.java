package com.brona.zpivac.dev.commands.impl;

import com.brona.zpivac.dev.audio.GuildAudioRegistry;
import com.brona.zpivac.dev.commands.core.CommandBase;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.brona.zpivac.dev.util.LoopingQueue.REPEAT_INFINITELY;

public class QueueCommand implements CommandBase {

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public Mono<Void> execute(CommandEvent<?> event) {
        return event.deferReply().then(
                event.getGuildId().flatMap(GuildAudioRegistry::get).map(guildAudio -> {

                    int page = 0;

                    try {
                        page = Integer.parseInt(event.getArguments().get().trim()) - 1;
                    } catch (Exception ignored) { }

                    int queueSize = guildAudio.getQueue().size();
                    int initIndex = page * 10;

                    if (initIndex < 0 || (initIndex != 0 && initIndex >= queueSize)) {
                        return event
                                .editReply()
                                .withEmbeds(
                                        EmbedCreateSpec
                                                .builder()
                                                .color(Color.RED)
                                                .title("Queue page out of range")
                                                .build()
                                )
                                .then();
                    }

                    int endIndex = Math.min((page + 1) * 10, queueSize);

                    AtomicInteger index = new AtomicInteger(initIndex);

                    return event.editReply()
                            .withEmbeds(EmbedCreateSpec.builder()
                                    .color(Color.CYAN)
                                    .title("The current queue looks like this" + (queueSize <= 10 ? "" : (" (" + (page+1) + "/" + (queueSize/10 + 1) + ")")))
                                    .description(
                                            guildAudio.getQueue()
                                                    .getElements()
                                                    .subList(initIndex, endIndex)
                                                    .stream()
                                                    .map(element ->
                                                            "`" +
                                                            index.incrementAndGet() +
                                                            ".` `" +
                                                            (element.getRepeats() == REPEAT_INFINITELY ? "Re" : ((element.getRepeats() + 1) + "x")) +
                                                            "` " +
                                                            element.get().getInfo().title
                                                    )
                                                    .collect(Collectors.joining("\n"))
                                    )
                                    .build()
                            )
                            .then();
                })
                .orElse(event
                        .editReply()
                        .withEmbeds(
                                EmbedCreateSpec
                                        .builder()
                                        .color(Color.RED)
                                        .title("An error occurred showing queue")
                                        .build()
                        )
                        .then()
                )

        );
    }


}
