import java.util.HashMap;
import java.util.Map;

public class ContactService {

    private final Map<String, Contact> contacts = new HashMap<>();

    /**
     * Adds a new contact. Throws if the ID already exists.
     */
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact must not be null.");
        }
        if (contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("A contact with ID '" + contact.getContactId() + "' already exists.");
        }
        contacts.put(contact.getContactId(), contact);
    }

    /**
     * Deletes a contact by ID. Throws if the ID is not found.
     */
    public void deleteContact(String contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("No contact found with ID '" + contactId + "'.");
        }
        contacts.remove(contactId);
    }

    /**
     * Updates the firstName of an existing contact.
     */
    public void updateFirstName(String contactId, String firstName) {
        getExistingContact(contactId).setFirstName(firstName);
    }

    /**
     * Updates the lastName of an existing contact.
     */
    public void updateLastName(String contactId, String lastName) {
        getExistingContact(contactId).setLastName(lastName);
    }

    /**
     * Updates the phone number of an existing contact.
     */
    public void updatePhone(String contactId, String phone) {
        getExistingContact(contactId).setPhone(phone);
    }

    /**
     * Updates the address of an existing contact.
     */
    public void updateAddress(String contactId, String address) {
        getExistingContact(contactId).setAddress(address);
    }

    /**
     * Retrieves a contact by ID (read-only lookup).
     */
    public Contact getContact(String contactId) {
        return getExistingContact(contactId);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private Contact getExistingContact(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("No contact found with ID '" + contactId + "'.");
        }
        return contact;
    }
}
