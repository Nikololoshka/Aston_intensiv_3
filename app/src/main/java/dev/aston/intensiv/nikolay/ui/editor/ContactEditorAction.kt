package dev.aston.intensiv.nikolay.ui.editor

import dev.aston.intensiv.nikolay.model.ContactItem

sealed interface ContactEditorAction {
    object ContactCreate : ContactEditorAction
    class ContactEdit(val contact: ContactItem) : ContactEditorAction

}