package querylang.queries;

import querylang.db.Database;
import querylang.db.User;
import querylang.result.SelectQueryResult;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SelectQuery implements Query {
    private final List<? extends FieldGetter> getters;
    private final Predicate<? super User> predicate;
    private final Comparator<? super User> comparator;

    public SelectQuery(
            List<? extends FieldGetter> getters,
            Predicate<? super User> predicate,
            Comparator<? super User> comparator
    ) {
        this.getters = getters;
        this.predicate = predicate;
        this.comparator = comparator;
    }

    @Override
    public SelectQueryResult execute(Database database) {
        // TODO: Реализовать выбор
        List<List<String>> result = database.getAll().stream()
                .filter(predicate)
                .sorted(comparator)
                .map(user -> {
                    List<String> fields = new ArrayList<>();
                    for (FieldGetter gtr : getters) {
                        fields.add(gtr.getFieldValue(user));
                    }
                    return fields;
                })
                .collect(Collectors.toList());

        return new SelectQueryResult(result);
    }
}
