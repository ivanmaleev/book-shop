package com.example.bookshop.mapper;

import com.example.bookshop.dto.GenreDto;
import com.example.bookshop.entity.Genre;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreDto toDto(Genre entity);

    Collection<GenreDto> toDto(Collection<Genre> entity);
}
