import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ContactServiceTest {

    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService();
    }

    // -------------------------------------------------------------------------
    // addContact — happy path
    // -------------------------------------------------------------------------

    @Test
    void testAddContactSuccessfully() {
        Contact c = new Contact("C001", "John", "Doe", "5551234567", "123 Main St");
        service.addContact(c);
        assertEquals("John", service.getContact("C001").getFirstName());
    }

    @Test
    void testAddMultipleContactsWithUniqueIds() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.addContact(new Contact("C002", "Jane", "Smith", "5559876543", "456 Oak Ave"));
        assertEquals("John", service.getContact("C001").getFirstName());
        assertEquals("Jane", service.getContact("C002").getFirstName());
    }

    // -------------------------------------------------------------------------
    // addContact — edge cases
    // -------------------------------------------------------------------------

    @Test
    void testAddDuplicateContactIdThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        assertThrows(IllegalArgumentException.class, () ->
            service.addContact(new Contact("C001", "Jane", "Smith", "5559876543", "456 Oak Ave")));
    }

    @Test
    void testAddNullContactThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
    }

    // -------------------------------------------------------------------------
    // deleteContact — happy path
    // -------------------------------------------------------------------------

    @Test
    void testDeleteContactSuccessfully() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.deleteContact("C001");
        assertThrows(IllegalArgumentException.class, () -> service.getContact("C001"));
    }

    @Test
    void testDeleteOneOfMultipleContacts() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.addContact(new Contact("C002", "Jane", "Smith", "5559876543", "456 Oak Ave"));
        service.deleteContact("C001");
        assertThrows(IllegalArgumentException.class, () -> service.getContact("C001"));
        assertDoesNotThrow(() -> service.getContact("C002")); // C002 should still exist
    }

    // -------------------------------------------------------------------------
    // deleteContact — edge cases
    // -------------------------------------------------------------------------

    @Test
    void testDeleteNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteContact("GHOST"));
    }

    @Test
    void testDeleteAlreadyDeletedContactThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.deleteContact("C001");
        assertThrows(IllegalArgumentException.class, () -> service.deleteContact("C001"));
    }

    // -------------------------------------------------------------------------
    // updateFirstName
    // -------------------------------------------------------------------------

    @Test
    void testUpdateFirstNameSuccessfully() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.updateFirstName("C001", "Jane");
        assertEquals("Jane", service.getContact("C001").getFirstName());
    }

    @Test
    void testUpdateFirstNameOnNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            service.updateFirstName("GHOST", "Jane"));
    }

    @Test
    void testUpdateFirstNameWithInvalidValueThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        assertThrows(IllegalArgumentException.class, () ->
            service.updateFirstName("C001", null));
        assertThrows(IllegalArgumentException.class, () ->
            service.updateFirstName("C001", "Abcdefghijk")); // 11 chars
    }

    // -------------------------------------------------------------------------
    // updateLastName
    // -------------------------------------------------------------------------

    @Test
    void testUpdateLastNameSuccessfully() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.updateLastName("C001", "Smith");
        assertEquals("Smith", service.getContact("C001").getLastName());
    }

    @Test
    void testUpdateLastNameOnNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            service.updateLastName("GHOST", "Smith"));
    }

    @Test
    void testUpdateLastNameWithInvalidValueThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        assertThrows(IllegalArgumentException.class, () ->
            service.updateLastName("C001", null));
    }

    // -------------------------------------------------------------------------
    // updatePhone
    // -------------------------------------------------------------------------

    @Test
    void testUpdatePhoneSuccessfully() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.updatePhone("C001", "9998887776");
        assertEquals("9998887776", service.getContact("C001").getPhone());
    }

    @Test
    void testUpdatePhoneOnNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            service.updatePhone("GHOST", "9998887776"));
    }

    @Test
    void testUpdatePhoneWithInvalidValueThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        assertThrows(IllegalArgumentException.class, () ->
            service.updatePhone("C001", "123")); // too short
        assertThrows(IllegalArgumentException.class, () ->
            service.updatePhone("C001", null));
        assertThrows(IllegalArgumentException.class, () ->
            service.updatePhone("C001", "55512345AB")); // non-digit chars
    }

    // -------------------------------------------------------------------------
    // updateAddress
    // -------------------------------------------------------------------------

    @Test
    void testUpdateAddressSuccessfully() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        service.updateAddress("C001", "789 Elm St");
        assertEquals("789 Elm St", service.getContact("C001").getAddress());
    }

    @Test
    void testUpdateAddressOnNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () ->
            service.updateAddress("GHOST", "789 Elm St"));
    }

    @Test
    void testUpdateAddressWithInvalidValueThrows() {
        service.addContact(new Contact("C001", "John", "Doe", "5551234567", "123 Main St"));
        assertThrows(IllegalArgumentException.class, () ->
            service.updateAddress("C001", null));
        assertThrows(IllegalArgumentException.class, () ->
            service.updateAddress("C001", "1234567890123456789012345678901")); // 31 chars
    }

    // -------------------------------------------------------------------------
    // getContact
    // -------------------------------------------------------------------------

    @Test
    void testGetContactReturnsCorrectObject() {
        Contact c = new Contact("C001", "John", "Doe", "5551234567", "123 Main St");
        service.addContact(c);
        Contact retrieved = service.getContact("C001");
        assertEquals("C001",        retrieved.getContactId());
        assertEquals("John",        retrieved.getFirstName());
        assertEquals("Doe",         retrieved.getLastName());
        assertEquals("5551234567",  retrieved.getPhone());
        assertEquals("123 Main St", retrieved.getAddress());
    }

    @Test
    void testGetNonExistentContactThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.getContact("GHOST"));
    }
}
