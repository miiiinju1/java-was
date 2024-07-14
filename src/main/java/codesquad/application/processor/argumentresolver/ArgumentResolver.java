package codesquad.application.processor.argumentresolver;

import codesquad.api.Request;

public interface ArgumentResolver<T> {

    T resolve(Request httpRequest);
}
