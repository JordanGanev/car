package com.alphateam6.carinsurance.events;

import com.alphateam6.carinsurance.models.requests.PolicyRequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class UpdateStatusListener implements ApplicationListener<OnPolicyUpdateStatus> {

    private final JavaMailSender mailSender;

    @Autowired
    public UpdateStatusListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnPolicyUpdateStatus event) {
        this.policyStatusUpdate(event);
    }

    private void policyStatusUpdate(OnPolicyUpdateStatus event) {
        PolicyRequestDetails policy = event.getPolicy();
        String recipientAddress = event.getPolicy().getUser().getUserAuthentication().getUsername();
        String subject = String.format("Your policy request was %s.", policy.getRequestStatus().getType());
        String emailBody = renderEmailBody(policy);
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(emailBody);
        mailSender.send(email);
    }

    private String renderEmailBody(PolicyRequestDetails policy) {
        return String.format(
                "----- Policy Request Details -----\n" +
                        "\n" +
                        "  Name               : %s.\n" +
                        "\n" +
                        "  Vehicle            : %s.\n" +
                        "\n" +
                        "  Cubic Capacity     : %d.\n" +
                        "\n" +
                        "  Driver Age         : %s.\n" +
                        "\n" +
                        "  Previous Accidents : %s.\n" +
                        "\n" +
                        "  Total Amount       : %s.\n" +
                        "\n" +
                        "  Request Date       : %s.\n" +
                        "\n" +
                        "  Effective Date     : %s.\n" +
                        "-----------------------------------\n" +
                        "      Request was %s.\n" +
                        "\n" +
                        "      Reason : ",
                policy.getUser().getFullName(),
                policy.getRegisteredVehicle().getVehicleFullName(),
                policy.getRegisteredVehicle().getCubicCapacity(),
                policy.getQuoteRequest().getDriverAge(),
                policy.getQuoteRequest().isPreviousAccidents(),
                1000.00,
                policy.getRequestDate(),
                policy.getEffectiveDate(),
                policy.getRequestStatus().getType());
    }
}
