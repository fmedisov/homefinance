package ru.medisov.home_finance.web.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class CategoryTransactionView {
    private Long id;
    private String name;
    private String parent;
    private CategoryTransactionView parentView;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (parent != null && parent.length() > 0) {
            builder.append(parentView.toString()).append("->");
        }

        builder.append(name);
        return builder.toString();
    }
}
