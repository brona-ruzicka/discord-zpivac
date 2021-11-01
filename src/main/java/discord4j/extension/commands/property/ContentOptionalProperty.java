package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;
import reactor.util.annotation.Nullable;

import java.util.Optional;


/**
 * Interface for builders supporting optional content property.
 *
 * @param <T> The builder type.
 */
public interface ContentOptionalProperty<T> {

    /**
     * Returns the current content, if set.
     *
     * @return Content, if set.
     */
    Possible<Optional<String>> content();

    /**
     * Creates a new builder with updated content property.
     *
     * @param content The content for the new builder.
     * @return New updated builder.
     */
    T withContent(Possible<Optional<String>> content);

    /**
     * Creates a new builder with updated content property.
     *
     * @param content The content for the new builder.
     * @return New updated builder.
     */
    default T withContent(@Nullable String content) {
        return this.withContent(Possible.of(Optional.ofNullable(content)));
    }

}
