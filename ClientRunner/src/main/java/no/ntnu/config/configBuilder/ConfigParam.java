package no.ntnu.config.configBuilder;

/**
 * Interface used for easily displaying some param in the SimpleTable. The recommended way to use this is,
 * having a supplier and consumer for setting and getting the param value.
 * <p>
 * Contains methods for getting and setting the config for easier config config
 */
public interface ConfigParam {

    /**
     * Get the display name of the config
     *
     * @return the display name of the config
     */
    String getConfigName();

    /**
     * Get the string format of the param.
     *
     * @return The string display name of the config
     */
    String getStringValue();

    /**
     * Set the param from a string value this is usually from a user input
     *
     * @param newValue the value to set the param from
     */
    void setFromStringValue(String newValue);
}
