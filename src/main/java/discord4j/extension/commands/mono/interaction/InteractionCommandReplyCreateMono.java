package discord4j.extension.commands.mono.interaction;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.mono.CommandReplyCreateMono;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;

import java.util.List;


/**
 * Implementation of {@link CommandReplyCreateMono} for interaction commands.
 */
public class InteractionCommandReplyCreateMono extends CommandReplyCreateMono {

    protected final InteractionCommandEvent event;

    protected InteractionCommandReplyCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts,
            InteractionCommandEvent event
    ) {
        super(allowedMentions, components, content, embeds, preferEphemeral, tts);
        this.event = event;
    }

    public InteractionCommandReplyCreateMono(InteractionCommandEvent event) {
        super();
        this.event = event;
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
        return new InteractionCommandReplyCreateMono(
                allowedMentions, components, content, embeds, preferEphemeral, tts, event
        );
    }

    @Override
    public void subscribe(CoreSubscriber<? super Message> actual) {
        event.getDelegate().reply()
                .withAllowedMentions(allowedMentions())
                .withComponents(components())
                .withContent(content())
                .withEmbeds(embeds())
                .withEphemeral(preferEphemeral())
                .withTts(tts().toOptional().orElse(false))
                .then(event.getReply())
                .subscribe(actual);
    }

}
