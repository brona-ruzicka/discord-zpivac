package discord4j.extension.commands.mono;

import discord4j.common.util.Snowflake;
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
 * <p>A {@link Mono} where, upon successful completion, emits the updated follow-up
 * message. If an error is received, it is emitted through the it.</p>
 *
 * <p>Edits a follow-up message to this event. Properties specifying how to edit the follow-up message can be
 * set via the {@code withXxx} methods of the returned {@code CommandFollowupEditMono}.</p>
 *
 * @see CommandEvent#editFollowup
 */
public abstract class CommandFollowupEditMono extends Mono<Message> implements
        AllowedMentionsOptionalProperty<CommandFollowupEditMono>,
        ComponentsOptionalProperty<CommandFollowupEditMono>,
        ContentOptionalProperty<CommandFollowupEditMono>,
        EmbedsOptionalProperty<CommandFollowupEditMono>,
        FilesProperty<CommandFollowupEditMono>,
        FollowupIdProperty<CommandFollowupEditMono> {

    protected final Possible<Optional<AllowedMentions>> allowedMentions;
    protected final Possible<Optional<List<LayoutComponent>>> components;
    protected final Possible<Optional<String>> content;
    protected final Possible<Optional<List<EmbedCreateSpec>>> embeds;
    protected final List<MessageCreateFields.File> files;
    protected final List<MessageCreateFields.FileSpoiler> fileSpoilers;
    protected final Snowflake followupId;

    protected CommandFollowupEditMono(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Snowflake followupId
    ) {
        this.allowedMentions = allowedMentions;
        this.components = components;
        this.content = content;
        this.embeds = embeds;
        this.files = files;
        this.fileSpoilers = fileSpoilers;
        this.followupId = followupId;
    }

    protected CommandFollowupEditMono(Snowflake followupId) {
        this(
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Possible.absent(),
                Collections.emptyList(),
                Collections.emptyList(),
                followupId
        );
    }

    protected abstract CommandFollowupEditMono withModified(
            Possible<Optional<AllowedMentions>> allowedMentions,
            Possible<Optional<List<LayoutComponent>>> components,
            Possible<Optional<String>> content,
            Possible<Optional<List<EmbedCreateSpec>>> embeds,
            List<MessageCreateFields.File> files,
            List<MessageCreateFields.FileSpoiler> fileSpoilers,
            Snowflake followupId
    );

    @Override
    public abstract void subscribe(CoreSubscriber<? super Message> actual);


    @Override
    public Possible<Optional<AllowedMentions>> allowedMentions() {
        return allowedMentions;
    }

    @Override
    public CommandFollowupEditMono withAllowedMentions(Possible<Optional<AllowedMentions>> allowedMentions) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public Possible<Optional<List<LayoutComponent>>> components() {
        return components;
    }

    @Override
    public CommandFollowupEditMono withComponents(Possible<Optional<List<LayoutComponent>>> components) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public Possible<Optional<String>> content() {
        return content;
    }

    @Override
    public CommandFollowupEditMono withContent(Possible<Optional<String>> content) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public Possible<Optional<List<EmbedCreateSpec>>> embeds() {
        return embeds;
    }

    @Override
    public CommandFollowupEditMono withEmbeds(Possible<Optional<List<EmbedCreateSpec>>> embeds) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public List<MessageCreateFields.File> files() {
        return files;
    }

    @Override
    public CommandFollowupEditMono withFiles(List<MessageCreateFields.File> files) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public List<MessageCreateFields.FileSpoiler> fileSpoilers() {
        return fileSpoilers;
    }

    @Override
    public CommandFollowupEditMono withFileSpoilers(List<MessageCreateFields.FileSpoiler> fileSpoilers) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                followupId
        );
    }

    @Override
    public Snowflake followupId() {
        return followupId;
    }

    @Override
    public CommandFollowupEditMono withFollowupId(Snowflake followupId) {
        return withModified(
                allowedMentions,
                components,
                content,
                embeds,
                files,
                fileSpoilers,
                this.followupId
        );
    }

}
