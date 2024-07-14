package codesquad.webserver.middleware;

import codesquad.api.Request;
import codesquad.api.Response;

import java.util.ArrayList;
import java.util.List;

public class MiddleWareChain {

    private final List<MiddleWare> middleWares;

    public void applyMiddleWares(Request httpRequest, Response httpResponse) {
        for (MiddleWare middleWare : middleWares) {
            middleWare.applyMiddleWare(httpRequest, httpResponse);
        }
    }

    public void addMiddleWare(MiddleWare middleWares) {
        this.middleWares.add(middleWares);
    }

    public MiddleWareChain() {
        this.middleWares = new ArrayList<>();
    }
}
