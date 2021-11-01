package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;
import discord4j.rest.util.AllowedMentions;
import reactor.util.annotation.Nullable;

import java.util.Optional;


/**
 * Interface for builders supporting optional allowedMentions property.
 *
 * @param <T> The builder type.
 */
public interface AllowedMentionsOptionalProperty<T> {

    /**
     * Returns the current allowed mentions, if set.
     *
     * @return Allowed mentions, if set.
     */
    Possible<Optional<AllowedMentions>> allowedMentions();

    /**
     * Creates a new builder with updated allowed mentions property.
     *
     * @param allowedMentions The allowed mentions for the new builder.
     * @return New updated builder.
     */
    T withAllowedMentions(Possible<Optional<AllowedMentions>> allowedMentions);

    /**
     * Creates a new builder with updated allowed mentions property.
     *
     * @param allowedMentions The allowed mentions for the new builder.
     * @return New updated builder.
     */
    default T withAllowedMentions(@Nullable AllowedMentions allowedMentions) {
        return this.withAllowedMentions(Possible.of(Optional.ofNullable(allowedMentions)));
    }

}
