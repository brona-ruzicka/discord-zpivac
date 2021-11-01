package discord4j.extension.commands.mono.message;

import discord4j.core.object.entity.Message;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.message.MessageCommandReplyData;
import discord4j.extension.commands.mono.CommandReplyDeferMono;
import discord4j.extension.commands.util.DeferredMessage;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;


/**
 * Implementation of {@link CommandReplyDeferMono} for message commands.
 */
public class MessageCommandReplyDeferMono extends CommandReplyDeferMono {

    protected final MessageCommandReplyData replyData;

    protected MessageCommandReplyDeferMono(
            Possible<Boolean> preferEphemeral,
            MessageCommandReplyData replyData
    ) {
        super(preferEphemeral);
        this.replyData = replyData;
    }

    public MessageCommandReplyDeferMono(MessageCommandReplyData replyData) {
        super();
        this.replyData = replyData;
    }

    @Override
    protected CommandReplyDeferMono withModified(
            Possible<Boolean> preferEphemeral
    ) {
        return new MessageCommandReplyDeferMono(
                preferEphemeral, replyData
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super DeferredMessage> actual) {
        Mono.defer(() -> {
            if (replyData.getReplySent().compareAndSet(false, true)) {
                Mono<Message> messageMono = replyData.getEvent().getChannel()
                        .flatMap(channel -> channel

                                // Checking that the message still exists
                                .getMessageById(replyData.getDelegate().getMessage().getId())
                                .map(message -> Possible.of(message.getId()))
                                .onErrorReturn(Possible.absent())

                                .flatMap(referenceId -> channel.createMessage() // TODO customisable
                                        .withContent("<a:loading:895806862195298314> <@" + replyData.getEvent().getClient().getSelfId().asString() + "> is thinking …")
                                        .withMessageReference(replyData.getDelegate().getMessage().getId())
                                )
                        )
                        .retry(1)
                        .cache();

                replyData.setReplyId(messageMono.map(Message::getId));
                replyData.getShouldDeleteContent().set(true);
                return messageMono.thenReturn(new DeferredMessage(replyData.getEvent()));
            } else {
                return Mono.error(new IllegalStateException(
                        "Initial reply already sent!"
                ));
            }
        })
                .subscribe(actual);
    }
}
