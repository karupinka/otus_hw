package ru.yandex.repinanr.movies.presentation.dialog

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import ru.yandex.repinanr.movies.R
import ru.yandex.repinanr.movies.app.App
import ru.yandex.repinanr.movies.databinding.DateDialogFragmentBinding
import ru.yandex.repinanr.movies.services.AlarmReceiver
import java.util.*
import javax.inject.Inject


class DateDialog @Inject constructor() : DialogFragment() {
    private val args by navArgs<DateDialogArgs>()

    private var itemTime: Int = Calendar.SECOND
    lateinit var dateDialogFragmentBinding: DateDialogFragmentBinding


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dateDialogFragmentBinding = DateDialogFragmentBinding.inflate(LayoutInflater.from(context))

        val spinner: Spinner = dateDialogFragmentBinding.spinner
        ArrayAdapter.createFromResource(
            App.instance,
            R.array.time_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                itemTime = when (position) {
                    0 -> Calendar.SECOND
                    1 -> Calendar.MINUTE
                    2 -> Calendar.HOUR
                    else -> Calendar.SECOND
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                itemTime = Calendar.SECOND
            }
        })


        val dialog = AlertDialog.Builder(requireActivity())
            .setNegativeButton(
                R.string.cancel_alert_answer,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
            .setPositiveButton(
                R.string.ok,
                DialogInterface.OnClickListener { dialog, id ->
                    val time = dateDialogFragmentBinding.editTextNumber.text.toString()
                    val timeInt = if (time.isEmpty()) 0 else time.toInt()
                    val calendar = Calendar.getInstance()
                    calendar.add(itemTime, timeInt)
                    val alarmManager =
                        requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
                    val intent = AlarmReceiver.newIntent(requireActivity())
                    intent.putExtra(ID, args.movieId)
                    val pendingIntent = PendingIntent.getBroadcast(
                        requireActivity(),
                        100,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    dialog.dismiss()
                })
            .setView(dateDialogFragmentBinding.root)
            .create()

        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    companion object {
        const val ID = "ID"
    }
}
