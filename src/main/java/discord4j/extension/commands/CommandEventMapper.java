package discord4j.extension.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.event.message.MessageCommandEvent;
import discord4j.extension.commands.resolver.MessageCommandResolver;
import discord4j.extension.commands.resolver.ResolvedMessageCommand;
import discord4j.extension.commands.util.DispatchableEventFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;


/**
 * <p>This is the core of the Discord4jCommands library. It processes events produced by {@link GatewayDiscordClient#on}
 * methods. If it evaluates, that the supplied {@link Event} is a command, it creates a wrapper object,
 * a child of {@link CommandEvent}. </p>
 *
 * <p>The {@code CommandEventMapper} will produce a {@link CommandEvent} if:</p>
 * <ul>
 *     <li>The event is an instance of {@link ApplicationCommandInteractionEvent}.</li>
 *     <li>The event is an instance of {@link MessageCreateEvent} and the associated
 *     {@link MessageCommandResolver} returns a non-null value.</li>
 * </ul>
 *
 * <p>The {@code CommandEventMapper} allows you to either process the created events directly via {@link #mapCommands} methods
 * or by using {@link #dispatchCommands} methods you can have the wrapper events redispatched back into the Discord4j
 * {@link EventDispatcher} and then process them via {@link GatewayDiscordClient#on} methods elsewhere.</p>
 *
 * @see CommandEvent
 */
public class CommandEventMapper {

    /**
     * The {@link MessageCommandResolver} associated with this {@code CommandEventMapper}.
     */
    protected final MessageCommandResolver resolver;

    /**
     * Constructs a new {@code CommandEventMapper} with the specified message command resolver.
     *
     * @param resolver {@link MessageCommandResolver} to be associated with the constructed {@code CommandEventMapper}.
     */
    protected CommandEventMapper(MessageCommandResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Returns a new {@code CommandEventMapper} with its command resolver set to {@link MessageCommandResolver#NOOP},
     * making it not react to any message commands.
     *
     * @return The created {@code CommandEventMapper}.
     */
    public static CommandEventMapper create() {
        return new CommandEventMapper(MessageCommandResolver.NOOP);
    }

    /**
     * Returns a new {@code CommandEventMapper} with the specified message command resolver.
     *
     * @param resolver {@link MessageCommandResolver} to be associated with the new {@code CommandEventMapper}.
     * @return The created {@code CommandEventMapper}.
     */
    public static CommandEventMapper create(MessageCommandResolver resolver) {
        return new CommandEventMapper(resolver);
    }

    /**
     * Returns a {@link DispatchableEventFlux} which publishes the produced {@link CommandEvent} instances.
     *
     * @param events The flux of {@link Event} instances to process.
     * @return A {@link DispatchableEventFlux} publishing the {@link CommandEvent} instances.
     */
    public DispatchableEventFlux mapCommands(Flux<Event> events) {
        return new DispatchableEventFlux(
                events.flatMap(event -> Mono.justOrEmpty(this.mapCommand(event))
                ));
    }

    /**
     * Returns a {@link DispatchableEventFlux} which publishes the produced {@link CommandEvent} instances.
     *
     * @param client The client, whose {@link Event} instances to process.
     * @return A {@link DispatchableEventFlux} publishing the {@link CommandEvent} instances.
     */
    public DispatchableEventFlux mapCommands(GatewayDiscordClient client) {
        return this.mapCommands(client.on(Event.class));
    }

    /**
     * Returns a {@link Mono} which upon subscription starts redispatching the produced {@link CommandEvent} instances
     * to their respective {@link GatewayDiscordClient}.
     *
     * @param events The flux of {@link Event} instances to process.
     * @return A {@link Mono} redispatching the produced {@link CommandEvent} instances.
     */
    public Mono<Void> dispatchCommands(Flux<Event> events) {
        return mapCommands(events).dispatch();
    }

    /**
     * Returns a {@link Mono} which upon subscription starts redispatching the produced {@link CommandEvent} instances
     * to their respective {@link GatewayDiscordClient}.
     *
     * @param client The client, whose {@link Event} instances to process.
     * @return A {@link Mono} redispatching the produced {@link CommandEvent} instances.
     */
    public Mono<Void> dispatchCommands(GatewayDiscordClient client) {
        return mapCommands(client).dispatch();
    }

    /**
     * <p>Processes the supplied {@link Event}. It will return an instance of {@link CommandEvent} if:</p>
     * <ul>
     *     <li>The supplied event is an instance of {@link ApplicationCommandInteractionEvent}.</li>
     *     <li>The supplied event is an instance of {@link MessageCreateEvent} and the associated
     *     {@link MessageCommandResolver} returns a non-null value.</li>
     * </ul>
     * <p>Otherwise it returns {@code null}.</p>
     *
     * @param event The Discord4j event to process.
     * @return A {@link CommandEvent} if the event is command, else {@code null}.
     */
    @Nullable
    public CommandEvent<?> mapCommand(Event event) {

        if (event instanceof ApplicationCommandInteractionEvent)
            return new InteractionCommandEvent((ApplicationCommandInteractionEvent) event);

        if (event instanceof MessageCreateEvent) {
            ResolvedMessageCommand resolvedCommand = resolver.resolve((MessageCreateEvent) event);

            if (resolvedCommand != null)
                return new MessageCommandEvent((MessageCreateEvent) event, resolvedCommand);
        }

        return null;
    }

    /**
     * Returns the associated {@link MessageCommandResolver}.
     *
     * @return The associated resolver.
     */
    public MessageCommandResolver getResolver() {
        return resolver;
    }

    /**
     * Creates a new {@code CommandEventMapper} with the supplied {@link MessageCommandResolver}.
     *
     * @param resolver the resolver for the new command event mapper.
     * @return New updated {@code CommandEventMapper}.
     */
    public CommandEventMapper withResolver(MessageCommandResolver resolver) {
        return new CommandEventMapper(resolver);
    }

}
