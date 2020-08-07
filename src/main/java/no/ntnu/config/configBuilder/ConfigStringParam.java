package no.ntnu.config.configBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * A config param where the config is a string
 */
public class ConfigStringParam implements ConfigParam {

    protected Supplier<String> getStringValue;
    protected Consumer<String> setFromStrValue;
    private final String paramName;

    /**
     * Creates a new string config param
     *
     * @param paramName       The display name of the param
     * @param getStringValue  The supplier for getting the string value
     * @param setFromStrValue The consumer for setting the string value
     */
    public ConfigStringParam(String paramName, Supplier<String> getStringValue, Consumer<String> setFromStrValue) {
        this.paramName = paramName;
        this.getStringValue = getStringValue;
        this.setFromStrValue = setFromStrValue;
    }

    /**
     * Get the display name of the config
     *
     * @return the display name of the config
     */
    @Override
    public String getConfigName() {
        return paramName;
    }

    /**
     * Return the value of the string param
     *
     * @return the value of the string param
     */
    @Override
    public String getStringValue() {
        return getStringValue.get();
    }

    /**
     * Sets the value of the string param
     *
     * @param newValue the value to set the param from
     */
    @Override
    public void setFromStringValue(String newValue) {
        setFromStrValue.accept(newValue);
    }
}
