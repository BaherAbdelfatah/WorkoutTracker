/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.persontracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.Person
import com.example.android.trackmysleepquality.database.PersonDatabaseDao
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class PersonTrackerViewModel(
        val database: PersonDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        private var viewModelJob = Job()

        override fun onCleared(){
            super.onCleared()
            viewModelJob.cancel()
        }


        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        private var toperson = MutableLiveData<Person?>()

        private val persons = database.getAllPersons()




        val personsString = Transformations.map(persons) { persons ->
            formatNights(persons, application.resources)

        }


        init {
            initializeTonight()
        }

        private fun initializeTonight(){
            uiScope.launch {
                toperson.value = getTopersonFromDatabase()
            }
        }
        private suspend fun getTopersonFromDatabase():  Person? {
            return withContext(Dispatchers.IO) {
                var night = database.getLivePerson()

                night

            }

        }


        fun onStartTracking() {
            uiScope.launch {
                val newPerson = Person()
                newPerson.angleOne=15
                newPerson.angleTwo=50
                newPerson.angleThree=100
                insert(newPerson)
                toperson.value = getTopersonFromDatabase()
            }

        }

        //for stop button
        private suspend fun insert(person: Person){
            withContext(Dispatchers.IO) {
                database.insert(person)
            }
        }


        fun onClear() {
            uiScope.launch {
                clear()
                toperson.value = null
            }
        }

        suspend fun clear() {
            withContext(Dispatchers.IO) {
                database.clear()
            }
        }



}

