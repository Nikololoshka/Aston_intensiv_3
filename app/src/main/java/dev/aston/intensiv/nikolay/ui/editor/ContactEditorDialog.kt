package dev.aston.intensiv.nikolay.ui.editor

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import dev.aston.intensiv.nikolay.R
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.databinding.DialogEditContactBinding

class ContactEditorDialog : DialogFragment(R.layout.dialog_edit_contact) {


    private var _binding: DialogEditContactBinding? = null
    private val binding get() = _binding!!

    private lateinit var action: String
    private var currentContact: ContactItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullscreenDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogEditContactBinding.bind(view)

        action = requireArguments().getAction(DIALOG_ACTION)
        currentContact = requireArguments().getBundle(CURRENT_CONTACT)?.getContact()

        setupToolbarSettings(action)
        currentContact?.let { bindContact(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbarSettings(action: String?) {
        binding.toolbar.setTitle(
            if (action == CREATE_CONTACT) R.string.contact_creator else R.string.contact_editor
        )
        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.save_contact -> {
                    setFormContactAsResult()
                    dismiss()
                    true
                }

                else -> false
            }
        }
    }

    private fun setFormContactAsResult() {
        val contact = ContactItem(
            id = currentContact?.id ?: -1,
            name = binding.name.text.toString(),
            surname = binding.surname.text.toString(),
            number = binding.phoneNumber.text.toString()
        )
        setFragmentResult(CONTACT_EDITOR_REQUEST, contact.toBundle())
    }

    private fun bindContact(contact: ContactItem) {
        binding.name.setText(contact.name)
        binding.surname.setText(contact.surname)
        binding.phoneNumber.setText(contact.number)
    }

    private fun Bundle.getAction(key: String): String {
        when (val value = getString(key)) {
            CREATE_CONTACT, EDIT_CONTACT -> return value
            else -> throw IllegalArgumentException("Action parameter was not passed")
        }
    }

    companion object {

        private const val DIALOG_ACTION = "dialog_action"
        private const val CREATE_CONTACT = "create_contact"
        private const val EDIT_CONTACT = "edit_contact"

        private const val CURRENT_CONTACT = "current_contact"
        private const val CONTACT_ID = "contact_id"
        private const val CONTACT_NAME = "contact_name"
        private const val CONTACT_SURNAME = "contact_surname"
        private const val CONTACT_PHONE_NUMBER = "contact_phone_number"

        const val CONTACT_EDITOR_REQUEST = "contact_Editor_request"

        fun parseResult(bundle: Bundle): ContactItem? {
            return bundle.getContact()
        }

        private fun Bundle.getContact(): ContactItem? {
            val id = getInt(CONTACT_ID)
            val name = getString(CONTACT_NAME)
            val surname = getString(CONTACT_SURNAME)
            val number = getString(CONTACT_PHONE_NUMBER)

            if (name != null && surname != null && number != null) {
                return ContactItem(id, name, surname, number)
            }
            return null
        }

        private fun ContactItem.toBundle(): Bundle = bundleOf(
            CONTACT_ID to id,
            CONTACT_NAME to name,
            CONTACT_SURNAME to surname,
            CONTACT_PHONE_NUMBER to number
        )

        fun newInstance(action: ContactEditorAction): ContactEditorDialog {
            return ContactEditorDialog().apply {
                arguments = when (action) {
                    is ContactEditorAction.ContactEdit -> {
                        bundleOf(
                            DIALOG_ACTION to EDIT_CONTACT,
                            CURRENT_CONTACT to action.contact.toBundle()
                        )
                    }

                    is ContactEditorAction.ContactCreate -> {
                        bundleOf(
                            DIALOG_ACTION to CREATE_CONTACT,
                            CURRENT_CONTACT to null
                        )
                    }
                }
            }
        }

        const val TAG = "ContactEditorDialog"
    }
}