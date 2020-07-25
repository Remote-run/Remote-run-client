package no.ntnu.config.configBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A config param where the config is a int
 */
public class ConfigIntParam implements ConfigParam {
    protected Supplier<Integer> getIntValue;
    protected Consumer<Integer> setFromIntValue;
    private final String paramName;

    /**
     * Creates a new int config param
     *
     * @param paramName       the display name of the param
     * @param getIntValue     The supplier for getting the int config value
     * @param setFromIntValue The consumer for setting the int config value
     */
    public ConfigIntParam(String paramName, Supplier<Integer> getIntValue, Consumer<Integer> setFromIntValue) {
        this.paramName = paramName;
        this.getIntValue = getIntValue;
        this.setFromIntValue = setFromIntValue;
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
     * Returns the string value of int param
     *
     * @return the string value of int param
     */
    @Override
    public String getStringValue() {
        return String.valueOf(getIntValue.get());
    }

    /**
     * Sets the int from a string value
     *
     * @param newValue the value to set the int from
     * @throws NumberFormatException if the new value is not an int
     */
    @Override
    public void setFromStringValue(String newValue) {
        setFromIntValue.accept(Integer.valueOf(newValue));
    }
}
