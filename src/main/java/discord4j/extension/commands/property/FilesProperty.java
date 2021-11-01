package discord4j.extension.commands.property;

import discord4j.core.spec.MessageCreateFields;

import java.util.Arrays;
import java.util.List;


/**
 * Interface for builders supporting files and file spoilers property.
 *
 * @param <T> The builder type.
 */
public interface FilesProperty<T> {

    /**
     * Returns the current files, if set.
     *
     * @return Files, if set.
     */
    List<MessageCreateFields.File> files();

    /**
     * Creates a new builder with updated files property.
     *
     * @param files The files for the new builder.
     * @return New updated builder.
     */
    T withFiles(List<MessageCreateFields.File> files);

    /**
     * Creates a new builder with updated files property.
     *
     * @param files The files for the new builder.
     * @return New updated builder.
     */
    default T withFiles(MessageCreateFields.File... files) {
        return this.withFiles(Arrays.asList(files));
    }

    /**
     * Returns the current file spoilers, if set.
     *
     * @return File spoilers, if set.
     */
    List<MessageCreateFields.FileSpoiler> fileSpoilers();

    /**
     * Creates a new builder with updated file spoilers property.
     *
     * @param fileSpoilers The file spoilers for the new builder.
     * @return New updated builder.
     */
    T withFileSpoilers(List<MessageCreateFields.FileSpoiler> fileSpoilers);

    /**
     * Creates a new builder with updated file spoilers property.
     *
     * @param fileSpoilers The file spoilers for the new builder.
     * @return New updated builder.
     */
    default T withFileSpoilers(MessageCreateFields.FileSpoiler... fileSpoilers) {
        return this.withFileSpoilers(Arrays.asList(fileSpoilers));
    }

}
