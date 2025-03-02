package com.puru.ReddisRateLimiting.API;

import com.puru.ReddisRateLimiting.API.ProviderRolesService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public  interface ProviderRolesRepo extends JpaRepository<ProviderRolesService.ProviderRoles,Integer> {

    public Optional<ProviderRolesService.ProviderRoles> findByClientName(String clientName);
}