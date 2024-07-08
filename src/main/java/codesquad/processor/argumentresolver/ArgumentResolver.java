package codesquad.processor.argumentresolver;

import codesquad.http.HttpRequest;

public interface ArgumentResolver<T> {

    T resolve(HttpRequest httpRequest);
}
