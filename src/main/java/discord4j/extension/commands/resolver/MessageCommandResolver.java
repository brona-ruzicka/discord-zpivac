package discord4j.extension.commands.resolver;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.util.annotation.Nullable;


/**
 * This interface represents an object which resolves the basic parts of a message command, and also
 * whether or not the supplied event even represents a command.
 */
@FunctionalInterface
public interface MessageCommandResolver {

    /**
     * Basic implementation of a {@code MessageCommandResolver}, which resolves all messages as non-commands.
     */
    MessageCommandResolver NOOP = event -> null;

    /**
     * This method determines, if the supplied event is a message command. If it is, it returns
     * {@link ResolvedMessageCommand} filled with details of the command. If it is not a command the method
     * returns {@code null}.
     *
     * @param event The event to be resolved.
     * @return A {@link ResolvedMessageCommand} if the event represents a command, else {@code null}.
     */
    @Nullable
    ResolvedMessageCommand resolve(MessageCreateEvent event);

    /**
     * <p>Creates a basic {@code MessageCommandResolver} which loop trough the resolvers until one of them resolves
     * the event as a command, if none does, it returns {@code null}.</p>
     *
     * <p>NOTE: Users are encouraged to create their own {@code MessageCommandResolver}. The scope of the
     * included resolvers is severely limited.</p>
     *
     * @param resolvers The resolvers to merge in this resolvers.
     * @return One merged resolver.
     */
    static MessageCommandResolver of(MessageCommandResolver... resolvers) {
        return event -> {
            for (MessageCommandResolver resolver : resolvers) {
                ResolvedMessageCommand resolved = resolver.resolve(event);

                if (resolved != null)
                    return resolved;
            }

            return null;
        };
    }

    /**
     * <p>Creates a basic {@code MessageCommandResolver} which resolves the message as a command if it starts with the
     * given prefix, else returns {@code null}.</p>
     *
     * <p>NOTE: Users are encouraged to create their own {@code MessageCommandResolver}. The scope of the
     * included resolvers is severely limited.</p>
     *
     * @param prefix The prefix of all commands.
     * @return The created resolver.
     */
    static MessageCommandResolver prefixed(String prefix) {
        return event -> {

            String content = event.getMessage().getContent();

            if (!content.startsWith(prefix))
                return null;

            String[] split = content.substring(prefix.length()).split("\\s+", 2);

            return ResolvedMessageCommand.create()
                    .withPrefix(prefix)
                    .withCommandName(split[0].trim())
                    .withArguments(split.length > 1 ? split[1].trim() : "");
        };
    }

}
