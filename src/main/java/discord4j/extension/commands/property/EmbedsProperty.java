package discord4j.extension.commands.property;

import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.possible.Possible;

import java.util.Arrays;
import java.util.List;


/**
 * Interface for builders supporting embeds property.
 *
 * @param <T> The builder type.
 */
public interface EmbedsProperty<T> {

    /**
     * Returns the current embeds, if set.
     *
     * @return Embeds, if set.
     */
    Possible<List<EmbedCreateSpec>> embeds();

    /**
     * Creates a new builder with updated embeds property.
     *
     * @param embeds The embeds for the new builder.
     * @return New updated builder.
     */
    T withEmbeds(Possible<List<EmbedCreateSpec>> embeds);

    /**
     * Creates a new builder with updated embeds property.
     *
     * @param embeds The embeds for the new builder.
     * @return New updated builder.
     */
    default T withEmbeds(EmbedCreateSpec... embeds) {
        return this.withEmbeds(Possible.of(Arrays.asList(embeds)));
    }

}
