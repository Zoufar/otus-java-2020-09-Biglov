package protobuf.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generated.NumberMessage;

import java.util.concurrent.CountDownLatch;

public class StreamObserverImpl implements StreamObserver<NumberMessage> {
    private static final Logger logger = LoggerFactory.getLogger(StreamObserverImpl.class);
    private long lastServerValue = 0;
    private CountDownLatch latch;

    public StreamObserverImpl(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public synchronized void onNext(NumberMessage nm) {
        setLastServerValue(nm.getFirstNumber());
        logger.info(" new value : {}", lastServerValue);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t);
    }

    @Override
    public void onCompleted() {
        System.out.println("\n\nРасчет закончил!");
        latch.countDown();
    }

    public synchronized void setLastServerValue (long value) {
        this.lastServerValue = value;
    }

    public synchronized long getLastServerValueAndReset () {
        long lastServerValuePrev = this.lastServerValue;
        this.lastServerValue = 0;
        return lastServerValuePrev;
    }

}

