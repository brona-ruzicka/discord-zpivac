package discord4j.extension.commands.mono.message;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.message.MessageCommandReplyData;
import discord4j.extension.commands.mono.CommandReplyEditMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/**
 * Implementation of {@link CommandReplyEditMono} for message commands.
 */
public class MessageCommandReplyEditMono extends CommandReplyEditMono {

    protected final MessageCommandReplyData replyData;

    protected MessageCommandReplyEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            MessageCommandReplyData replyData
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers);
        this.replyData = replyData;
    }

    public MessageCommandReplyEditMono(MessageCommandReplyData replyData) {
        super();
        this.replyData = replyData;
    }

    @Override
    protected CommandReplyEditMono withModified(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers
    ) {
        return new MessageCommandReplyEditMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, replyData
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> coreSubscriber) {
        replyData.getEvent().getReply()
                .flatMap(message -> message.edit()
                        .withAllowedMentions(allowedMentions())
                        .withComponents(components())
                        .withContent(content()
                                .toOptional()
                                .flatMap(Function.identity())
                                .isPresent() ? (
                                        content()
                                ) : (
                                        replyData
                                                .getShouldDeleteContent()
                                                .compareAndSet(true, false) ? (
                                                        Possible.of(Optional.of(""))
                                                ) : (
                                                        content()
                                                )
                                )
                        )
                        .withEmbeds(embeds())
                        .withFiles(files())
                        .withFileSpoilers(fileSpoilers())
                )
                .subscribe(coreSubscriber);
    }

}
