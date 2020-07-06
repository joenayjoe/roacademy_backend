package com.rojunaid.roacademy.auth.oauth2;

import com.rojunaid.roacademy.models.OAuth2Credential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2CredentialRepository extends CrudRepository<OAuth2Credential, Long> {

  Optional<OAuth2Credential> findFirstByProvider(String provider);
}
