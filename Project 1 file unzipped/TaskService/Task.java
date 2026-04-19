/**
 * Task.java
 *
 * Represents a single task object in the mobile application.
 *
 * Developer Notes:
 *   - All fields are validated at construction time. Invalid input throws
 *     IllegalArgumentException immediately rather than allowing a partially
 *     constructed object to exist in an inconsistent state.
 *   - taskId is declared final. Java's type system enforces immutability here,
 *     so there is no setter. This satisfies the "not updatable" requirement
 *     without any runtime check.
 *   - name and description are mutable via setters, which re-validate on every
 *     write. This means the same rules apply whether the value is set at
 *     construction or via TaskService update calls.
 *   - String length is checked with .length(), which counts UTF-16 code units.
 *     For the ASCII-range input expected in this application, this is correct.
 *
 * Requirements addressed:
 *   [R-TASK-1] Unique task ID, max 10 chars, not null, not updatable.
 *   [R-TASK-2] Name field, max 20 chars, not null.
 *   [R-TASK-3] Description field, max 50 chars, not null.
 */
public class Task {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /**
     * Unique identifier for this task.
     * Declared final: assignment after construction is a compile-time error,
     * which is the strongest possible enforcement of the "not updatable" rule.
     */
    private final String taskId;

    /** Human-readable name for the task. Max 20 characters, not null. */
    private String name;

    /** Detailed description of the task. Max 50 characters, not null. */
    private String description;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a fully validated Task.
     *
     * Developer Notes:
     *   - Validation is intentionally ordered: ID first, then name, then
     *     description. This matches the order fields are declared and makes
     *     test failures easier to trace.
     *   - We throw IllegalArgumentException (unchecked) rather than a checked
     *     exception because invalid input is a programming error, not a
     *     recoverable runtime condition at this layer.
     *
     * @param taskId      Unique ID, 1–10 characters, not null.
     * @param name        Task name, 1–20 characters, not null.
     * @param description Task description, 1–50 characters, not null.
     * @throws IllegalArgumentException if any argument fails validation.
     */
    public Task(String taskId, String name, String description) {
        // [R-TASK-1] Validate task ID
        if (taskId == null || taskId.length() > 10) {
            throw new IllegalArgumentException(
                "Task ID must be non-null and no longer than 10 characters.");
        }

        // [R-TASK-2] Validate name
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException(
                "Name must be non-null and no longer than 20 characters.");
        }

        // [R-TASK-3] Validate description
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException(
                "Description must be non-null and no longer than 50 characters.");
        }

        this.taskId      = taskId;
        this.name        = name;
        this.description = description;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the task ID.
     * No corresponding setter exists — taskId is intentionally read-only
     * after construction. [R-TASK-1]
     */
    public String getTaskId() {
        return taskId;
    }

    /** Returns the task name. */
    public String getName() {
        return name;
    }

    /** Returns the task description. */
    public String getDescription() {
        return description;
    }

    // -------------------------------------------------------------------------
    // Setters (name and description only — taskId has none by design)
    // -------------------------------------------------------------------------

    /**
     * Updates the task name.
     *
     * Developer Notes:
     *   - Validation here mirrors the constructor check. This is intentional:
     *     any path that writes to the field must enforce the same rules.
     *     Centralizing into a private helper would also be acceptable for
     *     larger projects, but inline checks are clear at this scale.
     *
     * @param name Non-null string, max 20 characters.
     * @throws IllegalArgumentException if name is null or too long.
     */
    public void setName(String name) {
        if (name == null || name.length() > 20) {
            throw new IllegalArgumentException(
                "Name must be non-null and no longer than 20 characters.");
        }
        this.name = name;
    }

    /**
     * Updates the task description.
     *
     * @param description Non-null string, max 50 characters.
     * @throws IllegalArgumentException if description is null or too long.
     */
    public void setDescription(String description) {
        if (description == null || description.length() > 50) {
            throw new IllegalArgumentException(
                "Description must be non-null and no longer than 50 characters.");
        }
        this.description = description;
    }
}