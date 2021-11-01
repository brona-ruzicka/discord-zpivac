package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;
import discord4j.rest.util.AllowedMentions;


/**
 * Interface for builders supporting allowedMentions property.
 *
 * @param <T> The builder type.
 */
public interface AllowedMentionsProperty<T> {

    /**
     * Returns the current allowed mentions, if set.
     *
     * @return Allowed mentions, if set.
     */
    Possible<AllowedMentions> allowedMentions();

    /**
     * Creates a new builder with updated allowed mentions property.
     *
     * @param allowedMentions The allowed mentions for the new builder.
     * @return New updated builder.
     */
    T withAllowedMentions(Possible<AllowedMentions> allowedMentions);

    /**
     * Creates a new builder with updated allowed mentions property.
     *
     * @param allowedMentions The allowed mentions for the new builder.
     * @return New updated builder.
     */
    default T withAllowedMentions(AllowedMentions allowedMentions) {
        return this.withAllowedMentions(Possible.of(allowedMentions));
    }

}
