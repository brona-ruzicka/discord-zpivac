package discord4j.extension.commands.mono.message;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.message.MessageCommandReplyData;
import discord4j.extension.commands.mono.CommandReplyCreateMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Implementation of {@link CommandReplyCreateMono} for message commands.
 */
public class MessageCommandReplyCreateMono extends CommandReplyCreateMono {

    protected final MessageCommandReplyData replyData;

    protected MessageCommandReplyCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts,
            MessageCommandReplyData replyData
    ) {
        super(allowedMentions, components, content, embeds, preferEphemeral, tts);
        this.replyData = replyData;
    }

    public MessageCommandReplyCreateMono(MessageCommandReplyData replyData) {
        super();
        this.replyData = replyData;
    }

    @Override
    protected CommandReplyCreateMono withModified(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts
    ) {
        return new MessageCommandReplyCreateMono(
                allowedMentions, components, content, embeds, preferEphemeral, tts, replyData
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        Mono.defer(() -> {
            if (replyData.getReplySent().compareAndSet(false, true)) {
                Mono<Message> messageMono = replyData.getEvent().getChannel()
                        .flatMap(channel -> channel

                                // Checking that the message still exists
                                .getMessageById(replyData.getDelegate().getMessage().getId())
                                .map(message -> Possible.of(message.getId()))
                                .onErrorReturn(Possible.absent())

                                .flatMap(referenceId -> channel.createMessage()
                                        .withAllowedMentions(allowedMentions())
                                        .withComponents(components())
                                        .withContent(content())
                                        .withEmbeds(embeds())
                                        .withTts(tts())
                                        .withMessageReference(referenceId)
                                )
                        )
                        .retry(1)
                        .cache();

                replyData.setReplyId(messageMono.map(Message::getId));
                return messageMono;
            } else {
                return Mono.error(new IllegalStateException(
                        "Initial reply already sent!"
                ));
            }
        }).subscribe(actual);
    }

}
