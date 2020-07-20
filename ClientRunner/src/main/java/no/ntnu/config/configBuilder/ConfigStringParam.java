package no.ntnu.config.configBuilder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigStringParam implements ConfigParam {

    private String paramName;

    protected Supplier<String> getStringValue;
    protected Consumer<String> setFromStrValue;

    public ConfigStringParam(String paramName, Supplier<String> getStringValue, Consumer<String> setFromStrValue) {
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
        return getStringValue.get();
    }

    @Override
    public void setFromStringValue(String newValue) {
        setFromStrValue.accept(newValue);
    }
}
