package kukekyakya.kukemarket.entity.category;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id //primarykey
    @GeneratedValue
    @Column(name="category_id")
    private Long id ;

    @Column(length = 30,nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")//외래키
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    public Category(String name,Category parent){
        this.name =name;
        this.parent=parent;
    }
}
