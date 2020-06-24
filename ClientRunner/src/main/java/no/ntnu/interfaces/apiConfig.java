package no.ntnu.interfaces;

import java.io.File;
import java.io.IOException;

/*
brainstorming hva trenger jeg

- = server
+ = client

## alle
- return mail
- path til executable
+ hva som skal pakkes med

## java
- dependencys
- class path


## andre språk
-ta seiner


 */


public abstract class apiConfig {

    public final File serverConfigFp = new File(".serverConfig");
    public final File localConfigFp = new File(".localconfig");

    protected enum configParams {
        returnMail,
    }


    public apiConfig() throws IOException{

    }


}
