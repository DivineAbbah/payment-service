@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void testProcessPayment() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest("user1", "order1", "visa-1234", 100.0);
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.orderId").value("order1"))
                .andExpect(jsonPath("$.status").value(PaymentStatus.SUCCESSFUL.toString()))
                .andExpect(jsonPath("$.amount").value(100.0));

        List<PaymentEntity> paymentEntities = paymentRepository.findByUserId("user1");
        assertThat(paymentEntities).hasSize(1);
        assertThat(paymentEntities.get(0).getOrderId()).isEqualTo("order1");
    }

    @Test
    public void testGetPaymentHistory() throws Exception {
        PaymentEntity paymentEntity = new PaymentEntity("payment1", "user1", "order1", PaymentStatus.SUCCESSFUL, 100.0, LocalDateTime.now());
        paymentRepository.save(paymentEntity);

        mockMvc.perform(get("/api/payments/history")
                .with(user("user1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].orderId").value("order1"))
                .andExpect(jsonPath("$[0].status").value(PaymentStatus.SUCCESSFUL.toString()))
                .andExpect(jsonPath("$[0].amount").value(100.0));
    }
}