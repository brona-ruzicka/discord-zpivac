package discord4j.extension.commands.property;

import discord4j.common.util.Snowflake;


/**
 * Interface for builders supporting follow-up id property.
 *
 * @param <T> The builder type.
 */
public interface FollowupIdProperty<T> {

    /**
     * Returns the current follow-up id.
     *
     * @return Follow-up id.
     */
    Snowflake followupId();

    /**
     * Creates a new builder with updated follow-up id property.
     *
     * @param followupId The follow-up id for the new builder.
     * @return New updated builder.
     */
    T withFollowupId(Snowflake followupId);

}
