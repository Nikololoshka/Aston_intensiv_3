package dev.aston.intensiv.nikolay.ui.list.delegate

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dev.aston.intensiv.nikolay.R
import dev.aston.intensiv.nikolay.databinding.ContactItemBinding
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.ui.setRippleEffect

fun contactDelegate(
    itemClickedListener: (position: Int) -> Unit
) = adapterDelegateViewBinding<ContactItem, ContactItem, ContactItemBinding>(
    viewBinding = { inflater, parent ->
        ContactItemBinding.inflate(inflater, parent, false)
    }
) {
    itemView.setOnClickListener { itemClickedListener(bindingAdapterPosition) }

    bind { payloads ->
        if (payloads.isNotEmpty() && payloads[0] == true) {
            binding.root.setBackgroundResource(R.drawable.background_selected)
        } else {
            binding.root.setRippleEffect()
        }

        binding.fullName.text = context.getString(
            R.string.contact_full_name, item.name, item.surname
        )
        binding.phoneNumber.text = item.number

    }
}

