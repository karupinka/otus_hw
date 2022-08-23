package ru.yandex.repinanr.movies.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import ru.yandex.repinanr.movies.data.Const.TAG_SAVE_DIALOG
import ru.yandex.repinanr.movies.databinding.SaveDialogFragmentBinding

class SaveDataDialog: DialogFragment() {
    lateinit var saveDialogFragmentBinding: SaveDialogFragmentBinding
    internal lateinit var listener: SaveDataDialogListener

    interface SaveDataDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        saveDialogFragmentBinding = SaveDialogFragmentBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(saveDialogFragmentBinding.root)
            .create()

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
        super.onAttach(context)
        try {
            listener = context as SaveDataDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must be SaveDataDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
}
