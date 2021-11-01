package discord4j.extension.commands.event.interaction;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.ApplicationCommand;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.extension.commands.event.CommandEvent;
import discord4j.extension.commands.mono.*;
import discord4j.extension.commands.mono.interaction.*;
import reactor.core.publisher.Mono;

import java.util.Optional;


/**
 * Implementation of {@link CommandEvent} for interaction commands.
 */
public class InteractionCommandEvent extends CommandEvent<ApplicationCommandInteractionEvent> {

    public static final String STRING_SLASH = "/";

    protected final String arguments;

    public InteractionCommandEvent(ApplicationCommandInteractionEvent delegate) {
        super(delegate);

        if (getCommandType() != ApplicationCommand.Type.CHAT_INPUT) {
            arguments = null;
        } else {
            arguments = delegate.getInteraction()
                    .getCommandInteraction()
                    .map(InteractionCommandEventUtil::extractArguments)
                    .orElse(null);
        }
    }

    @Override
    public Optional<Snowflake> getGuildId() {
        return getDelegate().getInteraction().getGuildId();
    }

    @Override
    public Mono<Guild> getGuild() {
        return getDelegate().getInteraction().getGuild();
    }

    @Override
    public Snowflake getChannelId() {
        return getDelegate().getInteraction().getChannelId();
    }

    @Override
    public Mono<MessageChannel> getChannel() {
        return getDelegate().getInteraction().getChannel();
    }

    @Override
    public Snowflake getUserId() {
        return getDelegate().getInteraction().getUser().getId();
    }

    @Override
    public User getUser() {
        return getDelegate().getInteraction().getUser();
    }

    @Override
    public Optional<Member> getMember() {
        return getDelegate().getInteraction().getMember();
    }

    @Override
    public ApplicationCommand.Type getCommandType() {
        return getDelegate().getCommandType();
    }

    @Override
    public boolean isInteraction() {
        return true;
    }

    @Override
    public String getCommandName() {
        return getDelegate().getCommandName();
    }

    @Override
    public Optional<String> getArguments() {
        return Optional.ofNullable(arguments);
    }

    @Override
    public Optional<String> getPrefix() {
        if (getCommandType() != ApplicationCommand.Type.CHAT_INPUT)
            return Optional.empty();

        return Optional.of(STRING_SLASH);
    }

    @Override
    public Optional<Snowflake> getTargetUserId() {
        if (getCommandType() != ApplicationCommand.Type.USER)
            return Optional.empty();

        return getDelegate().getInteraction()
                .getCommandInteraction()
                .flatMap(ApplicationCommandInteraction::getTargetId);
    }

    @Override
    public Mono<User> getTargetUser() {
        if (getCommandType() != ApplicationCommand.Type.USER)
            return Mono.empty();

        return Mono.justOrEmpty(
                getDelegate().getInteraction()
                        .getCommandInteraction()
                        .flatMap(ApplicationCommandInteraction::getResolved)
                        .flatMap(resolved -> getTargetUserId().flatMap(resolved::getUser))
        ).switchIfEmpty(
                Mono.justOrEmpty(getTargetUserId())
                        .flatMap(getClient()::getUserById)
        );
    }

    @Override
    public Mono<Member> getTargetMember() {
        if (getCommandType() != ApplicationCommand.Type.USER)
            return Mono.empty();

        return Mono.justOrEmpty(getGuildId())
                .flatMap(guildId -> Mono.justOrEmpty(getTargetUserId())
                        .flatMap(userId -> getClient().getMemberById(guildId, userId))
                );
    }

    @Override
    public Optional<Snowflake> getTargetMessageId() {
        if (getCommandType() != ApplicationCommand.Type.MESSAGE)
            return Optional.empty();

        return getDelegate().getInteraction()
                .getCommandInteraction()
                .flatMap(ApplicationCommandInteraction::getTargetId);
    }

    @Override
    public Mono<Message> getTargetMessage() {
        if (getCommandType() != ApplicationCommand.Type.MESSAGE)
            return Mono.empty();

        return Mono.justOrEmpty(getTargetMessageId())
                .flatMap(messageId -> getClient().getMessageById(getChannelId(), messageId));

    }

    @Override
    public CommandReplyDeferMono deferReply() {
        return new InteractionCommandReplyDeferMono(this);
    }

    @Override
    public CommandReplyCreateMono createReply() {
        return new InteractionCommandReplyCreateMono(this);
    }

    @Override
    public CommandReplyEditMono editReply() {
        return new InteractionCommandReplyEditMono(this);
    }

    @Override
    public Mono<Void> deleteReply() {
        return getDelegate().deleteReply();
    }

    @Override
    public Mono<Message> getReply() {
        return getDelegate().getReply();
    }

    @Override
    public CommandFollowupCreateMono createFollowup() {
        return new InteractionCommandFollowupCreateMono(this);
    }

    @Override
    public CommandFollowupEditMono editFollowup(Snowflake followupId) {
        return new InteractionCommandFollowupEditMono(followupId, this);
    }

    @Override
    public Mono<Void> deleteFollowup(Snowflake followupId) {
        return getDelegate().deleteFollowup(followupId);
    }

    @Override
    public Mono<Message> getFollowup(Snowflake followupId) {
        return getChannel().flatMap(channel -> channel.getMessageById(followupId));
    }

}
