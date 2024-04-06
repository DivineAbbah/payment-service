@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentGatewayClient paymentGatewayClient;

    @MockBean
    private FraudDetectionService fraudDetectionService;

    @MockBean
    private PaymentIntegrationService paymentIntegrationService;

    @Test
    public void testProcessPayment() {
        PaymentRequest paymentRequest = new PaymentRequest("user1", "order1", "visa-1234", 100.0);
        PaymentGatewayResponse gatewayResponse = new PaymentGatewayResponse(true, PaymentStatus.SUCCESSFUL);
        when(fraudDetectionService.isFraudulent(paymentRequest)).thenReturn(false);
        when(paymentGatewayClient.processPayment(paymentRequest)).thenReturn(gatewayResponse);

        PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);

        assertThat(paymentResponse.getUserId()).isEqualTo("user1");
        assertThat(paymentResponse.getOrderId()).isEqualTo("order1");
        assertThat(paymentResponse.getStatus()).isEqualTo(PaymentStatus.SUCCESSFUL);
        assertThat(paymentResponse.getAmount()).isEqualTo(100.0);
        verify(paymentIntegrationService).handlePaymentSuccess(paymentResponse);
    }
}