@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient paymentGatewayClient;
    private final FraudDetectionService fraudDetectionService;
    private final PaymentIntegrationService paymentIntegrationService;

    public PaymentService(PaymentRepository paymentRepository,
                         PaymentGatewayClient paymentGatewayClient,
                         FraudDetectionService fraudDetectionService,
                         PaymentIntegrationService paymentIntegrationService) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayClient = paymentGatewayClient;
        this.fraudDetectionService = fraudDetectionService;
        this.paymentIntegrationService = paymentIntegrationService;
    }

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        if (fraudDetectionService.isFraudulent(paymentRequest)) {
            throw new PaymentException("Fraudulent payment detected");
        }

        PaymentGatewayResponse gatewayResponse = paymentGatewayClient.processPayment(paymentRequest);

        PaymentEntity paymentEntity = new PaymentEntity(
            UUID.randomUUID().toString(),
            paymentRequest.getUserId(),
            paymentRequest.getOrderId(),
            gatewayResponse.getStatus(),
            paymentRequest.getAmount(),
            LocalDateTime.now()
        );
        paymentRepository.save(paymentEntity);

        PaymentResponse paymentResponse = new PaymentResponse(
            paymentEntity.getId(),
            paymentEntity.getUserId(),
            paymentEntity.getOrderId(),
            paymentEntity.getStatus(),
            paymentEntity.getAmount()
        );

        if (gatewayResponse.isSuccess()) {
            paymentIntegrationService.handlePaymentSuccess(paymentResponse);
        } else {
            paymentIntegrationService.handlePaymentFailure(paymentResponse);
        }

        return paymentResponse;
    }

    public List<PaymentHistory> getPaymentHistory(User user) {
        List<PaymentEntity> paymentEntities = paymentRepository.findByUserId(user.getId());
        return paymentEntities.stream()
                .map(entity -> new PaymentHistory(
                    entity.getId(),
                    entity.getUserId(),
                    entity.getOrderId(),
                    entity.getStatus(),
                    entity.getAmount(),
                    entity.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    public PaymentMethod addPaymentMethod(User user, PaymentMethod paymentMethod) {
        PaymentMethodEntity entity = new PaymentMethodEntity(
            UUID.randomUUID().toString(),
            user.getId(),
            paymentMethod.getCardNumber(),
            paymentMethod.getExpiryDate(),
            paymentMethod.getCvv()
        );
        PaymentMethodEntity savedEntity = paymentRepository.save(entity);
        return new PaymentMethod(
            savedEntity.getId(),
            savedEntity.getUserId(),
            savedEntity.getCardNumber(),
            savedEntity.getExpiryDate(),
            savedEntity.getCvv()
        );
    }
}