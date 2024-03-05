package dev.aston.intensiv.nikolay.ui.list.delegate

import dev.aston.intensiv.nikolay.model.ContactItem

data class SelectableContact(
    val contact: ContactItem
) {
    var isSelected: Boolean = false
}

fun ContactItem.toSelectable() = SelectableContact(this)

fun SelectableContact.toContact() = this.contact