package dev.aston.intensiv.nikolay.ui.list.delegate

import androidx.recyclerview.widget.DiffUtil

object SelectableContactDiffUtil : DiffUtil.ItemCallback<SelectableContact>() {

    override fun areItemsTheSame(
        oldItem: SelectableContact,
        newItem: SelectableContact
    ): Boolean {
        return oldItem.contact.id == newItem.contact.id
    }

    override fun areContentsTheSame(
        oldItem: SelectableContact,
        newItem: SelectableContact
    ): Boolean {
        return oldItem == newItem
    }
}