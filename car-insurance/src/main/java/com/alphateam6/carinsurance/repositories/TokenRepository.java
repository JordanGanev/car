package com.alphateam6.carinsurance.repositories;

import com.alphateam6.carinsurance.models.VerificationToken;

public interface TokenRepository {

    VerificationToken create(VerificationToken token);

    void delete(VerificationToken token);

    VerificationToken getByToken(String token);
}
