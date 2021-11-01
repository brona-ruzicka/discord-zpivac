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
import java.util.Optional;


/**
 * <p>A {@link Mono} where, upon successful completion, emits the updated message.
 * If an error is received, it is emitted through it.</p>
 *
 * <p>Edits the initial reply. Properties specifying how to edit the reply message can be
 * set via the {@code withXxx} methods of the returned {@code CommandReplyEditMono}.</p>
 *
 * @see CommandEvent#editReply
 */
public abstract class CommandReplyEditMono extends Mono<Message> implements
        AllowedMentionsOptionalProperty<CommandReplyEditMono>,
        ComponentsOptionalProperty<CommandReplyEditMono>,
        ContentOptionalProperty<CommandReplyEditMono>,
        EmbedsOptionalProperty<CommandReplyEditMono>,
        FilesProperty<CommandReplyEditMono> {

    protected final Possible<Optional<AllowedMentions>> allowedMentions;
    protected final Possible<Optional<List<LayoutComponent>>> components;
    protected final Possible<Optional<String>> content;
    protected final Possible<Optional<List<EmbedCreateSpec>>> embeds;
    protected final List<MessageCreateFields.File> files;
    protected final List<MessageCreateFields.FileSpoiler> fileSpoilers;

    protected CommandReplyEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers
    ) {
        this.allowedMentions = allowedMentions;
        this.components = components;
        this.content = content;
        this.embeds = embeds;
        this.files = files;
        this.fileSpoilers = fileSpoilers;
    }

    protected CommandReplyEditMono() {
        this(
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    protected abstract CommandReplyEditMono withModified(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers
    );

    @Override
    public abstract void subscribe(CoreSubscriber<? super Message> actual);

    @Override
    public Possible<Optional<AllowedMentions>> allowedMentions() {
        return allowedMentions;
    }

    @Override
    public CommandReplyEditMono withAllowedMentions(Possible<Optional<AllowedMentions>> allowedMentions) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

    @Override
    public Possible<Optional<List<LayoutComponent>>> components() {
        return components;
    }

    @Override
    public CommandReplyEditMono withComponents(Possible<Optional<List<LayoutComponent>>> components) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

    @Override
    public Possible<Optional<String>> content() {
        return content;
    }

    @Override
    public CommandReplyEditMono withContent(Possible<Optional<String>> content) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

    @Override
    public Possible<Optional<List<EmbedCreateSpec>>> embeds() {
        return embeds;
    }

    @Override
    public CommandReplyEditMono withEmbeds(Possible<Optional<List<EmbedCreateSpec>>> embeds) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

    @Override
    public List<MessageCreateFields.File> files() {
        return files;
    }

    @Override
    public CommandReplyEditMono withFiles(List<MessageCreateFields.File> files) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

    @Override
    public List<MessageCreateFields.FileSpoiler> fileSpoilers() {
        return fileSpoilers;
    }

    @Override
    public CommandReplyEditMono withFileSpoilers(List<MessageCreateFields.FileSpoiler> fileSpoilers) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers
        );
    }

}
