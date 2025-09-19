package querylang.queries;

import querylang.db.Database;
import querylang.result.ClearQueryResult;

public class ClearQuery implements Query {
    @Override
    public ClearQueryResult execute(Database database) {
        int total = database.size();
        database.clear();
        return new ClearQueryResult(total);
    }
}
