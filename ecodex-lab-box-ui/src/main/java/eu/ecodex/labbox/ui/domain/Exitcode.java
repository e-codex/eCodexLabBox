package eu.ecodex.labbox.ui.domain;

import java.util.stream.Stream;

public enum Exitcode {

    SCRIPT_SUCCESS(0, "Script successfully processed."), 
    SCRIPT_ERROR_LAB_ID_INVALID(100, "Lab ID invalid!"), 
    MAVEN_CALL_FAILED(200, "Maven call failed!"), 
    SETUP_GW_DB_FAILED(300, "Setup of gateway database failed!"), 
    SETUP_CLIENT_DB_FAILED(400, "Setup of domibusConnectorClient-Application database failed!");

    public final int value;
    public final String text;
   
    Exitcode(int value, String text){
        this.value = value;
        this.text = text;
    }
    
    public static Exitcode getExitcodeByValue(int value) {
    
    	return Stream.of(Exitcode.values())
                .filter(d -> d.value == value)
                .findFirst()
                .get();
    	
    }
}
