package dev.aston.intensiv.nikolay.ui.list

import androidx.recyclerview.widget.DiffUtil
import dev.aston.intensiv.nikolay.model.ContactItem

object ContactDiffUtil : DiffUtil.ItemCallback<ContactItem>() {

    override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
        return oldItem == newItem
    }
}