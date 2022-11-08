package ru.yandex.repinanr.movies.presentation.dialog

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SaveDialogViewModelFactory(val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SaveDialogViewModel(application) as T
    }
}