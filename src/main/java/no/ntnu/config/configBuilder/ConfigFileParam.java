package no.ntnu.config.configBuilder;



import no.trygvejw.util.FileUtils;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A config param where the param is a file.
 * <p>
 * The path is saved as a local path but is get and set as a absolute path
 */
public class ConfigFileParam implements ConfigParam {

    protected Supplier<File> getFileValue;
    protected Consumer<File> setFromFileValue;
    private final String paramName;

    /**
     * Creates a new file config file param
     *
     * @param paramName        the display name of this param
     * @param getFileValue     the getter supplier for the file param
     * @param setFromFileValue the setter consumer for the file value
     */
    public ConfigFileParam(String paramName, Supplier<File> getFileValue, Consumer<File> setFromFileValue) {
        this.paramName = paramName;
        this.getFileValue = getFileValue;
        this.setFromFileValue = setFromFileValue;
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
     * Get the absolute file path as a string
     *
     * @return The absolute file path as a string
     */
    @Override
    public String getStringValue() {
        File file = getFileValue.get();
        return new File(FileUtils.executionDir, file.getPath()).getAbsolutePath();
    }

    /**
     * Sets the param file path to the local version of the provided string
     *
     * @param newValue the absolute file path of the new config value
     */
    @Override
    public void setFromStringValue(String newValue) {
        this.setFromFileValue.accept(new File(newValue.replaceFirst(FileUtils.executionDir.getAbsolutePath(), "")));
    }
}
