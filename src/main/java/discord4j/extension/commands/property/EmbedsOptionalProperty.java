package discord4j.extension.commands.property;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.possible.Possible;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * Interface for builders supporting optional embeds property.
 *
 * @param <T> The builder type.
 */
public interface EmbedsOptionalProperty<T> {

    /**
     * Returns the current embeds, if set.
     *
     * @return Embeds, if set.
     */
    Possible<Optional<List<EmbedCreateSpec>>> embeds();

    /**
     * Creates a new builder with updated embeds property.
     *
     * @param embeds The embeds for the new builder.
     * @return New updated builder.
     */
    T withEmbeds(Possible<Optional<List<EmbedCreateSpec>>> embeds);

    /**
     * Creates a new builder with updated embeds property.
     *
     * @param embeds The embeds for the new builder.
     * @return New updated builder.
     */
    default T withEmbeds(EmbedCreateSpec... embeds) {
        return this.withEmbeds(Possible.of(Optional.of(Arrays.asList(embeds))));
    }

}
