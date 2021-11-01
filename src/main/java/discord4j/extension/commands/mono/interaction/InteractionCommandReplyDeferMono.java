package discord4j.extension.commands.mono.interaction;

import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.mono.CommandReplyDeferMono;
import discord4j.extension.commands.util.DeferredMessage;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;


/**
 * Implementation of {@link CommandReplyDeferMono} for interaction commands.
 */
public class InteractionCommandReplyDeferMono extends CommandReplyDeferMono {

    protected final InteractionCommandEvent event;

    protected InteractionCommandReplyDeferMono(
            Possible<Boolean> preferEphemeral,
            InteractionCommandEvent event
    ) {
        super(preferEphemeral);
        this.event = event;
    }

    public InteractionCommandReplyDeferMono(InteractionCommandEvent event) {
        super();
        this.event = event;
    }

    @Override
    protected CommandReplyDeferMono withModified(
            Possible<Boolean> preferEphemeral
    ) {
        return new InteractionCommandReplyDeferMono(
                preferEphemeral, event
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super DeferredMessage> actual) {
        event.getDelegate().deferReply()
                .withEphemeral(preferEphemeral())
                .then(Mono.just(new DeferredMessage(event)))
                .subscribe(actual);
    }
}
