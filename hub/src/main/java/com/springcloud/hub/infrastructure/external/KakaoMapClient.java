package com.springcloud.hub.infrastructure.external;

import com.springcloud.hub.application.dto.FindAddressQuery;
import org.springframework.data.domain.Pageable;

public interface KakaoMapClient {
    String requestGeocoding(FindAddressQuery query, Pageable pageable);
}
