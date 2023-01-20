package com.simplesystem.todo.mapper;

import com.simplesystem.todo.models.StatusEnum;
import com.simplesystem.todo.models.TodoCreationDto;
import com.simplesystem.todo.models.TodoDto;
import com.simplesystem.todo.models.TodoModificationDto;
import com.simplesystem.todo.persistence.entity.TodoEntity;
import org.mapstruct.*;

import java.time.OffsetDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface TodoMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "doneAt", ignore = true)
    TodoEntity toEntity(TodoCreationDto todoDto);

    @Mapping(target = "status",source = ".", qualifiedByName = "todoStatus")
    TodoDto toDto(TodoEntity todoEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "doneAt", ignore = true)
    @Mapping(target = "dueAt", ignore = true)
    TodoEntity partialUpdate(TodoModificationDto todoDto, @MappingTarget TodoEntity todoEntity);

    @Named("todoStatus")
    static StatusEnum todoStatus(TodoEntity todoEntity) {
        if (todoEntity.getDoneAt() != null) {
            return StatusEnum.DONE;
        } else if (todoEntity.getDueAt().isBefore(OffsetDateTime.now())) {
            return StatusEnum.PAST_DUE;
        } else {
            return StatusEnum.NOT_DONE;
        }
    }

}
