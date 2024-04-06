@Configuration
public class PaymentGatewayConfig {
    @Bean
    public PaymentGatewayClient paymentGatewayClient() {
        // Configure and return the payment gateway client
        return new StripePaymentGatewayClient();
    }
}