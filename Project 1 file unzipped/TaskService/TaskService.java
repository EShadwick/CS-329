/**
 * TaskService.java
 *
 * In-memory CRUD service for Task objects.
 *
 * Developer Notes:
 *   - Storage is a HashMap<String, Task> keyed on taskId. HashMap gives O(1)
 *     average-case performance for add, delete, and lookup — appropriate for
 *     an in-memory mobile application with no persistence layer.
 *   - No database or UI is required by the specification. This class is the
 *     entire data layer.
 *   - All public methods validate their inputs and throw
 *     IllegalArgumentException for invalid or inconsistent state. This keeps
 *     error handling consistent with Task.java and makes unit testing
 *     straightforward: every failure path has a well-defined exception type.
 *   - getTask() is a package-accessible helper used by the test suite to
 *     inspect state. It returns null for missing IDs rather than throwing,
 *     which is the conventional Map.get() contract.
 *
 * Requirements addressed:
 *   [R-SVC-1] Add tasks with a unique ID.
 *   [R-SVC-2] Delete tasks per task ID.
 *   [R-SVC-3] Update name and description per task ID.
 */
import java.util.HashMap;
import java.util.Map;

public class TaskService {

    // -------------------------------------------------------------------------
    // In-memory storage
    // -------------------------------------------------------------------------

    /**
     * The backing store for all tasks.
     *
     * Developer Notes:
     *   - HashMap is chosen over LinkedHashMap or TreeMap because insertion
     *     order and sorted order are not required by the spec.
     *   - Key type is String (taskId) to match the natural identifier used
     *     in all service operations.
     */
    private final Map<String, Task> tasks = new HashMap<>();

    // -------------------------------------------------------------------------
    // Service operations
    // -------------------------------------------------------------------------

    /**
     * Adds a new task to the service.
     *
     * Developer Notes:
     *   - Null check comes before the duplicate check so that a NullPointer
     *     can never be thrown from containsKey. Explicit checks produce clearer
     *     error messages than letting the JVM throw implicitly.
     *   - Uniqueness is enforced here rather than in Task.java because Task has
     *     no awareness of other tasks. The service layer owns the uniqueness
     *     constraint. [R-SVC-1]
     *
     * @param task A fully constructed, validated Task object.
     * @throws IllegalArgumentException if task is null or its ID already exists.
     */
    public void addTask(Task task) {
        // Guard: null task reference
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        // Guard: duplicate ID — each task ID must be unique across the store
        if (tasks.containsKey(task.getTaskId())) {
            throw new IllegalArgumentException(
                "A task with ID '" + task.getTaskId() + "' already exists.");
        }
        tasks.put(task.getTaskId(), task);
    }

    /**
     * Removes a task from the service by its ID.
     *
     * Developer Notes:
     *   - We explicitly check existence before removing rather than relying on
     *     the return value of Map.remove(). The explicit check makes the error
     *     message meaningful and the test expectation clear. [R-SVC-2]
     *
     * @param taskId The ID of the task to delete.
     * @throws IllegalArgumentException if no task with that ID exists.
     */
    public void deleteTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new IllegalArgumentException(
                "No task found with ID '" + taskId + "'.");
        }
        tasks.remove(taskId);
    }

    /**
     * Updates the name of an existing task.
     *
     * Developer Notes:
     *   - Lookup returns null for unknown IDs (standard Map contract). We
     *     convert that to an explicit exception so callers get a useful message
     *     instead of a NullPointerException on task.setName(). [R-SVC-3]
     *   - The actual name validation (null, length) is delegated to
     *     Task.setName(). This avoids duplicating business rules in the service
     *     layer — Task owns its own field constraints.
     *
     * @param taskId The ID of the task to update.
     * @param name   The new name value. Must satisfy Task name constraints.
     * @throws IllegalArgumentException if the task is not found, or if name
     *                                  fails Task validation.
     */
    public void updateName(String taskId, String name) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException(
                "No task found with ID '" + taskId + "'.");
        }
        // Delegates validation to Task.setName() — single source of truth
        task.setName(name);
    }

    /**
     * Updates the description of an existing task.
     *
     * Developer Notes:
     *   - Same pattern as updateName(). The service resolves the task by ID;
     *     field-level validation is owned by the Task object. [R-SVC-3]
     *
     * @param taskId      The ID of the task to update.
     * @param description The new description value. Must satisfy Task
     *                    description constraints.
     * @throws IllegalArgumentException if the task is not found, or if
     *                                  description fails Task validation.
     */
    public void updateDescription(String taskId, String description) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException(
                "No task found with ID '" + taskId + "'.");
        }
        // Delegates validation to Task.setDescription()
        task.setDescription(description);
    }

    // -------------------------------------------------------------------------
    // Test-support accessor
    // -------------------------------------------------------------------------

    /**
     * Returns the Task for the given ID, or null if not found.
     *
     * Developer Notes:
     *   - This method exists to support white-box unit testing. It lets
     *     TaskServiceTest inspect internal state without exposing a full
     *     collection iterator or breaking encapsulation of the map.
     *   - Returning null (rather than throwing) follows Map.get() convention
     *     and allows tests to use assertNull() for post-delete verification.
     *
     * @param taskId The task ID to look up.
     * @return The Task if present, null otherwise.
     */
    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }
}