package com.puru.ReddisRateLimiting.API;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Service
public class ProviderRolesService {

    @Autowired
    public ProviderRolesRepo providerRolesRepo;

    public ProviderRoles addRole(String clientname, boolean iswhitelisted){

       Optional<ProviderRoles> providerRole = findbyClientName(clientname);
       if(providerRole.isPresent()){
            return null;
       }
       else {
           ProviderRoles to_save = new ProviderRoles();
           to_save.setClientName(clientname);
           to_save.setWHITE_LISTED(iswhitelisted);
           return providerRolesRepo.save(to_save);
       }
    }


    public boolean isWhiteListed(String clientname) {
        Optional<ProviderRoles> providerRole = findbyClientName(clientname);
        if (!providerRole.isPresent()) {
            return false;
        } else {
            return providerRole.get().getWHITE_LISTED();
        }
    }
    public Optional<ProviderRoles> findbyClientName(String clientName){
        return providerRolesRepo.findByClientName(clientName);

    }

    public ProviderRoles WhiteList(ProviderRoles provider){
        provider.setWHITE_LISTED(true);
        providerRolesRepo.save(provider);
        return provider;
    }



    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ProviderRoles {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer clientId;
        @Column(unique = true,nullable = false)
        String clientName;
        Boolean WHITE_LISTED;

    }

    @RestController
    @RequestMapping("/api")
    public static class ProviderRolesController {


        public final String whiteListingSecret ="WHITELISTEDXYZ";
        @Autowired
        public ProviderRolesService providerRolesService;
        @PostMapping("/create_role")
        public ResponseEntity create_role(@RequestBody Map<String,String> body){

            ProviderRoles created = providerRolesService.addRole(body.get("ClientName"),body.get("WhiteListedSecret").equals(whiteListingSecret));
            if(created!=null){
                return ResponseEntity.status(HttpStatus.CREATED).body(created);
            }
            else{
              return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Existing");
            }
        }

        @GetMapping("/get_role")
        public ResponseEntity get_role(@RequestParam String client_name){
            Optional<ProviderRoles> found = providerRolesService.findbyClientName(client_name);
            if(found.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(found.get());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        @PostMapping("/white_list")
        public ResponseEntity<?> whitelist(@RequestParam String clientName, @RequestBody Map<String,String> Secret) {
            if (whiteListingSecret.equals(Secret.get("secret"))) {
                return providerRolesService.findbyClientName(clientName)
                        .map(role -> {
                            ProviderRoles updatedRole = providerRolesService.WhiteList(role);
                            return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
                        })
                        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret");
        }

    }

}


