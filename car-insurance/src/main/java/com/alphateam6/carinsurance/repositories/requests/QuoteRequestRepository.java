package com.alphateam6.carinsurance.repositories.requests;

import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;

import java.util.List;

public interface QuoteRequestRepository {

    QuoteRequest create(QuoteRequest quoteRequest);

    QuoteRequest getQuoteById(int request_id);

    List<PremiumReference> getReferences();

    List<QuoteRequest> getAll(int userId);

    List<PremiumReference> updateReferences(List<PremiumReference> updatedReferences);

    double calculateNetPremium(QuoteRequest quoteRequest);

}
