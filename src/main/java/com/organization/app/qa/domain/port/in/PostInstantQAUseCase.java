package com.organization.app.qa.domain.port.in;

import com.organization.app.qa.domain.model.InstantQA;

public interface PostInstantQAUseCase {
    InstantQA post(Long menteeAccountId, Long fieldId, String content);
}
