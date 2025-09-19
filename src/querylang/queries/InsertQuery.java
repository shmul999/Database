package querylang.queries;

import querylang.db.Database;
import querylang.db.User;
import querylang.result.InsertQueryResult;

public class InsertQuery implements Query {
    private final User user;

    public InsertQuery(User user) {
        this.user = user;
    }

    @Override
    public InsertQueryResult execute(Database database) {
        // TODO: Реализовать вставку
        int newUserId = database.add(user);
        return new InsertQueryResult(newUserId);
    }
}
