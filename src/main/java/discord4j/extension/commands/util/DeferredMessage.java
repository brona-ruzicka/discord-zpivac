package discord4j.extension.commands.util;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.DiscordObject;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.mono.CommandReplyDeferMono;
import discord4j.extension.commands.mono.CommandReplyEditMono;
import reactor.core.publisher.Mono;


/**
 * Emitted by {@link CommandReplyDeferMono} when an {@link CommandEvent} is deferred.
 * <p>
 * Represents the loading message of an interaction.
 * <p>
 * Contains methods to easily {@link DeferredMessage#edit} or {@link DeferredMessage#delete} the initial interaction reply.
 */
public class DeferredMessage implements DiscordObject {

    /**
     * The event associated to this object
     */
    protected final CommandEvent<?> event;

    /**
     * Constructs a {@code DeferredMessage} with an associated event for data acquisition.
     *
     * @param event The {@link CommandEvent} associated to this object, must be non-null.
     */
    public DeferredMessage(CommandEvent<?> event) {
        this.event = event;
    }

    @Override
    public GatewayDiscordClient getClient() {
        return event.getClient();
    }

    /**
     * Requests to edit this reply. Properties specifying how to edit this message can be set via the {@code
     * withXxx} methods of the returned {@link CommandReplyEditMono}.
     *
     * @return A {@link CommandReplyEditMono} where, upon successful completion, emits the edited {@link Message}. If an
     * error is received, it is emitted through the {@code CommandReplyEditMono}.
     */
    public CommandReplyEditMono edit() {
        return event.editReply();
    }

    /**
     * Requests to edit the this reply. Properties specifying how to edit this message can be set via the {@code
     * withXxx} methods of the returned {@link CommandReplyEditMono}.
     *
     * @param content A string to update the message with.
     * @return A {@link CommandReplyEditMono} where, upon successful completion, emits the edited {@link Message}. If an
     * error is received, it is emitted through the {@code CommandReplyEditMono}.
     * @see DeferredMessage#edit()
     */
    public CommandReplyEditMono edit(String content) {
        return event.editReply(content);
    }

    /**
     * Deletes this message.
     *
     * @return a {@link Mono} where, upon successful initial reply deletion, emits nothing indicating completion. If an
     * error is received, it is emitted through the {@code Mono}.
     */
    public Mono<Void> delete() {
        return event.deleteReply();
    }

    /**
     * Gets the ID of the channel the message was sent in.
     *
     * @return The ID of the channel the message was sent in.
     */
    public Snowflake getChannelId() {
        return event.getChannelId();
    }

    /**
     * Requests to retrieve the channel the message was sent in.
     *
     * @return A {@link Mono} where, upon successful completion, emits the {@link MessageChannel channel} the message
     * was sent in. If an error is received, it is emitted through the {@code Mono}.
     */
    public Mono<MessageChannel> getChannel() {
        return event.getChannel();
    }

}
