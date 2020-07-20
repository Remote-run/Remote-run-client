package no.ntnu.config.configBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigIntParam implements ConfigParam {
    private String paramName;

    protected Supplier<Integer> getStringValue;
    protected Consumer<Integer> setFromStrValue;

    public ConfigIntParam(String paramName, Supplier<Integer> getStringValue, Consumer<Integer> setFromStrValue) {
        this.paramName = paramName;
        this.getStringValue = getStringValue;
        this.setFromStrValue = setFromStrValue;
    }

    @Override
    public String getConfigName() {
        return paramName;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(getStringValue.get());
    }

    @Override
    public void setFromStringValue(String newValue) {
        setFromStrValue.accept(Integer.valueOf(newValue));
    }
}
