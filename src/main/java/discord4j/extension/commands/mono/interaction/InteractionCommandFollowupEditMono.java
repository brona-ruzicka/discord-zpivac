package discord4j.extension.commands.mono.interaction;

import discord4j.common.util.Snowflake;
import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.mono.CommandFollowupEditMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of {@link CommandFollowupEditMono} for interaction commands.
 */
public class InteractionCommandFollowupEditMono extends CommandFollowupEditMono {

    protected final InteractionCommandEvent event;

    protected InteractionCommandFollowupEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Snowflake followupId,
            InteractionCommandEvent event
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers, followupId);
        this.event = event;
    }

    public InteractionCommandFollowupEditMono(Snowflake followupId, InteractionCommandEvent event) {
        super(followupId);
        this.event = event;
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
        return new InteractionCommandFollowupEditMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, followupId, event
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        event.getDelegate().editFollowup(followupId())
                .withAllowedMentions(allowedMentions())
                .withComponents(components())
                .withContent(content())
                .withEmbeds(embeds())
                .withFiles(files())
                .withFileSpoilers(fileSpoilers())
                .subscribe(actual);
    }

}
