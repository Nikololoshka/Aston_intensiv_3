package dev.aston.intensiv.nikolay

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.aston.intensiv.nikolay.databinding.ActivityMainBinding
import dev.aston.intensiv.nikolay.model.ContactItem
import dev.aston.intensiv.nikolay.model.ContactSupplyer
import dev.aston.intensiv.nikolay.ui.changeVisibility
import dev.aston.intensiv.nikolay.ui.editor.ContactEditorAction
import dev.aston.intensiv.nikolay.ui.editor.ContactEditorDialog
import dev.aston.intensiv.nikolay.ui.list.ContactAdapter
import dev.aston.intensiv.nikolay.ui.list.ContactAdapterListener
import dev.aston.intensiv.nikolay.ui.list.MultiItemSelectionTracker
import dev.aston.intensiv.nikolay.ui.list.ReorderItemHelper
import dev.aston.intensiv.nikolay.ui.list.delegate.ContactDelegationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ContactAdapterListener {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val selectTracker by lazy {
        MultiItemSelectionTracker("contact_selector")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            selectTracker.onRestoreInstanceState(savedInstanceState)
        } else {
            val contacts = ContactSupplyer.loadContacts(this)
            viewModel.loadContacts(contacts)
        }

        val adapter = ContactDelegationAdapter(selectTracker, this)
        selectTracker.registerObserver(adapter)
        binding.contactsList.adapter = adapter

        val reorderItemHelper = ReorderItemHelper(adapter)
        reorderItemHelper.attachToRecyclerView(binding.contactsList)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.contactState.collectLatest { state ->
                    adapter.submitList(state.contacts)

                    showDeleteModeUI(show = state.isDeleteContactMode)
                    selectTracker.isSelectionActive = state.isDeleteContactMode
                    reorderItemHelper.enableReorder(!state.isDeleteContactMode)
                }
            }
        }

        binding.addContact.setOnClickListener { addContactClicked() }

        supportFragmentManager.setFragmentResultListener(
            ContactEditorDialog.CONTACT_EDITOR_REQUEST,
            this,
            this::onContactEditorResult
        )

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_contact -> {
                    viewModel.enableDeleteContactMode(true)
                    true
                }

                else -> false
            }
        }

        binding.cancelMode.setOnClickListener {
            viewModel.enableDeleteContactMode(false)
        }
        binding.deleteContacts.setOnClickListener {
            viewModel.deleteContacts(selectTracker.currentSelected)
            viewModel.enableDeleteContactMode(false)
        }
    }

    private fun onContactEditorResult(requestKey: String, result: Bundle) {
        val contact = ContactEditorDialog.parseResult(result)
        if (contact != null) {
            viewModel.updateContact(contact)
        }
    }

    override fun onItemClicked(item: ContactItem) {
        val action = ContactEditorAction.ContactEdit(item)
        startContactEditor(action)
    }

    override fun onItemMoved(from: Int, to: Int) {
        viewModel.moveContactItem(from, to)
    }

    private fun addContactClicked() {
        val action = ContactEditorAction.ContactCreate
        startContactEditor(action)
    }

    private fun startContactEditor(action: ContactEditorAction) {
        val dialog = ContactEditorDialog.newInstance(action)
        dialog.show(supportFragmentManager, ContactEditorDialog.TAG)
    }

    private fun showDeleteModeUI(show: Boolean) {
        binding.addContact.changeVisibility(isVisible = !show)

        binding.cancelMode.changeVisibility(isVisible = show)
        binding.deleteContacts.changeVisibility(isVisible = show)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectTracker.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        selectTracker.unregisterObserver()
    }
}