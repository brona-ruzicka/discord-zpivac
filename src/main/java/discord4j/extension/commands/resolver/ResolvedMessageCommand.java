package discord4j.extension.commands.resolver;


/**
 * Container for the basic parts of a message command. Prefix, command name and arguments.
 * Should be returned by {@link MessageCommandResolver} when it resolves a message as a command.
 */
public class ResolvedMessageCommand {

    /**
     * The prefix of the command.
     */
    protected final String prefix;

    /**
     * The name of the command.
     */
    protected final String commandName;

    /**
     * The arguments of the command.
     */
    protected final String arguments;

    /**
     * Constructs a {@code ResolvedMessageCommand} with its fields set to the specified values.
     *
     * @param prefix      The prefix of this command.
     * @param commandName The name of this command.
     * @param arguments   The arguments of this command.
     */
    protected ResolvedMessageCommand(String prefix, String commandName, String arguments) {
        this.prefix = prefix;
        this.commandName = commandName;
        this.arguments = arguments;
    }

    /**
     * Returns an empty {@code ResolvedMessageCommand} with all fields set to empty strings.
     *
     * @return The constructed {@code ResolvedMessageCommand}.
     */
    public static ResolvedMessageCommand create() {
        return new ResolvedMessageCommand("", "", "");
    }

    /**
     * Returns a {@code ResolvedMessageCommand} with its fields set to the specified values.
     *
     * @param prefix      The prefix of this command.
     * @param commandName The name of this command.
     * @param arguments   The arguments of this command.
     * @return The constructed {@code ResolvedMessageCommand}
     */
    public static ResolvedMessageCommand create(String prefix, String commandName, String arguments) {
        return new ResolvedMessageCommand(prefix, commandName, arguments);
    }

    /**
     * Returns the prefix of this command.
     *
     * @return The prefix of this command.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the name of this command.
     *
     * @return The name of this command.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Returns the arguments of this command.
     *
     * @return The arguments of this command.
     */
    public String getArguments() {
        return arguments;
    }

    /**
     * Creates a new {@code ResolvedMessageCommand} with the updated prefix.
     *
     * @param prefix The new prefix.
     * @return New updated {@code ResolvedMessageCommand}.
     */
    public ResolvedMessageCommand withPrefix(String prefix) {
        return new ResolvedMessageCommand(prefix, commandName, arguments);
    }

    /**
     * Creates a new {@code ResolvedMessageCommand} with the updated command name.
     *
     * @param commandName The new command name.
     * @return New updated {@code ResolvedMessageCommand}.
     */
    public ResolvedMessageCommand withCommandName(String commandName) {
        return new ResolvedMessageCommand(prefix, commandName, arguments);
    }

    /**
     * Creates a new {@code ResolvedMessageCommand} with the updated arguments.
     *
     * @param arguments The new arguments.
     * @return New updated {@code ResolvedMessageCommand}.
     */
    public ResolvedMessageCommand withArguments(String arguments) {
        return new ResolvedMessageCommand(prefix, commandName, arguments);
    }

}
