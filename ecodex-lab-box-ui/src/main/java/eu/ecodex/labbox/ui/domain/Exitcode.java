package eu.ecodex.labbox.ui.domain;

public enum Exitcode {

    SCRIPT_SUCCESS(1), SCRIPT_ERROR(2), PROXY_CONNECTION_ERROR(3);

    public final int value;
    Exitcode(int value){
        this.value = value;
    }
}
