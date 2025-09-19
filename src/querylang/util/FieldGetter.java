package querylang.util;

import querylang.db.User;

@FunctionalInterface
public interface FieldGetter {
    String getFieldValue(User user);
}
