package com.alphateam6.carinsurance.services.requests;

import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.requests.QuoteRequestRepository;
import com.alphateam6.carinsurance.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class QuoteRequestServiceImpl implements QuoteRequestService {

    public static final String NOT_ALLOWED_TO_VIEW_ERROR = "You are not allowed to view this page.";
    public static final String NOT_ALLOWED_TO_EDIT_ERROR = "You are not allowed to edit this request.";
    public static final String DATE_MISMATCH = "First registration date cannot be earlier than model year!";

    private final QuoteRequestRepository quoteRequestRepository;
    private final Mapper mapper;

    @Autowired
    public QuoteRequestServiceImpl(QuoteRequestRepository quoteRequestRepository, Mapper mapper) {
        this.mapper = mapper;
        this.quoteRequestRepository = quoteRequestRepository;
    }

    @Override
    public QuoteRequest createQuoteRequest(RequestCreateDto requestCreateDto, User user) {
        QuoteRequest quoteRequest = mapper.fromDto(requestCreateDto, user);
        quoteRequest.setTotalAmount(calculateTotalPremium(quoteRequest));

        if (Integer.parseInt(quoteRequest.getRegisteredVehicle().getRegistrationDate().substring(0, 4)) <
                quoteRequest.getRegisteredVehicle().getVehicleModel().getModelYear()) {
            throw new IllegalArgumentException(DATE_MISMATCH);
        }
        return quoteRequestRepository.create(quoteRequest);
    }

    @Override
    public List<QuoteRequest> getAllQuoteRequests(User userToShow, User userLogged) {
        if (userToShow.getId() != userLogged.getId() && !userLogged.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_VIEW_ERROR);
        }
        List<QuoteRequest> userQuotes = quoteRequestRepository.getAll(userToShow.getId());
        userQuotes.forEach(q -> q.setTotalAmount(calculateTotalPremium(q)));
        return userQuotes;
    }

    @Override
    public QuoteRequest getQuoteRequestById(int request_id, User author) {
        if (author.getPolicies().stream().noneMatch(p -> p.getId() == request_id) && !author.isAdmin()) {
            throw new InvalidOperationException(NOT_ALLOWED_TO_VIEW_ERROR);
        }
        return quoteRequestRepository.getQuoteById(request_id);
    }

    @Override
    public List<PremiumReference> getAllReferences() {
        return quoteRequestRepository.getReferences();
    }

    public List<PremiumReference> updateReferences(List<PremiumReference> updatedReferences) {
        return quoteRequestRepository.updateReferences(updatedReferences);
    }

    private double calculateNetPremium(QuoteRequest quoteRequest) {
        double prevAccidentsRate = 1.2;
        double yourDriverRate = 1.05;
        double netPremium;
        double baseAmount = quoteRequestRepository.calculateNetPremium(quoteRequest);

        netPremium = (quoteRequest.isPreviousAccidents()) ? baseAmount * prevAccidentsRate : baseAmount;
        netPremium = (quoteRequest.getDriverAge() < 25) ? netPremium * yourDriverRate : netPremium;
        return netPremium;
    }

    public double calculateTotalPremium(QuoteRequest quoteRequest) {
        double tax = 1.1;
        double result = calculateNetPremium(quoteRequest) * tax;
        DecimalFormat df = new DecimalFormat("0.00");
        result = Double.parseDouble(df.format(result));
        return result;
    }
}
