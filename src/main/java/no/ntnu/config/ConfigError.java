package no.ntnu.config;

/**
 * Enum containing all the different kind of errors (hopefully) the config can have.
 * Wold maybe be better with exemptions for every kind of error, but that wold have made the logic more messy
 */
public enum ConfigError {
    // common

    /**
     * Everything is ok
     */
    ok,

    /**
     * The return mail is not on the white listed domain
     */
    mailDomainError,

    // java

    /**
     * No pom file found
     */
    noPomError,

    /**
     * The provided class path does not resolve in a java file with a main method
     */
    noValidClasspathSetError,

    // python

    /**
     * The executable file does not exist
     */
    executableDoesNotExistError,



}
