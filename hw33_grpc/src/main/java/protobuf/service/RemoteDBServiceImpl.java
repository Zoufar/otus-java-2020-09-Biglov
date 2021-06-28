package protobuf.service;

import io.grpc.stub.StreamObserver;
import protobuf.generated.RemoteDBServiceGrpc;
import protobuf.generated.NumberMessage;
import protobuf.model.Numbers;

import java.util.List;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    private final RealDBService realDBService;

    public RemoteDBServiceImpl(RealDBService realDBService) {
        this.realDBService = realDBService;
    }

    @Override
    public void findAllNumbers(NumberMessage request, StreamObserver<NumberMessage> responseObserver) {
        List<Numbers> allNumbers = realDBService.findAllNumbers(request.getFirstNumber(), request.getSecondNumber());
        allNumbers.forEach(u -> {
            responseObserver.onNext(number2NumberMessage(u));
            try {
                Thread.sleep(2000L);
                } catch (Exception e) {
                e.printStackTrace();
                }
             });
        responseObserver.onCompleted();
    }

    private NumberMessage number2NumberMessage(Numbers number) {
        return NumberMessage.newBuilder()
                .setFirstNumber(number.getFirstNumber())
                .setSecondNumber(number.getSecondNumber())
                .build();
    }
}
