import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    // -------------------------------------------------------------------------
    // Valid construction
    // -------------------------------------------------------------------------

    @Test
    void testValidContactCreation() {
        Contact c = new Contact("C001", "John", "Doe", "5551234567", "123 Main St");
        assertEquals("C001",        c.getContactId());
        assertEquals("John",        c.getFirstName());
        assertEquals("Doe",         c.getLastName());
        assertEquals("5551234567",  c.getPhone());
        assertEquals("123 Main St", c.getAddress());
    }

    @Test
    void testContactIdAtMaxLength() {
        // Exactly 10 characters — should not throw
        assertDoesNotThrow(() -> new Contact("1234567890", "Jane", "Smith", "5559876543", "456 Oak Ave"));
    }

    @Test
    void testFirstNameAtMaxLength() {
        assertDoesNotThrow(() -> new Contact("C002", "Abcdefghij", "Doe", "5551234567", "123 Main St"));
    }

    @Test
    void testLastNameAtMaxLength() {
        assertDoesNotThrow(() -> new Contact("C003", "John", "Abcdefghij", "5551234567", "123 Main St"));
    }

    @Test
    void testAddressAtMaxLength() {
        // Exactly 30 characters
        String addr = "123456789012345678901234567890";
        assertDoesNotThrow(() -> new Contact("C004", "John", "Doe", "5551234567", addr));
    }

    // -------------------------------------------------------------------------
    // Contact ID validation
    // -------------------------------------------------------------------------

    @Test
    void testContactIdNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact(null, "John", "Doe", "5551234567", "123 Main St"));
    }

    @Test
    void testContactIdTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("12345678901", "John", "Doe", "5551234567", "123 Main St")); // 11 chars
    }

    // -------------------------------------------------------------------------
    // First name validation
    // -------------------------------------------------------------------------

    @Test
    void testFirstNameNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C005", null, "Doe", "5551234567", "123 Main St"));
    }

    @Test
    void testFirstNameTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C005", "Abcdefghijk", "Doe", "5551234567", "123 Main St")); // 11 chars
    }

    // -------------------------------------------------------------------------
    // Last name validation
    // -------------------------------------------------------------------------

    @Test
    void testLastNameNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C006", "John", null, "5551234567", "123 Main St"));
    }

    @Test
    void testLastNameTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C006", "John", "Abcdefghijk", "5551234567", "123 Main St")); // 11 chars
    }

    // -------------------------------------------------------------------------
    // Phone validation
    // -------------------------------------------------------------------------

    @Test
    void testPhoneNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C007", "John", "Doe", null, "123 Main St"));
    }

    @Test
    void testPhoneTooShort() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C007", "John", "Doe", "555123456", "123 Main St")); // 9 digits
    }

    @Test
    void testPhoneTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C007", "John", "Doe", "55512345678", "123 Main St")); // 11 digits
    }

    @Test
    void testPhoneContainsLetters() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C007", "John", "Doe", "555ABC4567", "123 Main St"));
    }

    // -------------------------------------------------------------------------
    // Address validation
    // -------------------------------------------------------------------------

    @Test
    void testAddressNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C008", "John", "Doe", "5551234567", null));
    }

    @Test
    void testAddressTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
            new Contact("C008", "John", "Doe", "5551234567", "1234567890123456789012345678901")); // 31 chars
    }

    // -------------------------------------------------------------------------
    // Setters — happy path
    // -------------------------------------------------------------------------

    @Test
    void testSetFirstName() {
        Contact c = new Contact("C009", "John", "Doe", "5551234567", "123 Main St");
        c.setFirstName("Jane");
        assertEquals("Jane", c.getFirstName());
    }

    @Test
    void testSetLastName() {
        Contact c = new Contact("C010", "John", "Doe", "5551234567", "123 Main St");
        c.setLastName("Smith");
        assertEquals("Smith", c.getLastName());
    }

    @Test
    void testSetPhone() {
        Contact c = new Contact("C011", "John", "Doe", "5551234567", "123 Main St");
        c.setPhone("9998887776");
        assertEquals("9998887776", c.getPhone());
    }

    @Test
    void testSetAddress() {
        Contact c = new Contact("C012", "John", "Doe", "5551234567", "123 Main St");
        c.setAddress("789 Elm St");
        assertEquals("789 Elm St", c.getAddress());
    }

    // -------------------------------------------------------------------------
    // Setters — edge cases
    // -------------------------------------------------------------------------

    @Test
    void testSetFirstNameNull() {
        Contact c = new Contact("C013", "John", "Doe", "5551234567", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> c.setFirstName(null));
    }

    @Test
    void testSetFirstNameTooLong() {
        Contact c = new Contact("C013", "John", "Doe", "5551234567", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> c.setFirstName("Abcdefghijk"));
    }

    @Test
    void testSetLastNameNull() {
        Contact c = new Contact("C014", "John", "Doe", "5551234567", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> c.setLastName(null));
    }

    @Test
    void testSetPhoneInvalid() {
        Contact c = new Contact("C015", "John", "Doe", "5551234567", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> c.setPhone("123"));
    }

    @Test
    void testSetAddressNull() {
        Contact c = new Contact("C016", "John", "Doe", "5551234567", "123 Main St");
        assertThrows(IllegalArgumentException.class, () -> c.setAddress(null));
    }

    // -------------------------------------------------------------------------
    // Contact ID immutability — no setter exists; verify getter is stable
    // -------------------------------------------------------------------------

    @Test
    void testContactIdIsNotUpdatable() {
        Contact c = new Contact("C017", "John", "Doe", "5551234567", "123 Main St");
        // Contact has no setContactId method; confirm ID remains unchanged after other updates
        c.setFirstName("Jane");
        c.setLastName("Smith");
        assertEquals("C017", c.getContactId());
    }
}
