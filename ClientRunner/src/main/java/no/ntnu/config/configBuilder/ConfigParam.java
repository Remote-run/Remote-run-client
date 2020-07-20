package no.ntnu.config.configBuilder;

public interface ConfigParam {

    public String getConfigName();

    public String getStringValue();

    public void setFromStringValue(String newValue);
}
