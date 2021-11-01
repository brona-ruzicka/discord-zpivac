package com.brona.zpivac.dev.commands.core;

import com.brona.zpivac.dev.commands.impl.*;
import discord4j.extension.commands.event.CommandEvent;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public final class CommandDispatcher {

    protected static final Map<String, CommandBase> COMMANDS = new HashMap<>();

    protected static void register(CommandBase command) {
        COMMANDS.put(command.getName(), command);
    }

    static {
        register(new ClearCommand());
        register(new JoinCommand());
        register(new JumpCommand());
        register(new LeaveCommand());
        register(new LinkCommand());
        register(new LoopCommand());
        register(new MoveCommand());
        register(new NowCommand());
        register(new PauseCommand());
        register(new PingCommand());
        register(new PlayCommand());
        register(new QueueCommand());
        register(new RemoveCommand());
        register(new ResumeCommand());
        register(new SkipCommand());
    }



    public static Mono<Void> execute(CommandEvent<?> event) {
        return Mono.defer(() -> {

            CommandBase command = COMMANDS.get(event.getCommandName());

            if (command != null)
                return command.execute(event);

            return Mono.error(new IllegalStateException("Unknown command: " + event.getCommandName()));
        })
        .onErrorResume(throwable -> {
            throwable.printStackTrace();
            return Mono.empty();
        });
    }



    private CommandDispatcher() { }

}
