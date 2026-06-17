package com.organization.app.qa.domain.port.in;

import com.organization.app.qa.domain.model.InstantQA;
import java.util.List;

public interface GetInstantQAsUseCase {
    List<InstantQA> getInstantQAsForMentor(Long mentorAccountId);
}
