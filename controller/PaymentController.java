@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public PaymentResponse processPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }

    @GetMapping("/history")
    public List<PaymentHistory> getPaymentHistory(@AuthenticationPrincipal User user) {
        return paymentService.getPaymentHistory(user);
    }

    @PostMapping("/methods")
    public PaymentMethod addPaymentMethod(@AuthenticationPrincipal User user, @RequestBody PaymentMethod paymentMethod) {
        return paymentService.addPaymentMethod(user, paymentMethod);
    }
}