package com.alphateam6.carinsurance.services.requests;

import com.alphateam6.carinsurance.exceptions.InvalidOperationException;
import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import com.alphateam6.carinsurance.models.requests.PremiumReference;
import com.alphateam6.carinsurance.models.requests.QuoteRequest;
import com.alphateam6.carinsurance.models.requests.dtos.PolicyRequestDto;
import com.alphateam6.carinsurance.models.requests.dtos.RequestCreateDto;
import com.alphateam6.carinsurance.models.users.User;
import com.alphateam6.carinsurance.repositories.requests.PolicyRequestRepository;
import com.alphateam6.carinsurance.repositories.requests.QuoteRequestRepository;
import com.alphateam6.carinsurance.utils.Mapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alphateam6.carinsurance.services.ModelFactory.*;

@ExtendWith(MockitoExtension.class)
public class QuoteRequestServiceTests {

    @InjectMocks
    QuoteRequestServiceImpl requestService;

    @InjectMocks
    PolicyRequestServiceImpl policyRequestService;

    @Mock
    QuoteRequestRepository mockQuoteRequestRepository;

    @Mock
    PolicyRequestRepository mockPolicyRequestRepository;

    @Mock
    Mapper mapper;

    User testUser;
    User testUser2;
    PolicyRequestDetails testPolicy;

    @BeforeEach
    public void setup() {
        testUser = createTestUser();
        testUser2 = createTestUser2();
        testPolicy = createTestPolicy();
    }

    @Test
    public void create_ShouldThrow_WhenFirstRegDateBeforeModelYear() {
        //Arrange
        RequestCreateDto requestCreateDto = new RequestCreateDto();
        QuoteRequest quoteRequest = createQuoteRequest(2018);
        PremiumReference reference = createReference();
        List<PremiumReference> results = new ArrayList<>();
        results.add(reference);

        Mockito.when(mapper.fromDto(requestCreateDto, testUser)).thenReturn(quoteRequest);

        //Act, Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> requestService.createQuoteRequest(requestCreateDto, testUser));

    }

    @Test
    public void create_ShouldCreateAndReturn_WhenInputDataCorrect() {
        //Arrange
        RequestCreateDto requestCreateDto = new RequestCreateDto();
        QuoteRequest quoteRequest = createQuoteRequest(2014);
        PremiumReference reference = createReference();
        List<PremiumReference> results = new ArrayList<>();
        results.add(reference);

        Mockito.when(mapper.fromDto(requestCreateDto, testUser)).thenReturn(quoteRequest);
        Mockito.when(mockQuoteRequestRepository.create(quoteRequest)).thenReturn(quoteRequest);

        //Act
        requestService.createQuoteRequest(requestCreateDto, testUser);

        //Assert
        Mockito.verify(mockQuoteRequestRepository, Mockito.times(1)).create(quoteRequest);
        Assertions.assertEquals(quoteRequest, mockQuoteRequestRepository.create(quoteRequest));
    }

    @Test
    public void createPolicyRequest_ShouldCreateWhenValidData() {
        //Arrange
        QuoteRequest quoteRequest = createQuoteRequest(2014);
        PolicyRequestDto policyRequestDto = new PolicyRequestDto();
        PolicyRequestDetails policyRequestDetails = new PolicyRequestDetails();

        Mockito.when(mapper.policyRequestMapper(quoteRequest, policyRequestDto, testUser)).thenReturn(policyRequestDetails);

        //Act
        policyRequestService.createPolicyRequest(quoteRequest, policyRequestDto, testUser);

        //Assert
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(1)).createPolicyRequest(policyRequestDetails);

    }

    @Test
    public void delete_ShouldThrow_WhenLoggedUserNotAuthor() {
        //Arrange
        PolicyRequestDetails policyRequestDetails = createTestPolicy();
        policyRequestDetails.getQuoteRequest().setUser(testUser2);

        //Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> policyRequestService.deletePolicyRequest(policyRequestDetails, testUser));
    }

    @Test
    public void delete_ShouldDelete_WhenLoggedUserIsAuthor() {
        //Arrange
        PolicyRequestDetails policyRequestDetails = createTestPolicy();
        policyRequestDetails.getQuoteRequest().setUser(testUser);

        //Act
        policyRequestService.deletePolicyRequest(policyRequestDetails, testUser);

        //Act, Assert
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(1)).delete(policyRequestDetails);
    }

    @Test
    public void getById_ShouldThrow_WhenRequestNotByAuthor() {
        //Arrange
        PolicyRequestDetails policyRequestDetails = createTestPolicy();
        policyRequestDetails.getQuoteRequest().setUser(testUser2);
        policyRequestDetails.setRequest_id(1);

        //Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> policyRequestService.getPolicyRequestById(1, testUser));
    }

    @Test
    public void getById_ShouldReturnCorrect_WhenRequestByAuthorOrAdmin() {
        //Arrange
        PolicyRequestDetails policyRequestDetails = createTestPolicy();
        policyRequestDetails.getQuoteRequest().setUser(testUser);
        //policyRequestDetails.setRequest_id(1);
        testUser.getPolicies().add(policyRequestDetails.getQuoteRequest());

        Mockito.when(mockPolicyRequestRepository.getById(2)).thenReturn(policyRequestDetails);
        //Act
        PolicyRequestDetails requestedDetails = policyRequestService.getPolicyRequestById(2, testUser);

        //Assert
        Assertions.assertEquals(policyRequestDetails, requestedDetails);
    }

    @Test
    public void updateStatus_ShouldThrow_WhenLoggedUserNotAdmin() {
        //Arrange
        PolicyRequestDetails policyRequestDetails = createTestPolicy();

        //Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> policyRequestService.updatePolicyStatus(policyRequestDetails, testUser));
    }


    @Test
    public void updateStatus_ShouldNotThrow_WhenAuthorIsAdmin(){
        //Arrange
        User user = createTestAdmin();
        PolicyRequestDetails policyRequestDetails = createTestPolicy();

        //Act
        policyRequestService.updatePolicyStatus(policyRequestDetails, user);

        //Assert
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(1)).updateStatus(policyRequestDetails);

    }

    @Test
    public void getAllPending_ShouldThrow_WhenUserNotAdmin() {
        // Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> policyRequestService.getAllPending(testUser));

        Mockito.verify(mockPolicyRequestRepository, Mockito.times(0)).getAllPending();
    }

    @Test
    public void getAllPending_ShouldReturn_WhenUserIsAdmin() {
        // Arrange
        Mockito.when(mockPolicyRequestRepository.getAllPending())
                .thenReturn(Arrays.asList(testPolicy, testPolicy, testPolicy));
        User testAdmin = createTestAdmin();
        // Act
        List<PolicyRequestDetails> pendingPolicies = policyRequestService.getAllPending(testAdmin);
        // Assert
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(1)).getAllPending();
        Assertions.assertEquals(3, pendingPolicies.size());
    }

    @Test
    public void getAll_ShouldThrow_WhenUserHaveNoRights() {
        // Arrange
        User userToShow = testUser;
        User userLogged = createTestUser2();
        // Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> requestService.getAllQuoteRequests(userToShow, userLogged));
        Mockito.verify(mockQuoteRequestRepository, Mockito.times(0)).getAll(userToShow.getId());
    }

    @Test
    public void getAll_ShouldReturn_WhenUserHaveRights() {
        // Arrange
        User userToShow = testUser;
        User userAdmin = createTestAdmin();

        QuoteRequest testQuote = testPolicy.getQuoteRequest();
        Mockito.when(mockQuoteRequestRepository.getAll(userToShow.getId()))
                .thenReturn(Arrays.asList(testQuote, testQuote, testQuote));
//        Mockito.when(mockRequestRepository.getReferences())
//                .thenReturn(Arrays.asList(new PremiumReference(), new PremiumReference()));
        // Act
        requestService.getAllQuoteRequests(userToShow, userAdmin);
        List<QuoteRequest> requests = requestService.getAllQuoteRequests(userToShow, userToShow);
        // Assert
        Mockito.verify(mockQuoteRequestRepository, Mockito.times(2)).getAll(userToShow.getId());
        Assertions.assertSame(requests.size(), 3);
    }

    @Test
    public void getById_ShouldThrow_WhenUserIsNotPolicyOwnerNorAdmin() {
        // Arrange
        User user = testUser;
        PolicyRequestDetails userPolicy = createTestPolicy();
        userPolicy.getQuoteRequest().setId(3);
        user.getPolicies().add(userPolicy.getQuoteRequest());
        PolicyRequestDetails policyToGet = createTestPolicy();
        // Act, Assert
        Assertions.assertThrows(InvalidOperationException.class,
                () -> policyRequestService.getPolicyRequestById(policyToGet.getRequest_id(), user));
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(0)).getById(policyToGet.getRequest_id());
    }

    @Test
    public void getById_ShouldReturnPolicy_WhenUserIsOwnerOrAdmin() {
        // Arrange
        User user = testUser;
        PolicyRequestDetails policyToGet = createTestPolicy();
        user.getPolicies().add(policyToGet.getQuoteRequest());
        Mockito.when(mockPolicyRequestRepository.getById(2)).thenReturn(policyToGet);
        // Act
        policyRequestService.getPolicyRequestById(2, user);
        // Assert
        Mockito.verify(mockPolicyRequestRepository, Mockito.times(1)).getById(2);
    }
}
