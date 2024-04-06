@Service
public class FraudDetectionService {
    private final ThirdPartyFraudDetectionClient fraudDetectionClient;

    public FraudDetectionService(ThirdPartyFraudDetectionClient fraudDetectionClient) {
        this.fraudDetectionClient = fraudDetectionClient;
    }

    public boolean isFraudulent(PaymentRequest paymentRequest) {
        FraudDetectionResponse response = fraudDetectionClient.detectFraud(paymentRequest);
        return response.isFraudulent();
    }
}