package com.brona.zpivac.dev;

import com.brona.zpivac.dev.commands.core.CommandDispatcher;
import discord4j.common.ReactorResources;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.extension.commands.CommandEventMapper;
import discord4j.extension.commands.resolver.MessageCommandResolver;
import discord4j.voice.VoiceReactorResources;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;


public class Zpivac {

    public static void main(String[] args) {

        Properties properties = loadProperties("bot.properties");

        GatewayDiscordClient client = createClient(properties.getProperty("token"));

        listenForCommands(client, properties.getProperty("prefix"));

        // registerCommands(client);

        client.onDisconnect().block();
    }


    public static Properties loadProperties(String filename) {

        Properties properties = new Properties();

        InputStream stream = Zpivac.class.getClassLoader().getResourceAsStream(filename);

        try {
            properties.load(stream);
        } catch (NullPointerException | IOException ignored) { }

        return properties;
    }

    public static GatewayDiscordClient createClient(String token) {

        return DiscordClient
                .create(token)
                .gateway()
                .setVoiceReactorResources(reactor -> new VoiceReactorResources(ReactorResources.builder()
                        .httpClient(ReactorResources.DEFAULT_HTTP_CLIENT.get().secure())
                        .blockingTaskScheduler(reactor.getBlockingTaskScheduler())
                        .timerTaskScheduler(reactor.getTimerTaskScheduler())
                        .build()
                ))
                .login()
                .retry(1)
                .block();
    }

    public static void listenForCommands(GatewayDiscordClient client, String prefix) {
        CommandEventMapper.create()
                .withResolver(MessageCommandResolver.prefixed(prefix))
                .mapCommands(client)
                .flatMap(event -> CommandDispatcher
                        .execute(event)
                        .onErrorResume(throwable -> {
                            throwable.printStackTrace();
                            return Mono.empty();
                        })
                )
                .subscribe();
    }


    public static void registerCommands(GatewayDiscordClient client) {

        client.getRestClient().getApplicationService().bulkOverwriteGlobalApplicationCommand(
                client.getApplicationInfo().block().getId().asLong(),
                Arrays.asList(
                        ApplicationCommandRequest.builder()
                                .name("clear")
                                .description("Clears the music queue.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("join")
                                .description("Joins your current voice channel.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("jump")
                                .description("Jumps to the specified song.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("index")
                                                .description("The index of the song to jump to.")
                                                .type(4)
                                                .required(true)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("leave")
                                .description("Leaves the channel ist currently connected to.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("link")
                                .description("Plays the linked music source.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("url")
                                                .description("The url of the source.")
                                                .type(3)
                                                .required(true)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("loop")
                                .description("The bot marks this song to be repeated.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("index")
                                                .description("The index of the song to be repeated.")
                                                .type(3)
                                                .required(true)
                                                .build(),
                                        ApplicationCommandOptionData.builder()
                                                .name("repeats")
                                                .description("The number of repeats for this song.")
                                                .type(4)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("move")
                                .description("Moves the specified song to a new position.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("index")
                                                .description("The current position of the song.")
                                                .type(4)
                                                .required(true)
                                                .build(),
                                        ApplicationCommandOptionData.builder()
                                                .name("new_index")
                                                .description("The new position of the song.")
                                                .type(4)
                                                .required(true)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("now")
                                .description("Shows the currently played song.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("pause")
                                .description("Pauses the player.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("ping")
                                .description("Replies with pong!")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("play")
                                .description("Searches for best match of the supplied text on Youtube and plays it.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("name")
                                                .description("The searched name.")
                                                .type(3)
                                                .required(true)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("queue")
                                .description("Displays the current queue.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("page")
                                                .description("The page of the queue.")
                                                .type(4)
                                                .required(false)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("remove")
                                .description("Removes the specified song.")
                                .options(Arrays.asList(
                                        ApplicationCommandOptionData.builder()
                                                .name("index")
                                                .description("The index of the song to be removed.")
                                                .type(4)
                                                .required(true)
                                                .build()
                                ))
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("resume")
                                .description("Resumes the player.")
                                .build(),
                        ApplicationCommandRequest.builder()
                                .name("skip")
                                .description("Skips the current song.")
                                .build()
                )
        ).subscribe(System.out::println, System.err::println);
    }

}
