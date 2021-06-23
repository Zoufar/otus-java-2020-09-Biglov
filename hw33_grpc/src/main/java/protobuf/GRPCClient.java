package protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import protobuf.generated.RemoteDBServiceGrpc;
import protobuf.generated.NumberMessage;
import protobuf.model.Numbers;

import java.util.concurrent.CountDownLatch;


public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        Numbers numbersInit = new Numbers(0L, 30L);
        Numbers numbersReturned = new Numbers(0L, 0L);
        long currentValue = 0L;

        CountDownLatch latch = new CountDownLatch(1);

        RemoteDBServiceGrpc.RemoteDBServiceStub newestStub = RemoteDBServiceGrpc.newStub(channel);
        newestStub.findAllNumbers (NumberMessage.newBuilder()
                .setFirstNumber(numbersInit.getFirstNumber())
                .setSecondNumber(numbersInit.getSecondNumber())
                .build(), new StreamObserver<NumberMessage>() {
            @Override
            public void onNext(NumberMessage nm) {
                numbersReturned.setFirstNumber(nm.getFirstNumber());
                logger.info(" new value : {}", nm.getFirstNumber());
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

        });

        for ( int i = 0; i <= 50 ; i++ ) {
            Thread.sleep(1000L);
            synchronized (monitor) {
                currentValue += 1 + numbersReturned.getFirstNumber();
                logger.info("currentValue : {}", currentValue);
                numbersReturned.setFirstNumber(0L);
            }
        }

        latch.await();

        channel.shutdown();
    }
}
