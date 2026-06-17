package com.organization.app.qa.domain.port.in;

import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.model.QAAnswer;
import java.util.List;

public interface GetMyInstantQAsUseCase {
    List<InstantQA> getMyInstantQAs(Long menteeAccountId);
    List<QAAnswer> getAnswers(Long menteeAccountId, Long qaId);
}
