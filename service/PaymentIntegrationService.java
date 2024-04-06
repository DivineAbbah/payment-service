@Service
public class PaymentIntegrationService {
    private final OrderService orderService;
    private final NotificationService notificationService;

    public PaymentIntegrationService(OrderService orderService, NotificationService notificationService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    public void handlePaymentSuccess(PaymentResponse paymentResponse) {
        orderService.updateOrderStatus(paymentResponse.getOrderId(), OrderStatus.PAID);
        notificationService.sendPaymentConfirmation(paymentResponse.getUserId(), paymentResponse.getOrderId());
    }

    public void handlePaymentFailure(PaymentResponse paymentResponse) {
        orderService.updateOrderStatus(paymentResponse.getOrderId(), OrderStatus.PAYMENT_FAILED);
        notificationService.sendPaymentFailureNotification(paymentResponse.getUserId(), paymentResponse.getOrderId());
    }
}