package discord4j.extension.commands.event;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.extension.commands.CommandEventMapper;
import discord4j.extension.commands.event.interaction.InteractionCommandEvent;
import discord4j.extension.commands.event.message.MessageCommandEvent;
import discord4j.extension.commands.mono.*;
import discord4j.extension.commands.property.PreferEphemeralProperty;
import discord4j.extension.commands.util.DeferredMessage;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static discord4j.extension.commands.event.interaction.InteractionCommandEventUtil.TOKEN_DELIMITER;


/**
 * <p>Unified interface for <a href="https://discord.com/developers/docs/interactions/application-commands">application commands</a>, emitted as {@link ApplicationCommandInteractionEvent}, and
 * original {@link Message message} commands, normal messages beginning with a prefix emitted by {@link MessageCreateEvent}.</p>
 *
 * <p>Contains very similar methods as {@link ApplicationCommandInteractionEvent} and allows to use features of <a href="https://discord.com/developers/docs/interactions/application-commands">application commands</a>.
 * It's implementations {@link InteractionCommandEvent} and {@link MessageCommandEvent} provide wrappers for {@link ApplicationCommandInteractionEvent} and {@link MessageCreateEvent} respectively.</p>
 *
 * <p>You are required to respond to this event within a three-second window by using one of the following:</p>
 * <ul>
 *     <li>{@link #createReply()} to directly include a message</li>
 *     <li>{@link #deferReply()} to acknowledge without a message, typically to perform a background task and give the
 *     user a loading state until it is edited</li>
 * </ul>
 * <p>After the initial response is complete, you can work with the interaction using the following methods:</p>
 * <ul>
 *     <li>{@link #editReply()} to edit the initial response</li>
 *     <li>{@link #deleteReply()} to delete the initial response</li>
 *     <li>{@link #getReply()} to fetch the initial response</li>
 * </ul>
 * <p>You can also work with followup messages using:</p>
 * <ul>
 *     <li>{@link #createFollowup()} to create a followup message</li>
 *     <li>{@link #editFollowup(Snowflake)} to update a followup message, given its ID</li>
 *     <li>{@link #deleteFollowup(Snowflake)} to delete a followup message, given its ID</li>
 *     <li>{@link #getFollowup(Snowflake)} to fetch a followup message, given its ID</li>
 * </ul>
 *
 * <p>{@link CommandEventMapper} produces this event.</p>
 *
 * @param <T> The event type this class provides wrapper for.
 * @see CommandEventMapper
 */
public abstract class CommandEvent<T extends Event> extends Event {

    /**
     * The event this class provides wrapper for.
     */
    protected final T delegate;

    /**
     * Constructs a {@code CommandEvent} with an associated delegate event.
     *
     * @param delegate The {@link T event} this class provides wrapper for, must be non-null.
     */
    public CommandEvent(T delegate) {
        super(delegate.getClient(), delegate.getShardInfo());
        this.delegate = delegate;
    }

    /**
     * Gets the {@link T event} that triggered this {@code CommandEvent}.
     *
     * @return The event delegate underneath this wrapper.
     */
    public T getDelegate() {
        return delegate;
    }

    /**
     * Gets id of the guild it was sent from, if invoked in a guild.
     *
     * @return The id of guild it was sent from, if invoked in a guild.
     */
    public abstract Optional<Snowflake> getGuildId();

    /**
     * Gets the guild it was sent from, if invoked in a guild.
     *
     * @return The guild it was sent from, if invoked in a guild.
     */
    public abstract Mono<Guild> getGuild();

    /**
     * Gets the id of the channel it was sent from.
     *
     * @return The id of the channel it was sent from.
     */
    public abstract Snowflake getChannelId();

    /**
     * Gets the channel it was sent from.
     *
     * @return The channel it was sent from.
     */
    public abstract Mono<MessageChannel> getChannel();

    /**
     * Gets the id of the invoking user.
     *
     * @return The id of the invoking user.
     */
    public abstract Snowflake getUserId();

    /**
     * Gets the invoking user.
     *
     * @return The invoking user.
     */
    public abstract User getUser();

    /**
     * Gets the invoking member, if invoked in a guild.
     *
     * @return The invoking member, if invoked in a guild.
     */
    public abstract Optional<Member> getMember();

    /**
     * Gets the type of the invoked command, always {@link ApplicationCommand.Type#CHAT_INPUT} for message commands.
     *
     * @return The type of the invoked command.
     */
    public abstract ApplicationCommand.Type getCommandType();

    /**
     * Gets whether or not this event was triggered by and interaction.
     * Note, that that is required for {@link PreferEphemeralProperty#withPreferEphemeral withPreferEphemeral} to work properly.
     *
     * @return Whether or not this event was triggered by and interaction
     */
    public abstract boolean isInteraction();

    /**
     * Gets the name of the invoked command.
     *
     * @return The name of the invoked command.
     */
    public abstract String getCommandName();

    /**
     * Gets the prefix of this {@code CommandEvent}, only present for {@link ApplicationCommand.Type#CHAT_INPUT text commands}, always {@code "/"} for application text commands.
     *
     * @return The prefix of this {@code CommandEvent}.
     */
    public abstract Optional<String> getPrefix();

    /**
     * Gets the arguments of this {@code CommandEvent}, only present for {@link ApplicationCommand.Type#CHAT_INPUT text commands}.
     * Note, that this method returns {@code Optional<String>}. All arguments are converted to a string to look as if it was a normal message.
     *
     * @return The arguments of this {@code CommandEvent}.
     */
    public abstract Optional<String> getArguments();

    /**
     * Joins {@link CommandEvent#getCommandName()} and {@link CommandEvent#getArguments()} to look as the original message, just without the prefix.
     * Only present for {@link ApplicationCommand.Type#CHAT_INPUT text commands}.
     *
     * @return The full command except for just the prefix.
     */
    public Optional<String> getFullString() {
        return getArguments().map(args -> getCommandName() + TOKEN_DELIMITER + args);
    }

    /**
     * Gets the ID of the targeted user.
     * Only present for {@link ApplicationCommand.Type#USER user-targeted application commands}.
     *
     * @return The ID of the targeted user.
     */
    public abstract Optional<Snowflake> getTargetUserId();

    /**
     * Gets the targeted user.
     * Only present for {@link ApplicationCommand.Type#USER user-targeted application commands}, else {@code Mono.empty()}.
     *
     * @return The targeted user.
     */
    public abstract Mono<User> getTargetUser();

    /**
     * Gets the targeted member, if invoked in a guild.
     * Only present for {@link ApplicationCommand.Type#USER user-targeted application commands}, else {@code Mono.empty()}.
     *
     * @return The targeted member, if invoked in a guild.
     */
    public abstract Mono<Member> getTargetMember();

    /**
     * Gets the ID of the targeted message.
     * Only present for {@link ApplicationCommand.Type#MESSAGE message-targeted application commands}.
     *
     * @return The ID of the targeted message.
     */
    public abstract Optional<Snowflake> getTargetMessageId();

    /**
     * Gets the targeted message.
     * Only present for {@link ApplicationCommand.Type#MESSAGE message-targeted application commands}, else {@code Mono.empty()}.
     *
     * @return The targeted message.
     */
    public abstract Mono<Message> getTargetMessage();

    /**
     * <p>Acknowledges the event indicating a response will be edited later. The user sees a loading state, visible
     * to all participants in the invoking channel. For an "only you can see this" response, add {@code withEphemeral(true)}.</p>
     * <p>
     * <pAfter calling {@code deferReply}, you are not allowed to call other acknowledging or reply method.</p>
     *
     * @return A {@link CommandReplyDeferMono} where, upon successful completion, emits a {@link DeferredMessage}.
     * If an error is received, it is emitted through it.
     */
    public abstract CommandReplyDeferMono deferReply();

    /**
     * <p>Requests to respond to the event with a message. Properties specifying how to build the reply message to
     * the event can be set via the {@code withXxx} methods of the returned {@link CommandReplyCreateMono}.</p>
     *
     * <p>After calling {@code createReply}, you are not allowed to call other acknowledging or reply method.</p>
     *
     * @return A {@link CommandReplyCreateMono} where, upon successful completion, emits the created message.
     * If an error is received, it is emitted through it.
     */
    public abstract CommandReplyCreateMono createReply();

    /**
     * <p>Requests to respond to the event with a message. Properties specifying how to build the reply message to
     * the event can be set via the {@code withXxx} methods of the returned {@link CommandReplyCreateMono}.</p>
     *
     * <p>After calling {@code createReply}, you are not allowed to call other acknowledging or reply method.</p>
     *
     * @param content A string to populate the message with.
     * @return A {@link CommandReplyCreateMono} where, upon successful completion, emits the created message.
     * If an error is received, it is emitted through it.
     */
    public CommandReplyCreateMono createReply(String content) {
        return createReply().withContent(content);
    }

    /**
     * <p>Edits the initial reply to this event. Properties specifying how to edit the reply message can be
     * set via the {@code withXxx} methods of the returned {@link CommandReplyEditMono}.</p>
     *
     * @return A {@link CommandReplyEditMono} where, upon successful completion, emits the updated message.
     * If an error is received, it is emitted through it.
     */
    public abstract CommandReplyEditMono editReply();

    /**
     * <p>Edits the initial reply to this event. Properties specifying how to edit the reply message can be
     * set via the {@code withXxx} methods of the returned {@link CommandReplyEditMono}.</p>
     *
     * @param content A string to update the message with.
     * @return A {@link CommandReplyEditMono} where, upon successful completion, emits the updated message.
     * If an error is received, it is emitted through it.
     */
    public CommandReplyEditMono editReply(String content) {
        return editReply().withContent(content);
    }

    /**
     * Deletes the initial reply to this event.
     *
     * @return A {@link Mono} where, upon successful initial reply deletion, emits nothing.
     * If an error is received, it is emitted through it.
     */
    public abstract Mono<Void> deleteReply();

    /**
     * Returns the initial reply to this event.
     *
     * @return A {@link Mono} where, upon successful completion, emits the initial reply message.
     * If an error is received, it is emitted through the it.
     */
    public abstract Mono<Message> getReply();

    /**
     * Creates a follow-up message to this event. Properties specifying how to build the follow-up message can be
     * set via the {@code withXxx} methods of the returned {@link CommandFollowupCreateMono}.
     *
     * @return A {@link CommandFollowupCreateMono} where, upon successful completion, emits the resulting follow-up
     * message. If an error is received, it is emitted through the it.
     */
    public abstract CommandFollowupCreateMono createFollowup();

    /**
     * Creates a follow-up message to this event. Properties specifying how to build the follow-up message can be
     * set via the {@code withXxx} methods of the returned {@link CommandFollowupCreateMono}.
     *
     * @param content A string to populate the followup message with.
     * @return A {@link CommandFollowupCreateMono} where, upon successful completion, emits the resulting follow-up
     * message. If an error is received, it is emitted through the it.
     */
    public CommandFollowupCreateMono createFollowup(String content) {
        return createFollowup().withContent(content);
    }

    /**
     * Edits a follow-up message to this event. Properties specifying how to edit the follow-up message can be
     * set via the {@code withXxx} methods of the returned {@link CommandFollowupEditMono}.
     *
     * @param followupId The follow-up message ID to edit.
     * @return A {@link CommandFollowupEditMono} where, upon successful completion, emits the updated follow-up
     * message. If an error is received, it is emitted through the it.
     */
    public abstract CommandFollowupEditMono editFollowup(Snowflake followupId);

    /**
     * Edits a follow-up message to this event. Properties specifying how to edit the follow-up message can be
     * set via the {@code withXxx} methods of the returned {@link CommandFollowupEditMono}.
     *
     * @param followupId The follow-up message ID to edit.
     * @param content    A string to update the message with.
     * @return A {@link CommandFollowupEditMono} where, upon successful completion, emits the updated follow-up
     * message. If an error is received, it is emitted through the it.
     */
    public CommandFollowupEditMono editFollowup(Snowflake followupId, String content) {
        return editFollowup(followupId).withContent(content);
    }

    /**
     * Deletes a follow-up message to this event.
     *
     * @param followupId The follow-up message ID to delete.
     * @return A {@link Mono} where, upon successful message deletion, emits nothing.
     * If an error is received, it is emitted through it.
     */
    public abstract Mono<Void> deleteFollowup(Snowflake followupId);

    /**
     * Returns a follow-up message to this event.
     *
     * @param followupId The follow-up message ID to return.
     * @return A {@link Mono} where, upon successful completion, emits the follow-up message.
     * If an error is received, it is emitted through the it.
     */
    public abstract Mono<Message> getFollowup(Snowflake followupId);

}
