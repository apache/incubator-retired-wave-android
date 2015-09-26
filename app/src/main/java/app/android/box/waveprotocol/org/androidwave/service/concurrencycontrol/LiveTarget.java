package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;

import org.waveprotocol.wave.model.operation.Operation;
import org.waveprotocol.wave.model.operation.OperationException;
import org.waveprotocol.wave.model.operation.OperationRuntimeException;
import org.waveprotocol.wave.model.operation.SilentOperationSink;

public final class LiveTarget<T, O extends Operation<? super T>> {

    private final T target;

    private final SilentOperationSink<O> executor;

    private final ProxyOperationSink<O> output;

    private LiveTarget(T target, SilentOperationSink<O> executor, ProxyOperationSink<O> output) {
        this.target = target;
        this.executor = executor;
        this.output = output;
    }

    public static <T, O extends Operation<? super T>> LiveTarget<T, O> create(final T data) {
        ProxyOperationSink<O> output = ProxyOperationSink.create();
        SilentOperationSink<O> executor = new SilentOperationSink<O>() {
            @Override
            public void consume(O operation) {
                try {
                    operation.apply(data);
                } catch (OperationException e) {
                    // Fail this object permanently
                    throw new OperationRuntimeException("Error applying op", e);
                }
            }
        };
        return new LiveTarget<T, O>(data, executor, output);
    }

    public T getTarget() {
        return target;
    }

    public SilentOperationSink<O> getExecutorSink() {
        return executor;
    }

    public ProxyOperationSink<O> getOutputSink() {
        return output;
    }
}

