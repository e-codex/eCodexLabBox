package eu.ecodex.labbox.ui.utils;


import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class PortValidator implements Converter<String, String> {

    @Override
    public Result<String> convertToModel(String port, ValueContext valueContext) {

        boolean containsOnlyDigits = false;

        for (int i = 0; i < port.length(); i++) {
            final char c = port.charAt(i);
            if (Character.isDigit(port.charAt(i))) {
                containsOnlyDigits = true;
            } else {
                containsOnlyDigits = false;
                break;
            }
        }

        if (containsOnlyDigits) {
            return Result.ok(port);
        } else {
            return Result.error("Port must only contain digits!");
        }
    }

    @Override
    public String convertToPresentation(String port, ValueContext valueContext) {
        return port;
    }
}
