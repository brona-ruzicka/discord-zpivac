package discord4j.extension.commands.mono;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.property.*;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.List;


/**
 * <p>A {@link Mono} where, upon successful completion, emits a created reply message.
 * If an error is received, it is emitted through it.</p>
 *
 * <p>Properties specifying how to build the reply message to the event can be set
 * via the {@code withXxx} methods of this {@code CommandReplyCreateMono}.</p>
 *
 * @see CommandEvent#createReply
 */
public abstract class CommandReplyCreateMono extends Mono<Message> implements
        AllowedMentionsProperty<CommandReplyCreateMono>,
        ComponentsProperty<CommandReplyCreateMono>,
        ContentProperty<CommandReplyCreateMono>,
        EmbedsProperty<CommandReplyCreateMono>,
        PreferEphemeralProperty<CommandReplyCreateMono>,
        TtsProperty<CommandReplyCreateMono> {

    protected final Possible<AllowedMentions> allowedMentions;
    protected final Possible<List<LayoutComponent>> components;
    protected final Possible<String> content;
    protected final Possible<List<EmbedCreateSpec>> embeds;
    protected final Possible<Boolean> preferEphemeral;
    protected final Possible<Boolean> tts;

    protected CommandReplyCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts
    ) {
        this.allowedMentions = allowedMentions;
        this.components = components;
        this.content = content;
        this.embeds = embeds;
        this.preferEphemeral = preferEphemeral;
        this.tts = tts;
    }

    protected CommandReplyCreateMono() {
        this(
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent()
        );
    }

    protected abstract CommandReplyCreateMono withModified(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts
    );

    @Override
    public abstract void subscribe(CoreSubscriber<? super Message> actual);

    @Override
    public Possible<AllowedMentions> allowedMentions() {
        return allowedMentions;
    }

    @Override
    public CommandReplyCreateMono withAllowedMentions(Possible<AllowedMentions> allowedMentions) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<List<LayoutComponent>> components() {
        return components;
    }

    @Override
    public CommandReplyCreateMono withComponents(Possible<List<LayoutComponent>> components) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<String> content() {
        return content;
    }

    @Override
    public CommandReplyCreateMono withContent(Possible<String> content) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<List<EmbedCreateSpec>> embeds() {
        return embeds;
    }

    @Override
    public CommandReplyCreateMono withEmbeds(Possible<List<EmbedCreateSpec>> embeds) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<Boolean> preferEphemeral() {
        return preferEphemeral;
    }

    @Override
    public CommandReplyCreateMono withPreferEphemeral(Possible<Boolean> preferEphemeral) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<Boolean> tts() {
        return tts;
    }

    @Override
    public CommandReplyCreateMono withTts(Possible<Boolean> tts) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                preferEphemeral,
                tts
        );
    }

}
