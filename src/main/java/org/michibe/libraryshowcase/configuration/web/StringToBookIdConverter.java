package org.michibe.libraryshowcase.configuration.web;

import org.michibe.libraryshowcase.modules.library.model.BookId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBookIdConverter implements Converter<String, BookId> {
    @Override
    public BookId convert(String source) {
        return BookId.ofString(source);
    }
}
