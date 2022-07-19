package org.michibe.libraryshowcase.configuration.web;

import org.michibe.libraryshowcase.modules.library.model.CategoryId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCategoryIdConverter implements Converter<String, CategoryId> {
    @Override
    public CategoryId convert(String source) {
        return CategoryId.ofString(source);
    }
}
