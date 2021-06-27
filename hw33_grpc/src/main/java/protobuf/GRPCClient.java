package protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import protobuf.generated.RemoteDBServiceGrpc;
import protobuf.generated.NumberMessage;
import protobuf.model.Numbers;
import protobuf.service.StreamObserverImpl;
import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final int LOOP_LIMIT = 50;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var latch = new CountDownLatch(1);
        var streamObserverImpl = new StreamObserverImpl (latch);
        var numbersInit = new Numbers(0L, 30L);
        long currentValue = 0L;

        RemoteDBServiceGrpc.RemoteDBServiceStub newestStub = RemoteDBServiceGrpc.newStub(channel);

        newestStub.findAllNumbers (NumberMessage.newBuilder()
                .setFirstNumber(numbersInit.getFirstNumber())
                .setSecondNumber(numbersInit.getSecondNumber())
                .build(), streamObserverImpl );

        for ( int i = 0; i <= LOOP_LIMIT ; i++ ) {
            Thread.sleep(1000L);
            currentValue += 1 + streamObserverImpl.getLastServerValueAndReset ();
            logger.info("currentValue : {}", currentValue);
        }

        latch.await();

        channel.shutdown();
    }
}
