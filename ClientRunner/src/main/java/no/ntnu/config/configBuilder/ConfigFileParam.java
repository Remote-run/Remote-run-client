package no.ntnu.config.configBuilder;

import no.ntnu.util.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigFileParam implements ConfigParam {

    private String paramName;

    protected Supplier<File> getFileValue;
    protected Consumer<File> setFromFileValue;

    public ConfigFileParam(String paramName, Supplier<File> getFileValue, Consumer<File> setFromFileValue) {
        this.paramName = paramName;
        this.getFileValue = getFileValue;
        this.setFromFileValue = setFromFileValue;
    }

    @Override
    public String getConfigName() {
        return paramName;
    }

    @Override
    public String getStringValue() {
        File file = getFileValue.get();
        return new File(FileUtils.executionDir, file.getPath()).getAbsolutePath();
    }

    @Override
    public void setFromStringValue(String newValue) {
        this.setFromFileValue.accept(new File(newValue));
    }
}
