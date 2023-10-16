package com.example.gp.list

import android.app.Application
import androidx.lifecycle.*
import com.example.gp.login.LoginViewModel
import com.example.gp.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Disease>>
    val repository: ListRepository

    init {
        repository = ListRepository(application)
        readAllData = repository.readAllData
    }

    fun addDiseases(list: ArrayList<Disease>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllDiseases(list)
        }
    }

    fun searchDisease(searchQuery: String): LiveData<List<Disease>> {
            return repository.searchDisease(searchQuery)
    }

    private val _navigateToSelectedProperty = MutableLiveData<Disease?>()

    val navigateToSelectedProperty: MutableLiveData<Disease?>
        get() = _navigateToSelectedProperty

    fun displayPropertyDetails(disease: Disease) {
        _navigateToSelectedProperty.value = disease
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}