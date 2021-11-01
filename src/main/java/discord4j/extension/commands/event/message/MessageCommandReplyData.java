package discord4j.extension.commands.event.message;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Utility class for extra data of message command events.
 */
public class MessageCommandReplyData {

    protected final MessageCommandEvent event;
    protected final AtomicBoolean shouldDeleteContent = new AtomicBoolean(false);
    protected final AtomicBoolean replySent = new AtomicBoolean(false);
    protected Mono<Snowflake> replyId = Mono.empty();

    public MessageCommandReplyData(MessageCommandEvent event) {
        this.event = event;
    }

    public MessageCommandEvent getEvent() {
        return event;
    }

    public MessageCreateEvent getDelegate() {
        return event.getDelegate();
    }

    public AtomicBoolean getReplySent() {
        return replySent;
    }

    public Mono<Snowflake> getReplyId() {
        return Mono.defer(() -> replyId);
    }

    public void setReplyId(Mono<Snowflake> replyId) {
        this.replyId = replyId.retry(1).cache();
    }

    public AtomicBoolean getShouldDeleteContent() {
        return shouldDeleteContent;
    }
}
