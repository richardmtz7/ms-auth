package com.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.entities.RoleEntity;

public interface IRoleRepository extends JpaRepository<RoleEntity, Integer>{
	List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
}
