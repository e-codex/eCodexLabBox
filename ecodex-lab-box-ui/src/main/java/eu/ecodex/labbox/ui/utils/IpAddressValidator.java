package eu.ecodex.labbox.ui.utils;


import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.validator.routines.InetAddressValidator;

public class IpAddressValidator implements Converter<String, String> {

    @Override
    public Result<String> convertToModel(String ip, ValueContext valueContext) {
        if (InetAddressValidator.getInstance().isValidInet4Address(ip)) {
            return Result.ok(ip);
        } else {
            return Result.error("Not a valid Ip Address!");
        }
    }

    @Override
    public String convertToPresentation(String ip, ValueContext valueContext) {
        return ip;
    }
}
