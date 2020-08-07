package no.ntnu.enums;

public enum RunType {
    /**
     * The standard java maven run, where the ticket is executed with mvn exec
     */
    JAVA,

    /**
     * the standard python run where you specify the desired image and command.
     * The dependencys wil be installed from a dependecy.txt file
     */
    PYTHON,
}
