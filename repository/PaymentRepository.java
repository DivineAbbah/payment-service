@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    List<PaymentEntity> findByUserId(String userId);
    Optional<PaymentMethodEntity> findByUserIdAndId(String userId, String paymentMethodId);
}