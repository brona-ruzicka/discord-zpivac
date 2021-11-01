package discord4j.extension.commands.property;

import discord4j.core.object.component.LayoutComponent;
import discord4j.discordjson.possible.Possible;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * Interface for builders supporting optional components property.
 *
 * @param <T> The builder type.
 */
public interface ComponentsOptionalProperty<T> {

    /**
     * Returns the current components, if set.
     *
     * @return Components, if set.
     */
    Possible<Optional<List<LayoutComponent>>> components();

    /**
     * Creates a new builder with updated components property.
     *
     * @param components The components for the new builder.
     * @return New updated builder.
     */
    T withComponents(Possible<Optional<List<LayoutComponent>>> components);

    /**
     * Creates a new builder with updated components property.
     *
     * @param components The components for the new builder.
     * @return New updated builder.
     */
    default T withComponents(LayoutComponent... components) {
        return this.withComponents(Possible.of(Optional.of(Arrays.asList(components))));
    }

}
