package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;


/**
 * Interface for builders supporting content property.
 *
 * @param <T> The builder type.
 */
public interface ContentProperty<T> {

    /**
     * Returns the current content, if set.
     *
     * @return Content, if set.
     */
    Possible<String> content();

    /**
     * Creates a new builder with updated content property.
     *
     * @param content The content for the new builder.
     * @return New updated builder.
     */
    T withContent(Possible<String> content);

    /**
     * Creates a new builder with updated content property.
     *
     * @param content The content for the new builder.
     * @return New updated builder.
     */
    default T withContent(String content) {
        return this.withContent(Possible.of(content));
    }

}
