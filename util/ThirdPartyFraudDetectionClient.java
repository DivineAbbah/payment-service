@Component
public class ThirdPartyFraudDetectionClient {
    public FraudDetectionResponse detectFraud(PaymentRequest paymentRequest) {
        // Integrate with a third-party fraud detection service
        return new FraudDetectionResponse(false);
    }
}