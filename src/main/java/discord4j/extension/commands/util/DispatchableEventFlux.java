package discord4j.extension.commands.util;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.extension.commands.event.CommandEvent;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * A {@link Flux} which emits {@link CommandEvent CommandEvents}.
 * <p>
 * The function {@link #dispatch()} allows you to integrate the command events into the discord4j dispatcher. So that
 * they can be accessible by {@link GatewayDiscordClient#on} methods.
 *
 * @see GatewayDiscordClient#on
 * @see EventDispatcher
 */
public class DispatchableEventFlux extends Flux<CommandEvent<?>> {

    /**
     * The delegate Flux for this {@code DispatchableCommandFlux}.
     */
    protected final Flux<CommandEvent<?>> delegate;

    /**
     * Construct a {@code DispatchableCommandFlux} for the specified delegate.
     *
     * @param delegate The delegate to construct the {@code DispatchableCommandFlux} for.
     */
    public DispatchableEventFlux(Flux<CommandEvent<?>> delegate) {
        this.delegate = delegate;
    }

    /**
     * Upon subscription starts dispatching all command events in the discord4j {@link EventDispatcher}.
     *
     * @return A {@link Mono} which represent the redispatching process.
     */
    public Mono<Void> dispatch() {
        return delegate.doOnNext(event -> event.getClient().getEventDispatcher().publish(event)).then();
    }

    @Override
    public void subscribe(CoreSubscriber<? super CommandEvent<?>> actual) {
        delegate.subscribe(actual);
    }

}
