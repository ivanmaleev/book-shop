package com.example.bookshop.mapper;

import com.example.bookshop.dto.BookDto;
import com.example.bookshop.entity.BookLocal;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface BookLocalMapper {

    BookDto toDto(BookLocal entity);

    Collection<BookDto> toDto(Collection<BookLocal> entity);

}
