package eu.ecodex.labbox.ui.utils;


import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.io.File;
import java.nio.file.Path;

public class StringToPathConverter implements Converter<String, Path> {

    @Override
    public Result<Path> convertToModel(String path, ValueContext valueContext) {
        File file = new File(path);
        if (file.exists()) {
            return Result.ok(file.toPath());
        } else {
            return Result.error("MY OTHER MESSAGE");
        }
    }

    @Override
    public String convertToPresentation(Path path, ValueContext valueContext) {
        return path.toString();
    }
}
