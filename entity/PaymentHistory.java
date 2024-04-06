@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {
    private String id;
    private String userId;
    private String orderId;
    private PaymentStatus status;
    private double amount;
    private LocalDateTime timestamp;
}
