package discord4j.extension.commands.mono.interaction;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.mono.CommandReplyEditMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of {@link CommandReplyEditMono} for interaction commands.
 */
public class InteractionCommandReplyEditMono extends CommandReplyEditMono {

    protected final InteractionCommandEvent event;

    protected InteractionCommandReplyEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            InteractionCommandEvent event
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers);
        this.event = event;
    }

    public InteractionCommandReplyEditMono(InteractionCommandEvent event) {
        super();
        this.event = event;
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
        return new InteractionCommandReplyEditMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, event
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> coreSubscriber) {
        event.getDelegate().editReply()
                .withAllowedMentions(allowedMentions())
                .withComponents(components())
                .withContent(content())
                .withEmbeds(embeds())
                .withFiles(files())
                .withFileSpoilers(fileSpoilers())
                .subscribe(coreSubscriber);
    }

}
