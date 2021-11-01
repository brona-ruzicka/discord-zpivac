package discord4j.extension.commands.mono.interaction;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.mono.CommandFollowupCreateMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.Collections;
import java.util.List;


/**
 * Implementation of {@link CommandFollowupCreateMono} for interaction commands.
 */
public class InteractionCommandFollowupCreateMono extends CommandFollowupCreateMono {

    protected final InteractionCommandEvent event;

    protected InteractionCommandFollowupCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts,
            InteractionCommandEvent event
    ) {
        super(allowedMentions, components, content, embeds, files, fileSpoilers, preferEphemeral, tts);
        this.event = event;
    }

    public InteractionCommandFollowupCreateMono(InteractionCommandEvent event) {
        super();
        this.event = event;
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
        return new InteractionCommandFollowupCreateMono(
                allowedMentions, components, content, embeds, files, fileSpoilers, preferEphemeral, tts, event
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        event.getDelegate().createFollowup()
                .withAllowedMentions(allowedMentions())
                .withComponents(components())
                .withContent(content())
                .withEmbeds(embeds().toOptional().orElse(Collections.emptyList()))
                .withFiles(files())
                .withFileSpoilers(fileSpoilers())
                .withEphemeral(preferEphemeral())
                .withTts(tts().toOptional().orElse(false))
                .subscribe(actual);
    }

}
