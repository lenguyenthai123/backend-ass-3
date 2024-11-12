package com.assignment.backend_assignment3.service.mapstruct;

import com.assignment.backend_assignment3.domain.UserAccount;
import com.assignment.backend_assignment3.dto.UserAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    // Mapping từ UserAccount sang UserAccountDTO
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserAccountDto toDto(UserAccount userAccount);

    // Mapping từ UserAccountDTO sang UserAccount
    UserAccount toEntity(UserAccountDto UserAccountDto);
}