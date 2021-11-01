package discord4j.extension.commands.property;

import discord4j.discordjson.possible.Possible;


/**
 * Interface for builders supporting prefer ephemeral property.
 *
 * @param <T> The builder type.
 */
public interface PreferEphemeralProperty<T> {

    /**
     * Returns the current prefer ephemeral, if set.
     *
     * @return Prefer ephemeral, if set.
     */
    Possible<Boolean> preferEphemeral();

    /**
     * Creates a new builder with updated prefer ephemeral property.
     *
     * @param preferEphemeral The prefer ephemeral for the new builder.
     * @return New updated builder.
     */
    T withPreferEphemeral(Possible<Boolean> preferEphemeral);

    /**
     * Creates a new builder with updated prefer ephemeral property.
     *
     * @param preferEphemeral The prefer ephemeral for the new builder.
     * @return New updated builder.
     */
    default T withPreferEphemeral(Boolean preferEphemeral) {
        return this.withPreferEphemeral(Possible.of(preferEphemeral));
    }

    /**
     * Creates a new builder with prefer ephemeral property set to true.
     * Alias for {@link #withPreferEphemeral(Boolean) withPreferEphemeral(true)}.
     *
     * @return New updated builder.
     */
    default T doPreferEphemeral() {
        return this.withPreferEphemeral(true);
    }

    /**
     * Creates a new builder with prefer ephemeral property set to false.
     * Alias for {@link #withPreferEphemeral(Boolean) withPreferEphemeral(false)}.
     *
     * @return New updated builder.
     */
    default T doNotPreferEphemeral() {
        return this.withPreferEphemeral(false);
    }

}
