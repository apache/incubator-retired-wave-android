package app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol;

import com.google.common.base.Preconditions;

import org.waveprotocol.wave.model.operation.Operation;
import org.waveprotocol.wave.model.operation.SilentOperationSink;
import org.waveprotocol.wave.model.util.CollectionUtils;

import java.util.Queue;

public final class ProxyOperationSink<O extends Operation<?>> implements SilentOperationSink<O> {

    private Queue<O> queue;
    private SilentOperationSink<O> target;

    private ProxyOperationSink() {
    }

    public static <O extends Operation<?>> ProxyOperationSink<O> create() {
        return new ProxyOperationSink<O>();
    }

    public void setTarget(SilentOperationSink<O> target) {
        Preconditions.checkState(this.target == null);
        this.target = target;

        if (queue != null) {
            while (!queue.isEmpty()) {
                target.consume(queue.poll());
            }
            queue = null;
        }
    }

    @Override
    public void consume(O op) {
        if (target != null) {
            target.consume(op);
        } else {
            if (queue == null) {
                queue = CollectionUtils.createQueue();
            }
            queue.add(op);
        }
    }
}