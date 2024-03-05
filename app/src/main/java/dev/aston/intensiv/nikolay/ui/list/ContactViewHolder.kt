package dev.aston.intensiv.nikolay.ui.list

import androidx.recyclerview.widget.RecyclerView
import dev.aston.intensiv.nikolay.R
import dev.aston.intensiv.nikolay.databinding.ContactItemBinding
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.ui.setRippleEffect

class ContactViewHolder(
    private val binding: ContactItemBinding,
    onItemClicked: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener { onItemClicked(bindingAdapterPosition) }
    }

    fun bind(contact: ContactItem, isSelected: Boolean) {
        if (isSelected) {
            binding.root.setBackgroundResource(R.drawable.background_selected)
        } else {
            binding.root.setRippleEffect()
        }

        val context = binding.fullName.context
        val fullName = context.getString(R.string.contact_full_name, contact.name, contact.surname)

        binding.fullName.text = fullName
        binding.phoneNumber.text = contact.number
    }

}