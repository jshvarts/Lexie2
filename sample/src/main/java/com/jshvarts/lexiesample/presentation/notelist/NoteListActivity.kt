package com.jshvarts.lexiesample.presentation.notelist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.jshvarts.lexiesample.R
import com.jshvarts.lexiesample.domain.Note
import com.jshvarts.lexiesample.domain.NoteListUseCase
import com.jshvarts.lexiesample.presentation.notedetail.EXTRA_DETAIL_ID
import com.jshvarts.lexiesample.presentation.notedetail.NoteDetailActivity
import kotlinx.android.synthetic.main.note_list.*

class NoteListActivity : AppCompatActivity() {

    private lateinit var viewModel: NoteListViewModel

    private val clickListener: ClickListener = this::onNoteClicked

    private val recyclerViewAdapter = NoteAdapter(clickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_list)

        setupRecyclerView()

        // Normally the Use Case should be injected into NoteListViewModelFactory instead
        viewModel = ViewModelProviders.of(this, NoteListViewModelFactory(null, NoteListUseCase()))
                .get(NoteListViewModel::class.java)

        viewModel.observableState.observe(this@NoteListActivity, Observer { state ->
            state?.let { renderState(state) }
        })

        if (savedInstanceState == null) {
            viewModel.dispatch(Action.LoadNotes)
        }
    }

    private fun renderState(state: State) {
        with(state) {
            when {
                isLoading -> renderLoadingState()
                isError -> renderErrorState()
                else -> renderNotesState(notes)
            }
        }
    }

    private fun renderLoadingState() {
        loadingIndicator.visibility = View.VISIBLE
    }

    private fun renderErrorState() {
        loadingIndicator.visibility = View.GONE
        Toast.makeText(this, R.string.error_loading_notes, Toast.LENGTH_LONG).show()
    }

    private fun renderNotesState(notes: List<Note>) {
        loadingIndicator.visibility = View.GONE
        recyclerViewAdapter.updateNotes(notes)
        notesRecyclerView.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = recyclerViewAdapter
        notesRecyclerView.setHasFixedSize(true)
    }

    private fun onNoteClicked(note: Note) {
        val intent = Intent(this, NoteDetailActivity::class.java).apply {
            putExtra(EXTRA_DETAIL_ID, note.id)
        }
        startActivity(intent)
    }
}
