package querylang.queries;

import querylang.db.Database;
import querylang.result.QueryResult;

public interface Query {
    QueryResult execute(Database database);
}
