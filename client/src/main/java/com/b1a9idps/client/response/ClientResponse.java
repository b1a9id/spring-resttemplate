package com.b1a9idps.client.response;

import com.b1a9idps.client.externals.response.ServerResponse;

public record ClientResponse(int id, String name) {

    public static ClientResponse newInstance(ServerResponse serverResponse) {
        return new ClientResponse(serverResponse.id(), serverResponse.name());
    }
}
