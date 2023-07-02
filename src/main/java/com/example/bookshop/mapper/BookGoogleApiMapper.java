package com.example.bookshop.mapper;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.entity.BookGoogleApi;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface BookGoogleApiMapper {

    BookDto toDto(BookGoogleApi entity);

    Collection<BookDto> toDto(Collection<BookGoogleApi> entity);

}
