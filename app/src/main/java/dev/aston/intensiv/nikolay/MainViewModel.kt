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
    private val state = MutableStateFlow(ContactState.empty())
    val contactState: StateFlow<ContactState> = state.asStateFlow()


    fun loadContacts(contacts: List<ContactItem>) {
        state.value = state.value.copy(contacts = contacts)
        contactIdPool = contacts.size
    }

    fun enableDeleteContactMode(enable: Boolean) {
        state.value = state.value.copy(isDeleteContactMode = enable)
    }

    fun updateContact(item: ContactItem) {
        state.value = state.value.let { currentState ->
            if (item.id == -1) {
                currentState.copy(contacts = currentState.contacts.toMutableList().apply {
                    add(item.copy(id = ++contactIdPool))
                })
            } else {
                currentState.copy(contacts = currentState.contacts.toMutableList().apply {
                    val itemIndex = indexOfFirst { it.id == item.id }
                    if (itemIndex != -1) {
                        set(itemIndex, item)
                    }
                })
            }
        }
    }

    fun moveContactItem(from: Int, to: Int) {
        state.value = state.value.let { currentState ->
            val newList = currentState.contacts.toMutableList()
            Collections.swap(newList, from, to)
            currentState.copy(contacts = newList)
        }
    }

    fun deleteContacts(selected: Set<Int>) {
        state.value = state.value.let { currentState ->
            val newList = currentState.contacts.filterIndexed { index, _ ->
                !selected.contains(index)
            }
            currentState.copy(isDeleteContactMode = false, contacts = newList)
        }
    }
}

data class ContactState(
    val isDeleteContactMode: Boolean,
    val contacts: List<ContactItem>
) {
    companion object {
        fun empty() = ContactState(
            isDeleteContactMode = false,
            contacts = mutableListOf()
        )
    }
}