package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 직접 생성 안되고 다른 스타일로 생성하게끔 제약.
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // EAGER : order 조회시 member를 join 해서 한방에 같이 가져옴
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order를 persist하면 orderitem도 persist
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // Order 저장할 때 delivery entity도 persist 해줌 : 원래는 각자 해줌
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //  연관관계 메서드
    public void setMember(Member member){
        this.member = member;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // ... 문법으로 여러개 넘김
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
