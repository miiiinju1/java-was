package codesquad.middleware;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class MiddleWareChain {

    private final List<MiddleWare> middleWares;

    public boolean applyMiddleWares(HttpRequest request, HttpResponse response) {
        for (MiddleWare middleWare : middleWares) {
            if(!middleWare.applyMiddleWare(request, response)) {
                return false;
            }
        }
        return true;
    }

    public void addMiddleWare(MiddleWare middleWares) {
        this.middleWares.add(middleWares);
    }

    public MiddleWareChain() {
        this.middleWares = new ArrayList<>();
    }
}
