package ru.medisov.home_finance.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;
import ru.medisov.home_finance.common.validator.NotEmpty;
import ru.medisov.home_finance.common.validator.Valid;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tag_tbl")
@Data
@Accessors(chain = true)
@Valid
@NoArgsConstructor
public class TagModel implements SimpleModel<TagModel> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "name")
    @NaturalId
    private String name;
    @Column(name = "count")
    private long count;

    public TagModel(String name) {
        this.name = name;
    }
}