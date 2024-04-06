@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String userId;
    private String orderId;
    private String paymentMethod;
    private double amount;
}