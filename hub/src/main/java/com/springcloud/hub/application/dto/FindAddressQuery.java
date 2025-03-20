package com.springcloud.hub.application.dto;

import com.springcloud.hub.interfaces.dto.FindAddressRequest;

public record FindAddressQuery(String address) {
    public static FindAddressQuery fromRequestParam(FindAddressRequest request
    ) {
        return new FindAddressQuery(
                request.address()
        );
    }
}
