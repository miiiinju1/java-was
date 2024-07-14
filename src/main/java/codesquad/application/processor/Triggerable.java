package codesquad.application.processor;

public interface Triggerable<T, R> {

    R run(T t);

}
