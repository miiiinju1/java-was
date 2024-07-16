package codesquad.application.processor;

import codesquad.api.Request;

public interface ArgumentResolver<T> {

    T resolve(Request httpRequest);
}
