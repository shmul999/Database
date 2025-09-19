package querylang.parsing;

import querylang.db.User;
import querylang.queries.*;
import querylang.util.FieldGetter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class QueryParser {
    public ParsingResult<Query> parse(String line) {
        String[] parts = line.strip().split("\\s+", 2);
        String queryName = parts[0].strip().toUpperCase();
        String arguments = parts.length > 1 ? parts[1].strip() : "";
        return switch (queryName) {
            case "CLEAR" -> parseClear(arguments);
            // TODO: Реализовать оставшиеся команды
            case "INSERT" -> parseInsert(arguments);
            case "REMOVE" -> parseRemove(arguments);
            case "SELECT" -> parseSelect(arguments);
            default -> ParsingResult.error("Unexpected query name '" + queryName + "'");
        };
    }

    private ParsingResult<Query> parseClear(String arguments) {
        if (!arguments.isEmpty()) {
            return ParsingResult.error("'CLEAR' doesn't accept arguments");
        }
        return ParsingResult.of(new ClearQuery());
    }

    private ParsingResult<Query> parseInsert(String arguments) {
        try {
            String cleanedArgs = arguments.strip()
                    .replaceAll("^\\(", "")
                    .replaceAll("\\)$", "");

            String[] data = cleanedArgs.split("\\s*,\\s*");

            if (data.length != 4) {
                return ParsingResult.error("INSERT requires exactly 4 values");
            }

            String firstName = data[0].strip();
            String lastName = data[1].strip();
            String city = data[2].strip();
            String ageStr = data[3].replaceAll("[^0-9]", "");

            if (ageStr.isEmpty()) {
                return ParsingResult.error("Age must be a number");
            }

            int age = Integer.parseInt(ageStr);

            if (firstName.isEmpty() || lastName.isEmpty()) {
                return ParsingResult.error("First name and last name cannot be empty");
            }

            if (age <= 0) {
                return ParsingResult.error("Age must be a positive number");
            }

            User newUser = new User(-1, firstName, lastName, city, age);
            return ParsingResult.of(new InsertQuery(newUser));

        } catch (NumberFormatException e) {
            return ParsingResult.error("Invalid age format: must be a number");
        } catch (Exception e) {
            return ParsingResult.error("Error parsing INSERT query: " + e.getMessage());
        }
    }

    private ParsingResult<Query> parseRemove(String arguments){
        try {
            int id = Integer.parseInt(arguments.strip());
            return ParsingResult.of(new RemoveQuery(id));
        } catch (NumberFormatException e) {
            return ParsingResult.error("ID must be integer");
        }
    }

    private ParsingResult<Query> parseSelect(String arguments) {
        try {
            String fieldsPart = arguments.split("(?=FILTER|ORDER)", 2)[0].strip();
            List<FieldGetter> getters = parseFields(fieldsPart);

            if (getters == null) {
                return ParsingResult.error("Invalid field selection syntax");
            }
            Predicate<User> predicate = parseFilters(arguments);
            if (predicate == null) {
                return ParsingResult.error("Invalid FILTER syntax");
            }
            Comparator<User> comparator = parseOrder(arguments);
            if (comparator == null) {
                return ParsingResult.error("Invalid ORDER syntax");
            }

            return ParsingResult.of(new SelectQuery(getters, predicate, comparator));
        } catch (Exception e) {
            return ParsingResult.error("Error parsing SELECT query: " + e.getMessage());
        }
    }

    private List<FieldGetter> parseFields(String fieldsData) {
        if (fieldsData.equals("*")) {
            return List.of(
                    user -> String.valueOf(user.id()),
                    User::firstName,
                    User::lastName,
                    User::city,
                    user -> String.valueOf(user.age())
            );
        }

        if (!fieldsData.startsWith("(") || !fieldsData.endsWith(")")) {
            return null;
        }

        String[] fields = fieldsData.substring(1, fieldsData.length() - 1).split(",");
        List<FieldGetter> getters = new ArrayList<>();

        for (String field : fields) {
            String fieldName = field.strip();
            switch (fieldName) {
                case "id": getters.add(user -> String.valueOf(user.id())); break;
                case "firstName": getters.add(User::firstName); break;
                case "lastName": getters.add(User::lastName); break;
                case "city": getters.add(User::city); break;
                case "age": getters.add(user -> String.valueOf(user.age())); break;
                default: return null;
            }
        }

        return getters;
    }

    private Predicate<User> parseFilters(String arguments) {
        Predicate<User> predicate = user -> true;

        String[] parts = arguments.split("(?=FILTER\\()");
        for (String part : parts) {
            if (part.startsWith("FILTER(")) {
                String filterContent = part.substring(7).split("\\)", 2)[0];
                String[] filterParts = filterContent.split(",", 2);
                if (filterParts.length != 2) return null;

                String field = filterParts[0].strip();
                String value = filterParts[1].strip();

                switch (field) {
                    case "id":
                        try {
                            int id = Integer.parseInt(value);
                            predicate = predicate.and(u -> u.id() == id);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                        break;
                    case "firstName":
                        predicate = predicate.and(u -> u.firstName().equals(value));
                        break;
                    case "lastName":
                        predicate = predicate.and(u -> u.lastName().equals(value));
                        break;
                    case "city":
                        predicate = predicate.and(u -> u.city().equals(value));
                        break;
                    case "age":
                        try {
                            int age = Integer.parseInt(value);
                            predicate = predicate.and(u -> u.age() == age);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                        break;
                    default:
                        return null;
                }
            }
        }
        return predicate;
    }

    private Comparator<User> parseOrder(String arguments) {
        if (!arguments.contains("ORDER(")) {
            return Comparator.comparingInt(User::id);
        }

        String orderPart = arguments.split("ORDER\\(", 2)[1].split("\\)", 2)[0];
        String[] orderParts = orderPart.split(",", 2);
        if (orderParts.length != 2) return null;

        String field = orderParts[0].strip();
        String direction = orderParts[1].strip();

        Comparator<User> comparator = switch (field) {
            case "id" -> Comparator.comparingInt(User::id);
            case "firstName" -> Comparator.comparing(User::firstName);
            case "lastName" -> Comparator.comparing(User::lastName);
            case "city" -> Comparator.comparing(User::city);
            case "age" -> Comparator.comparingInt(User::age);
            default -> null;
        };

        if (comparator == null) return null;

        if (direction.equalsIgnoreCase("DESC")) {
            comparator = comparator.reversed();
        } else if (!direction.equalsIgnoreCase("ASC")) {
            return null;
        }

        return comparator.thenComparingInt(User::id);
    }
}
