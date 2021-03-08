package com.alphateam6.carinsurance.models;

import com.alphateam6.carinsurance.models.requests.PremiumReference;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
public class ReferenceForm {

    private List<PremiumReference> references;

}
