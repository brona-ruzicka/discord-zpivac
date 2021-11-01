package discord4j.extension.commands.mono;

import discord4j.core.object.component.LayoutComponent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateFields;
import discord4j.discordjson.possible.Possible;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.property.*;
import discord4j.rest.util.AllowedMentions;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;


/**
 * <p>A {@link Mono} where, upon successful completion, emits the resulting follow-up
 * message. If an error is received, it is emitted through the it.</p>
 *
 * <p>Creates a follow-up message to this event. Properties specifying how to build the follow-up message can be
 * set via the {@code withXxx} methods of the returned {@code CommandFollowupCreateMono}.</p>
 *
 * @see CommandEvent#createFollowup
 */
public abstract class CommandFollowupCreateMono extends Mono<Message> implements
        AllowedMentionsProperty<CommandFollowupCreateMono>,
        ComponentsProperty<CommandFollowupCreateMono>,
        ContentProperty<CommandFollowupCreateMono>,
        EmbedsProperty<CommandFollowupCreateMono>,
        FilesProperty<CommandFollowupCreateMono>,
        PreferEphemeralProperty<CommandFollowupCreateMono>,
        TtsProperty<CommandFollowupCreateMono> {

    protected final Possible<AllowedMentions> allowedMentions;
    protected final Possible<List<LayoutComponent>> components;
    protected final Possible<String> content;
    protected final Possible<List<EmbedCreateSpec>> embeds;
    protected final List<MessageCreateFields.File> files;
    protected final List<MessageCreateFields.FileSpoiler> fileSpoilers;
    protected final Possible<Boolean> preferEphemeral;
    protected final Possible<Boolean> tts;

    protected CommandFollowupCreateMono(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Possible<Boolean> preferEphemeral,
            Possible<Boolean> tts
    ) {
        this.allowedMentions = allowedMentions;
        this.components = components;
        this.content = content;
        this.embeds = embeds;
        this.files = files;
        this.fileSpoilers = fileSpoilers;
        this.preferEphemeral = preferEphemeral;
        this.tts = tts;
    }

    protected CommandFollowupCreateMono() {
        this(
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Collections.emptyList(),
                Collections.emptyList(),
                Possible.absent(),
                Possible.absent()
        );
    }

    protected abstract CommandFollowupCreateMono withModified(
            Possible<AllowedMentions> allowedMentions,
            Possible<List<LayoutComponent>> components,
            Possible<String> content,
            Possible<List<EmbedCreateSpec>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
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
    public CommandFollowupCreateMono withAllowedMentions(Possible<AllowedMentions> allowedMentions) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<List<LayoutComponent>> components() {
        return components;
    }

    @Override
    public CommandFollowupCreateMono withComponents(Possible<List<LayoutComponent>> components) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<String> content() {
        return content;
    }

    @Override
    public CommandFollowupCreateMono withContent(Possible<String> content) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<List<EmbedCreateSpec>> embeds() {
        return embeds;
    }

    @Override
    public CommandFollowupCreateMono withEmbeds(Possible<List<EmbedCreateSpec>> embeds) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public List<MessageCreateFields.File> files() {
        return files;
    }

    @Override
    public CommandFollowupCreateMono withFiles(List<MessageCreateFields.File> files) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public List<MessageCreateFields.FileSpoiler> fileSpoilers() {
        return fileSpoilers;
    }

    @Override
    public CommandFollowupCreateMono withFileSpoilers(List<MessageCreateFields.FileSpoiler> fileSpoilers) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<Boolean> preferEphemeral() {
        return preferEphemeral;
    }

    @Override
    public CommandFollowupCreateMono withPreferEphemeral(Possible<Boolean> preferEphemeral) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

    @Override
    public Possible<Boolean> tts() {
        return tts;
    }

    @Override
    public CommandFollowupCreateMono withTts(Possible<Boolean> tts) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                preferEphemeral,
                tts
        );
    }

}
