package dev.aston.intensiv.nikolay

import androidx.lifecycle.ViewModel
import dev.aston.intensiv.nikolay.model.ContactItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Collections

class MainViewModel : ViewModel() {

    private var contactIdPool = 0

    // TODO("Оптимизировать операции со списком контактов")
    private var contacts: MutableList<ContactItem> = mutableListOf()

    private val state = MutableStateFlow(
        ContactState(
            isDeleteContactMode = false,
            contacts = contacts
        )
    )
    val contactState: StateFlow<ContactState> = state.asStateFlow()


    fun loadContacts(newContacts: List<ContactItem>) {
        contacts = newContacts.toMutableList()

        state.value = state.value.copy(contacts = contacts)
        contactIdPool = contacts.size
    }

    fun enableDeleteContactMode(enable: Boolean) {
        state.value = state.value.copy(isDeleteContactMode = enable)
    }

    private fun addNewContact(contact: ContactItem) {
        val newContacts = contacts.toMutableList().apply {
            add(contact.copy(id = ++contactIdPool))
        }

        contacts = newContacts
        state.value = state.value.copy(contacts = newContacts)
    }

    private fun editContact(contact: ContactItem) {
        val newContacts = contacts.let { currentContacts ->
            val updateContactIndex = currentContacts.indexOfFirst { it.id == contact.id }
            if (updateContactIndex == -1) {
                return
            }

            val newContacts = currentContacts.toMutableList()
            newContacts[updateContactIndex] = contact
            newContacts
        }

        contacts = newContacts
        state.value = state.value.copy(contacts = newContacts)
    }

    fun updateContact(contact: ContactItem) {
        if (contact.id == -1) {
            addNewContact(contact)
        } else {
            editContact(contact)
        }
    }

    fun moveContactItem(from: Int, to: Int) {
        Collections.swap(contacts, from, to)
    }

    fun deleteContacts(selected: Set<Int>) {
        val newContacts: MutableList<ContactItem> = mutableListOf()
        contacts.filterIndexedTo(newContacts) { index, _ -> !selected.contains(index) }

        contacts = newContacts
        state.value = state.value.copy(contacts = newContacts)
    }
}

data class ContactState(
    val isDeleteContactMode: Boolean,
    val contacts: List<ContactItem>
)