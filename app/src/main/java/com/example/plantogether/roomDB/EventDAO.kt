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
    fun insertEvent(event: Event) : Long

    /*
    @Delete
    fun deleteEvent(event :Event)
*/
    @Query("DELETE FROM events WHERE id = :eventId")
    fun deleteEvent(eventId: Int)

    @Update
    fun updateEvent(event :Event) : Int

    @Query("Select * from events where date = :date")
    fun getEventByTitle(date: String): List<Event>

    @Query("Select * from events where date >= :date")
    fun getEventAfterToday(date : String) : List<Event>

    @Query("Select * from plans where date >= :date")
    fun getPlanAfterToday(date : String) : List<Plan>


    @Query("Select * from events where id = :id")
    fun getEventById(id: Int): Event

    @Query("Select * from plans where id = :id")
    fun getPlanById(id: Int): Plan

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


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotice(notice : Notice)

    @Query("Select * from notices")
    fun getNotice() : List<Notice>


    //현재 시간 기준으로 오늘 일정이 있는
    @Query("Select * from events where date = :date")
    fun getEventToday(date : String) : List<Event>

    @Query("Select * from notices where pid = :eventID")
    fun getNoticeToday(eventID : Int) : List<Notice>



}