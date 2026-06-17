package com.organization.app.matching.domain.port.in;

import com.organization.app.matching.domain.model.MatchRequest;
import java.util.List;

public interface GetMyMatchRequestsQuery {
    List<MatchRequest> getMyRequests(Long menteeAccountId);
}
