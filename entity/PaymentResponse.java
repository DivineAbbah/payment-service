@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String id;
    private String userId;
    private String orderId;
    private PaymentStatus status;
    private double amount;
}