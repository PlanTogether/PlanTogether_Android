package com.example.plantogether.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EventDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: Event)

    @Delete
    fun deleteEvent(event :Event)

    @Update
    fun updateEvent(event :Event)

    @Query("Select * from events where date = :date")
    fun getEventByTitle(date: String): List<Event>

    @Query("Select * from events where date >= :date")
    fun getEventAfterToday(date :String) : List<Event>


    @Query("Select * from events where id = :id")
    fun getEventById(id: Int): Event

    @Query("Select * from events")
    fun getEvents(): List<Event>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlan(plan: Plan)

    @Delete
    fun deletePlan(plan :Plan)
    @Query("Select * from plans where date = :date")
    fun getPlanByTitle(date: String): List<Plan>

    @Query("Select * from plans")
    fun getPlans(): List<Plan>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Query("Select * from users")
    fun getUser(): List<User>

}