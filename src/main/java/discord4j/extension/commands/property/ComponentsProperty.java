package discord4j.extension.commands.property;

import discord4j.core.object.component.LayoutComponent;
import discord4j.discordjson.possible.Possible;

import java.util.Arrays;
import java.util.List;


/**
 * Interface for builders supporting components property.
 *
 * @param <T> The builder type.
 */
public interface ComponentsProperty<T> {

    /**
     * Returns the current components, if set.
     *
     * @return Components, if set.
     */
    Possible<List<LayoutComponent>> components();

    /**
     * Creates a new builder with updated components property.
     *
     * @param components The components for the new builder.
     * @return New updated builder.
     */
    T withComponents(Possible<List<LayoutComponent>> components);

    /**
     * Creates a new builder with updated components property.
     *
     * @param components The components for the new builder.
     * @return New updated builder.
     */
    default T withComponents(LayoutComponent... components) {
        return this.withComponents(Possible.of(Arrays.asList(components)));
    }

}
