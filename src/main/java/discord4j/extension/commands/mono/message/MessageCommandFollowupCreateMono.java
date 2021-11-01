package discord4j.extension.commands.mono.message;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.message.MessageCommandReplyData;
import discord4j.extension.commands.mono.CommandFollowupCreateMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * Implementation of {@link CommandFollowupCreateMono} for message commands.
 */
public class MessageCommandFollowupCreateMono extends CommandFollowupCreateMono {

    protected final MessageCommandReplyData replyData;

    protected MessageCommandFollowupCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts,
            MessageCommandReplyData replyData
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers, preferEphemeral, tts);
        this.replyData = replyData;
    }

    public MessageCommandFollowupCreateMono(MessageCommandReplyData replyData) {
        super();
        this.replyData = replyData;
    }

    @Override
    protected CommandFollowupCreateMono withModified(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts
    ) {
        return new MessageCommandFollowupCreateMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, preferEphemeral, tts, replyData
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        replyData.getEvent().getChannel()
                .filter(channel -> replyData.getReplySent().get())
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(
                                "Initial reply not sent yet!"
                        ))
                )
                .flatMap(channel -> replyData.getReplyId()
                        .map(Possible::of)
                        .defaultIfEmpty(Possible.absent())
                        .flatMap(referenceId -> channel.createMessage()
                                .withAllowedMentions(allowedMentions())
                                .withComponents(components())
                                .withContent(content())
                                .withEmbeds(embeds())
                                .withFiles(files())
                                .withFileSpoilers(fileSpoilers())
                                .withMessageReference(referenceId)
                                .withTts(tts())
                        )
                ).subscribe(actual);
    }

}
