/**
 * TaskServiceTest.java
 *
 * JUnit 5 unit tests for the TaskService class.
 *
 * Developer Notes — Testing Strategy:
 *   - Tests are organized into four sections: addTask, deleteTask, updateName,
 *     and updateDescription. This maps directly to the three service
 *     requirements (add, delete, update) while keeping update tests split by
 *     field for granularity.
 *   - A fresh TaskService instance is created before each test via @BeforeEach.
 *     This ensures tests are fully isolated — no shared mutable state can cause
 *     one test to affect another (a common source of flaky test suites).
 *   - Each section tests: the success (happy) path, error paths (not found,
 *     null/invalid input), and, where relevant, isolation from other records.
 *   - getTask() is used as a state inspection tool after operations. This is
 *     white-box testing: we know the service stores tasks in a Map and we
 *     verify internal state rather than just testing return values.
 *
 * Requirements covered:
 *   [R-SVC-1] Add tasks with a unique ID.
 *   [R-SVC-2] Delete tasks per task ID.
 *   [R-SVC-3] Update name and description per task ID.
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {

    // -------------------------------------------------------------------------
    // Test fixture
    // -------------------------------------------------------------------------

    /**
     * Fresh service instance for every test.
     *
     * Developer Notes:
     *   - @BeforeEach guarantees a clean slate. Without this, tasks added in
     *     one test could cause "duplicate ID" failures in another, making tests
     *     order-dependent and brittle.
     */
    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService();
    }

    // =========================================================================
    // [R-SVC-1] addTask
    // =========================================================================

    /**
     * Adding a valid task should make it retrievable by ID. [R-SVC-1]
     *
     * Developer Notes:
     *   - assertNotNull confirms the service stored the task. This is the
     *     minimal proof that addTask() succeeded — the task exists in the store.
     */
    @Test
    void testAddTaskSuccess() {
        Task task = new Task("T001", "Fix Bug", "Resolve the login null pointer.");
        service.addTask(task);
        assertNotNull(service.getTask("T001"));
    }

    /**
     * Adding a task whose ID already exists must throw. [R-SVC-1]
     *
     * Developer Notes:
     *   - Two different Task objects sharing the same ID. The second add should
     *     be rejected regardless of whether the other fields differ. Uniqueness
     *     is keyed on taskId only.
     */
    @Test
    void testAddTaskDuplicateIdThrows() {
        Task task1 = new Task("T001", "Fix Bug",  "Resolve the login null pointer.");
        Task task2 = new Task("T001", "New Task", "Another description here.");
        service.addTask(task1);
        assertThrows(IllegalArgumentException.class, () -> service.addTask(task2));
    }

    /**
     * Passing null to addTask() must throw. [R-SVC-1]
     *
     * Developer Notes:
     *   - Null guard prevents NullPointerException from propagating out of
     *     the service with an unhelpful stack trace. The service converts it
     *     to a clear IllegalArgumentException.
     */
    @Test
    void testAddNullTaskThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.addTask(null));
    }

    /**
     * Multiple tasks with distinct IDs must all be stored independently. [R-SVC-1]
     *
     * Developer Notes:
     *   - Verifies the store handles more than one entry. This also implicitly
     *     confirms that adding T002 does not overwrite T001.
     */
    @Test
    void testAddMultipleTasksWithUniqueIds() {
        service.addTask(new Task("T001", "Task One", "First task description here."));
        service.addTask(new Task("T002", "Task Two", "Second task description here."));
        assertNotNull(service.getTask("T001"));
        assertNotNull(service.getTask("T002"));
    }

    // =========================================================================
    // [R-SVC-2] deleteTask
    // =========================================================================

    /**
     * Deleting an existing task must remove it from the store. [R-SVC-2]
     *
     * Developer Notes:
     *   - assertNull after deletion confirms the task is gone. getTask()
     *     returns null for unknown keys, which is the expected post-delete state.
     */
    @Test
    void testDeleteTaskSuccess() {
        service.addTask(new Task("T001", "Fix Bug", "Resolve the login null pointer."));
        service.deleteTask("T001");
        assertNull(service.getTask("T001"));
    }

    /**
     * Deleting a task ID that does not exist must throw. [R-SVC-2]
     *
     * Developer Notes:
     *   - Attempting to delete a nonexistent record is a caller error. The
     *     service signals this with an exception rather than silently doing
     *     nothing, which would make bugs harder to detect.
     */
    @Test
    void testDeleteNonexistentTaskThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteTask("GHOST"));
    }

    /**
     * Deleting one task must not affect other tasks in the store. [R-SVC-2]
     *
     * Developer Notes:
     *   - Isolation test. Confirms the HashMap.remove() call is keyed
     *     correctly and does not accidentally clear unrelated entries.
     */
    @Test
    void testDeleteTaskDoesNotAffectOthers() {
        service.addTask(new Task("T001", "Task One", "First task description here."));
        service.addTask(new Task("T002", "Task Two", "Second task description here."));
        service.deleteTask("T001");
        assertNull(service.getTask("T001"));    // deleted
        assertNotNull(service.getTask("T002")); // untouched
    }

    // =========================================================================
    // [R-SVC-3] updateName
    // =========================================================================

    /**
     * Updating the name of an existing task must persist the new value. [R-SVC-3]
     *
     * Developer Notes:
     *   - We retrieve the task after update and check the field directly.
     *     This confirms the service located the correct object and that
     *     Task.setName() was called on it (not a copy).
     */
    @Test
    void testUpdateNameSuccess() {
        service.addTask(new Task("T001", "Fix Bug", "Resolve the login null pointer."));
        service.updateName("T001", "Bug Fix v2");
        assertEquals("Bug Fix v2", service.getTask("T001").getName());
    }

    /**
     * Updating the name for a nonexistent task ID must throw. [R-SVC-3]
     *
     * Developer Notes:
     *   - The service converts a null return from Map.get() into an explicit
     *     exception. Without this, task.setName() would throw NullPointerException
     *     — technically correct but far less informative.
     */
    @Test
    void testUpdateNameOnNonexistentTaskThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> service.updateName("GHOST", "New Name"));
    }

    /**
     * Passing null as the new name must throw. [R-SVC-3]
     *
     * Developer Notes:
     *   - The exception originates in Task.setName(), not TaskService. The
     *     service delegates field validation down to the Task object (single
     *     source of truth). This test confirms that delegation is wired
     *     correctly.
     */
    @Test
    void testUpdateNameNullThrows() {
        service.addTask(new Task("T001", "Fix Bug", "Resolve the login null pointer."));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateName("T001", null));
    }

    /**
     * Passing an over-length name must throw. [R-SVC-3]
     */
    @Test
    void testUpdateNameTooLongThrows() {
        service.addTask(new Task("T001", "Fix Bug", "Resolve the login null pointer."));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateName("T001", "This name is way too long!"));
    }

    // =========================================================================
    // [R-SVC-3] updateDescription
    // =========================================================================

    /**
     * Updating the description of an existing task must persist the new value. [R-SVC-3]
     *
     * Developer Notes:
     *   - Same pattern as testUpdateNameSuccess(). Separate test because
     *     name and description are independent fields with different length
     *     limits (20 vs 50). Testing them separately makes failures unambiguous.
     */
    @Test
    void testUpdateDescriptionSuccess() {
        service.addTask(new Task("T001", "Fix Bug", "Original description text here."));
        service.updateDescription("T001", "Updated description for this task.");
        assertEquals("Updated description for this task.",
            service.getTask("T001").getDescription());
    }

    /**
     * Updating the description for a nonexistent task ID must throw. [R-SVC-3]
     */
    @Test
    void testUpdateDescriptionOnNonexistentTaskThrows() {
        assertThrows(IllegalArgumentException.class,
            () -> service.updateDescription("GHOST", "Some description."));
    }

    /**
     * Passing null as the new description must throw. [R-SVC-3]
     */
    @Test
    void testUpdateDescriptionNullThrows() {
        service.addTask(new Task("T001", "Fix Bug", "Original description text here."));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateDescription("T001", null));
    }

    /**
     * Passing an over-length description must throw. [R-SVC-3]
     *
     * Developer Notes:
     *   - Over 50 characters. Exception bubbles up from Task.setDescription().
     */
    @Test
    void testUpdateDescriptionTooLongThrows() {
        service.addTask(new Task("T001", "Fix Bug", "Original description text here."));
        assertThrows(IllegalArgumentException.class,
            () -> service.updateDescription("T001",
                "This description is definitely too long and exceeds fifty chars!"));
    }
}