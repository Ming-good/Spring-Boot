package hello.itemservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity  // Entity를 사용해야 JPA 객체로 인지됨. (테이블이랑 맵핑되어 관리되는 객체)
@Data
@Table(name = "Item") // 만약 객체 명칭이랑 동일하면 생략해도 상관없음
public class Item {

    @Id // PK임을 인지시킴
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY란 값이 생성되는데 DB에서 넣어주는 것을 말함. (자동생성)
    private Long id;

    @Column(name = "item_name", length = 10) // 칼럼명이 객체 변수 명이랑 동일하면 사용 안해도 상관없음. (스프링 부트와 통합해서 사용하면 필드의 카멜 케이스를 언더스코어로 치환해줌.)
    private String itemName;
    private Integer price;
    private Integer quantity;

    // JPA는 public 또는 protected의 기본 생성자가 필수이다. 그래야 프록시 기술을 사용하기 수월하다.
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
