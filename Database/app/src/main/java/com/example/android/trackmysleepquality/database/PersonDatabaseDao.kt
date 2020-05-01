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

package com.example.android.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PersonDatabaseDao {

    @Insert
    fun insert(person: Person)

    @Update
    fun update(person: Person)

    @Query("SELECT * from workout_Angles WHERE personId = :key")
    fun get(key: Long): Person

    @Query("DELETE FROM workout_Angles")
    fun clear()

    @Query("SELECT * from workout_Angles")
    fun getAllPersons(): LiveData<List<Person>>

    @Query("SELECT * from workout_Angles ORDER BY personId Limit 1")
    fun getLivePerson(): Person?

}


