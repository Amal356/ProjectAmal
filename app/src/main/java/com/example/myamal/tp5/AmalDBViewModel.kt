package com.example.myamal.tp5

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AmalDBViewModel : ViewModel() {

    private var _productsList = MutableStateFlow(emptyList<Product>())
    val productsList = _productsList.asStateFlow()

    fun getProductsList(databaseHelper: DatabaseHelper){
        _productsList.value = databaseHelper.getAllProducts()
    }

}