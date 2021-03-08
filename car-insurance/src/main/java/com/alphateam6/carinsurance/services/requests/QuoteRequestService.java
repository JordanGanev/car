package com.alphateam6.carinsurance.services.requests;


import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.User;

import java.util.List;

public interface QuoteRequestService {

    QuoteRequest createQuoteRequest(RequestCreateDto requestCreateDto, User user);

    QuoteRequest getQuoteRequestById(int request_id, User author);

    List<QuoteRequest> getAllQuoteRequests(User userToShow, User userLogged);

    List<PremiumReference> getAllReferences();

    List<PremiumReference> updateReferences(List<PremiumReference> updatedReferences);

    double calculateTotalPremium(QuoteRequest quoteRequest);

}
