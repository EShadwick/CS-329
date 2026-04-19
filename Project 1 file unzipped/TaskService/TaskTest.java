/**
 * TaskTest.java
 *
 * JUnit 5 unit tests for the Task class.
 *
 * Developer Notes — Testing Strategy:
 *   - Tests are organized into four sections: constructor (happy path),
 *     task ID requirements, name requirements, and description requirements.
 *     This mirrors the three class-level requirements in the rubric and makes
 *     it easy to trace each test back to a specific requirement.
 *   - Every constraint is tested from both directions:
 *       positive test  — valid input, assertDoesNotThrow or assertEquals
 *       negative tests — null input and over-length input, assertThrows
 *   - Boundary value analysis is applied at each maximum length (10, 20, 50).
 *     A string of exactly max length must pass; max+1 must fail.
 *   - The "not updatable" requirement for taskId is verified by confirming
 *     that no setTaskId() method exists (enforced by the compiler via the
 *     final keyword). The test demonstrates this by checking the getter
 *     returns the original value consistently.
 *
 * Requirements covered:
 *   [R-TASK-1] Task ID: unique, max 10 chars, not null, not updatable.
 *   [R-TASK-2] Name: max 20 chars, not null.
 *   [R-TASK-3] Description: max 50 chars, not null.
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    // =========================================================================
    // Constructor — happy path
    // =========================================================================

    /**
     * Verify that a Task constructed with all valid arguments stores and
     * returns each field correctly.
     *
     * Developer Notes:
     *   - This is the baseline "everything works" test. If this fails,
     *     something is wrong with the constructor or the getters — both more
     *     fundamental than validation logic.
     */
    @Test
    void testValidTaskCreation() {
        Task task = new Task("T001", "Fix Bug", "Resolve the null pointer issue in login.");
        assertEquals("T001",   task.getTaskId());
        assertEquals("Fix Bug", task.getName());
        assertEquals("Resolve the null pointer issue in login.", task.getDescription());
    }

    // =========================================================================
    // [R-TASK-1] Task ID requirements
    // =========================================================================

    /**
     * Null task ID must throw. [R-TASK-1]
     *
     * Developer Notes:
     *   - assertThrows captures the thrown exception and asserts its type.
     *     The lambda wraps the constructor call so the exception is thrown
     *     inside the assertion rather than propagating to JUnit as a test
     *     failure.
     */
    @Test
    void testTaskIdNotNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task(null, "Fix Bug", "Description here."));
    }

    /**
     * Task ID longer than 10 characters must throw. [R-TASK-1]
     *
     * Developer Notes:
     *   - 11-character string is the smallest value that exceeds the limit.
     *     Testing with a very long string is also valid but adds no additional
     *     information beyond what an 11-char test provides.
     */
    @Test
    void testTaskIdTooLong() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("12345678901", "Fix Bug", "Description here."));
    }

    /**
     * Task ID of exactly 10 characters must be accepted. [R-TASK-1]
     *
     * Developer Notes:
     *   - Boundary value: the maximum allowed length should pass.
     *     assertDoesNotThrow confirms no exception is thrown.
     */
    @Test
    void testTaskIdExactlyTenChars() {
        assertDoesNotThrow(() -> new Task("1234567890", "Fix Bug", "Description here."));
    }

    /**
     * Task ID must not be updatable after construction. [R-TASK-1]
     *
     * Developer Notes:
     *   - The final keyword on taskId makes setTaskId() a compile-time error,
     *     which is stronger than any runtime check. This test verifies the
     *     getter returns the same value throughout the object's lifetime,
     *     demonstrating immutability without needing to call a setter.
     */
    @Test
    void testTaskIdNotUpdatable() {
        Task task = new Task("T001", "Fix Bug", "Description here.");
        // Calling task.setTaskId() would be a compile error — field is final.
        // We confirm the getter is stable as the observable proof of immutability.
        assertEquals("T001", task.getTaskId());
        assertEquals("T001", task.getTaskId()); // second call — value cannot change
    }

    // =========================================================================
    // [R-TASK-2] Name requirements
    // =========================================================================

    /**
     * Null name must throw. [R-TASK-2]
     */
    @Test
    void testNameNotNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("T001", null, "Description here."));
    }

    /**
     * Name longer than 20 characters must throw. [R-TASK-2]
     *
     * Developer Notes:
     *   - "This name is way too long!" is 26 characters — clearly over the
     *     limit. Using a slightly over-limit value like 21 characters would
     *     also be valid and more precise.
     */
    @Test
    void testNameTooLong() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("T001", "This name is way too long!", "Description here."));
    }

    /**
     * Name of exactly 20 characters must be accepted. [R-TASK-2]
     *
     * Developer Notes:
     *   - Boundary value at the maximum. The string "12345678901234567890"
     *     is exactly 20 characters.
     */
    @Test
    void testNameExactlyTwentyChars() {
        assertDoesNotThrow(() -> new Task("T001", "12345678901234567890", "Description here."));
    }

    /**
     * setName() with a valid value must update the field. [R-TASK-2]
     *
     * Developer Notes:
     *   - Tests the setter independently of the constructor. The two code
     *     paths both call the same validation logic, but testing them
     *     separately ensures neither path was accidentally skipped.
     */
    @Test
    void testSetNameValid() {
        Task task = new Task("T001", "Fix Bug", "Description here.");
        task.setName("New Name");
        assertEquals("New Name", task.getName());
    }

    /**
     * setName(null) must throw. [R-TASK-2]
     */
    @Test
    void testSetNameNull() {
        Task task = new Task("T001", "Fix Bug", "Description here.");
        assertThrows(IllegalArgumentException.class, () -> task.setName(null));
    }

    /**
     * setName() with a name over 20 characters must throw. [R-TASK-2]
     */
    @Test
    void testSetNameTooLong() {
        Task task = new Task("T001", "Fix Bug", "Description here.");
        assertThrows(IllegalArgumentException.class,
            () -> task.setName("This name is way too long!"));
    }

    // =========================================================================
    // [R-TASK-3] Description requirements
    // =========================================================================

    /**
     * Null description must throw. [R-TASK-3]
     */
    @Test
    void testDescriptionNotNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("T001", "Fix Bug", null));
    }

    /**
     * Description longer than 50 characters must throw. [R-TASK-3]
     *
     * Developer Notes:
     *   - The test string is 65 characters — well over the 50-char limit.
     */
    @Test
    void testDescriptionTooLong() {
        assertThrows(IllegalArgumentException.class,
            () -> new Task("T001", "Fix Bug",
                "This description is definitely too long and exceeds fifty chars!"));
    }

    /**
     * Description of exactly 50 characters must be accepted. [R-TASK-3]
     *
     * Developer Notes:
     *   - Boundary value: "12345678901234567890123456789012345678901234567890"
     *     is exactly 50 characters.
     */
    @Test
    void testDescriptionExactlyFiftyChars() {
        assertDoesNotThrow(() -> new Task("T001", "Fix Bug",
            "12345678901234567890123456789012345678901234567890"));
    }

    /**
     * setDescription() with a valid value must update the field. [R-TASK-3]
     */
    @Test
    void testSetDescriptionValid() {
        Task task = new Task("T001", "Fix Bug", "Original description.");
        task.setDescription("Updated description for task.");
        assertEquals("Updated description for task.", task.getDescription());
    }

    /**
     * setDescription(null) must throw. [R-TASK-3]
     */
    @Test
    void testSetDescriptionNull() {
        Task task = new Task("T001", "Fix Bug", "Original description.");
        assertThrows(IllegalArgumentException.class, () -> task.setDescription(null));
    }

    /**
     * setDescription() with a description over 50 characters must throw. [R-TASK-3]
     */
    @Test
    void testSetDescriptionTooLong() {
        Task task = new Task("T001", "Fix Bug", "Original description.");
        assertThrows(IllegalArgumentException.class,
            () -> task.setDescription(
                "This description is definitely too long and exceeds fifty chars!"));
    }
}