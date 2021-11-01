package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;


/**
 * Interface for builders supporting tts property.
 *
 * @param <T> The builder type.
 */
public interface TtsProperty<T> {

    /**
     * Returns the current tts, if set.
     *
     * @return Tts, if set.
     */
    Possible<Boolean> tts();

    /**
     * Creates a new builder with updated tts property.
     *
     * @param tts The tts for the new builder.
     * @return New updated builder.
     */
    T withTts(Possible<Boolean> tts);

    /**
     * Creates a new builder with updated tts property.
     *
     * @param tts The tts for the new builder.
     * @return New updated builder.
     */
    default T withTts(Boolean tts) {
        return this.withTts(Possible.of(tts));
    }

    /**
     * Creates a new builder with tts property set to true.
     * Alias for {@link #withTts(Boolean) withTts(true)}.
     *
     * @return New updated builder.
     */
    default T doTts() {
        return this.withTts(true);
    }

    /**
     * Creates a new builder with tts property set to false.
     * Alias for {@link #withTts(Boolean) withTts(false)}.
     *
     * @return New updated builder.
     */
    default T doNotTts() {
        return this.withTts(false);
    }

}
