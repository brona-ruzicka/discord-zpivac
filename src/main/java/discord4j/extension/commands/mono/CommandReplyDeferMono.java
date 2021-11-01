package discord4j.extension.commands.mono;

import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.property.PreferEphemeralProperty;
import discord4j.extension.commands.util.DeferredMessage;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;


/**
 * <p>A {@link Mono} where, upon successful completion, emits a {@link DeferredMessage}.
 * If an error is received, it is emitted through it.</p>
 *
 * <p>Acknowledges the event indicating a response will be edited later. The user sees a loading state, visible
 * to all participants in the invoking channel. For an "only you can see this" response,
 * add {@code withEphemeral(true)}.</p>
 *
 * @see CommandEvent#deferReply
 */
public abstract class CommandReplyDeferMono extends Mono<DeferredMessage> implements
        PreferEphemeralProperty<CommandReplyDeferMono> {

    protected final Possible<Boolean> preferEphemeral;

    protected CommandReplyDeferMono(
            Possible<Boolean> preferEphemeral
    ) {
        this.preferEphemeral = preferEphemeral;
    }

    public CommandReplyDeferMono() {
        this(
                Possible.absent()
        );
    }

    protected abstract CommandReplyDeferMono withModified(
            Possible<Boolean> preferEphemeral
    );

    @Override
    public abstract void subscribe(CoreSubscriber<? super DeferredMessage> actual);

    @Override
    public Possible<Boolean> preferEphemeral() {
        return preferEphemeral;
    }

    @Override
    public CommandReplyDeferMono withPreferEphemeral(Possible<Boolean> preferEphemeral) {
        return withModified(
                preferEphemeral
        );
    }

}
