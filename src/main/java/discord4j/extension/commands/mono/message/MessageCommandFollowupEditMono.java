package discord4j.extension.commands.mono.message;

import discord4j.common.util.Snowflake;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.message.MessageCommandReplyData;
import discord4j.extension.commands.mono.CommandFollowupEditMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of {@link CommandFollowupEditMono} for message commands.
 */
public class MessageCommandFollowupEditMono extends CommandFollowupEditMono {

    protected final MessageCommandReplyData replyData;

    protected MessageCommandFollowupEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Snowflake followupId,
            MessageCommandReplyData replyData
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers, followupId);
        this.replyData = replyData;
    }

    public MessageCommandFollowupEditMono(Snowflake followupId, MessageCommandReplyData replyData) {
        super(followupId);
        this.replyData = replyData;
    }

    @Override
    protected CommandFollowupEditMono withModified(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Snowflake followupId
    ) {
        return new MessageCommandFollowupEditMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, followupId, replyData
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        replyData.getEvent().getClient()
                .getMessageById(
                        replyData.getEvent().getChannelId(),
                        followupId
                ).flatMap(message -> message.edit()
                .withAllowedMentions(allowedMentions())
                .withComponents(components())
                .withContent(content())
                .withEmbeds(embeds())
                .withFiles(files())
                .withFileSpoilers(fileSpoilers())
        ).subscribe(actual);
    }

}
