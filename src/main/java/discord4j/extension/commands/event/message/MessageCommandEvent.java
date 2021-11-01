package discord4j.extension.commands.event.message;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.mono.*;
import discord4j.extension.commands.mono.message.*;
import discord4j.extension.commands.resolver.ResolvedMessageCommand;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;


/**
 * Implementation of {@link CommandEvent} for message commands.
 */
public class MessageCommandEvent extends CommandEvent<MessageCreateEvent> {

    protected final MessageCommandReplyData replyData;
    protected final ResolvedMessageCommand resolvedCommand;

    public MessageCommandEvent(MessageCreateEvent delegate, ResolvedMessageCommand resolvedCommand) {
        super(delegate);

        this.replyData = new MessageCommandReplyData(this);
        this.resolvedCommand = resolvedCommand;
    }

    @Override
    public Optional<Snowflake> getGuildId() {
        return getDelegate().getGuildId();
    }

    @Override
    public Mono<Guild> getGuild() {
        return getDelegate().getGuild();
    }

    @Override
    public Snowflake getChannelId() {
        return getDelegate().getMessage().getChannelId();
    }

    @Override
    public Mono<MessageChannel> getChannel() {
        return getDelegate().getMessage().getChannel();
    }

    @Override
    public Snowflake getUserId() {
        return getDelegate().getMessage().getAuthor().orElseThrow(NoSuchElementException::new).getId();
    }

    @Override
    public User getUser() {
        return getDelegate().getMessage().getAuthor().orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Optional<Member> getMember() {
        return getDelegate().getMember();
    }

    @Override
    public ApplicationCommand.Type getCommandType() {
        return ApplicationCommand.Type.CHAT_INPUT;
    }

    @Override
    public boolean isInteraction() {
        return false;
    }

    @Override
    public String getCommandName() {
        return resolvedCommand.getCommandName();
    }

    @Override
    public Optional<String> getArguments() {
        return Optional.of(resolvedCommand.getArguments());
    }

    @Override
    public Optional<String> getPrefix() {
        return Optional.of(resolvedCommand.getPrefix());
    }

    @Override
    public Optional<Snowflake> getTargetUserId() {
        return Optional.empty();
    }

    @Override
    public Mono<User> getTargetUser() {
        return Mono.empty();
    }

    @Override
    public Mono<Member> getTargetMember() {
        return Mono.empty();
    }

    @Override
    public Optional<Snowflake> getTargetMessageId() {
        return Optional.empty();
    }

    @Override
    public Mono<Message> getTargetMessage() {
        return Mono.empty();
    }

    @Override
    public CommandReplyDeferMono deferReply() {
        return new MessageCommandReplyDeferMono(replyData);
    }

    @Override
    public CommandReplyCreateMono createReply() {
        return new MessageCommandReplyCreateMono(replyData);
    }

    @Override
    public CommandReplyEditMono editReply() {
        return new MessageCommandReplyEditMono(replyData);
    }

    @Override
    public Mono<Void> deleteReply() {
        return getReply().flatMap(reply -> {
            replyData.setReplyId(Mono.empty());
            return Mono.when(
                    reply.delete(),
                    getDelegate().getMessage().delete().onErrorResume(err -> Mono.empty())
            );
        });
    }

    @Override
    public Mono<Message> getReply() {
        return replyData.getReplyId()
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(
                                "Initial reply not present! (either not sent yet or already deleted)"
                        ))
                )
                .flatMap(replyId -> getClient()
                        .getMessageById(getDelegate().getMessage().getChannelId(), replyId)
                );
    }

    @Override
    public CommandFollowupCreateMono createFollowup() {
        return new MessageCommandFollowupCreateMono(replyData);
    }

    @Override
    public CommandFollowupEditMono editFollowup(Snowflake followupId) {
        return new MessageCommandFollowupEditMono(followupId, replyData);
    }

    @Override
    public Mono<Void> deleteFollowup(Snowflake followupId) {
        return getClient().getMessageById(getDelegate().getMessage().getChannelId(), followupId)
                .flatMap(Message::delete);
    }

    @Override
    public Mono<Message> getFollowup(Snowflake followupId) {
        return getClient().getMessageById(getDelegate().getMessage().getChannelId(), followupId);
    }

}
