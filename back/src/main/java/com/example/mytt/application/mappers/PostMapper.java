package com.example.mytt.application.mappers;

import com.example.mytt.application.entities.PostEntity;
import com.example.mytt.core.domain.entities.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

  Post toDomain(PostEntity postEntity);

  PostEntity toEntity(Post post);
}
