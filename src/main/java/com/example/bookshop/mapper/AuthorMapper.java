package com.example.bookshop.mapper;

import com.example.bookshop.dto.AuthorDto;
import com.example.bookshop.entity.Author;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author entity);

    Collection<AuthorDto> toDto(Collection<Author> entity);
}
