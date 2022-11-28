package ru.yandex.repinanr.movies.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.data.Const.TAG_SAVE_DIALOG
import ru.yandex.repinanr.movies.databinding.SaveDialogFragmentBinding
import ru.yandex.repinanr.movies.presentation.ViewModelFactory
import javax.inject.Inject

class SaveDataDialog @Inject constructor(): DialogFragment() {
    private val args by navArgs<SaveDataDialogArgs>()

    @Inject
    lateinit var viewModel: SaveDialogViewModel

    lateinit var saveDialogFragmentBinding: SaveDialogFragmentBinding
    internal lateinit var listener: SaveDataDialogListener

    private val component by lazy {
        (requireActivity().application as App).component
    }

    interface SaveDataDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        saveDialogFragmentBinding = SaveDialogFragmentBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(saveDialogFragmentBinding.root)
            .create()

        viewModel = ViewModelProvider(this)
            .get(SaveDialogViewModel::class.java)

        with(saveDialogFragmentBinding) {
            dialogYesButton.setOnClickListener {
                listener.onDialogPositiveClick(this@SaveDataDialog)
            }

            dialogNoButton.setOnClickListener {
                listener.onDialogNegativeClick(this@SaveDataDialog)
            }

            dialogCancelButton.setOnClickListener {
                dialog.dismiss()
                Log.d(TAG_SAVE_DIALOG, "dialogCancelListener")
            }
        }

        return dialog
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is SaveDataDialogListener) {
            listener = context
        } else {
            throw RuntimeException("Activity must implements interface OnBackPressedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    fun updateMovie() {
        viewModel.addDeleteFavoriteMovieDB(args.movie, args.oldIsFavorite)
        viewModel.saveMovieComment(args.movie.movieId, args.comment)
    }
}
