package discord4j.extension.commands.event.interaction;

import discord4j.common.util.Snowflake;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandOption;

import java.util.List;


/**
 * Utility class for extra functions to handle interaction command events.
 */
public final class InteractionCommandEventUtil {

    public static final String TOKEN_DELIMITER = " ";

    private InteractionCommandEventUtil() {
    }

    // TODO: Test this properly
    public static String extractArguments(ApplicationCommandInteraction interaction) {
        StringBuilder builder = new StringBuilder();
        appendOptions(interaction, builder, interaction.getOptions());

        if (builder.length() > 0)
            builder.delete(builder.length() - TOKEN_DELIMITER.length(), builder.length());

        return builder.toString();
    }

    protected static void appendOptions(
            ApplicationCommandInteraction interaction,
            StringBuilder builder,
            List<ApplicationCommandInteractionOption> options
    ) {
        options.forEach(option -> appendOption(interaction, builder, option));
    }

    protected static void appendOption(
            ApplicationCommandInteraction interaction,
            StringBuilder builder,
            ApplicationCommandInteractionOption option
    ) {

        ApplicationCommandOption.Type optionType = option.getType();

        switch (optionType) {

            case SUB_COMMAND:
            case SUB_COMMAND_GROUP:
                builder.append(option.getName()).append(TOKEN_DELIMITER);
                appendOptions(interaction, builder, option.getOptions());
                break;

            case STRING:
            case INTEGER:
            case BOOLEAN:
            case USER:
            case CHANNEL:
            case ROLE:
            case MENTIONABLE:
            case NUMBER:

                option.getValue().ifPresent(value -> {

                    switch (optionType) {

                        case STRING:
                            builder.append(value.asString());
                            break;

                        case INTEGER:
                            builder.append(value.asLong());
                            break;

                        case BOOLEAN:
                            builder.append(value.asBoolean());
                            break;

                        case USER:
                            builder.append("<@")
                                    .append(value.asSnowflake().asString())
                                    .append(">");
                            break;

                        case CHANNEL:
                            builder.append("<#")
                                    .append(value.asSnowflake().asString())
                                    .append(">");
                            break;

                        case ROLE:
                            builder.append("<@&")
                                    .append(value.asSnowflake().asString())
                                    .append(">");
                            break;

                        case MENTIONABLE:
                            interaction.getResolved().ifPresent(resolved -> {
                                Snowflake id = value.asSnowflake();

                                boolean isUser = resolved.getUser(id).isPresent();
                                boolean isRole = resolved.getRole(id).isPresent();

                                if (isUser == isRole)
                                    throw new IllegalStateException("Unknown Mentionable");

                                builder.append(isUser ? "<@" : "<@&")
                                        .append(id.asString())
                                        .append(">");

                            });
                            break;

                        case NUMBER:
                            builder.append(value.asDouble());
                            break;
                    }
                });

                builder.append(TOKEN_DELIMITER);
                break;

            default:
                throw new IllegalStateException("Unknown ApplicationCommandOption.Type");
        }

    }

}
