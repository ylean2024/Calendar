package com.example.android.calendar.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pizzadelivery.model.api.retrofit.Fetch
import com.example.android.calendar.db.Repository
import com.example.android.calendar.db.event.EventDb
import com.example.android.calendar.db.friend.FriendDb
import com.example.android.calendar.db.user.UserDb
import com.example.android.calendar.model.Article
import com.example.android.calendar.model.Event
import com.example.android.calendar.model.Friend
import com.example.android.calendar.utils.EventConverter
import com.example.android.calendar.utils.EventScheduler
import com.example.android.calendar.utils.FriendConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


class CalendarViewModel : ViewModel() {
    private val repository: Repository = Repository.get()
    private val _newsItemsLiveData: MutableLiveData<List<Article>> = MutableLiveData()
    val newsItemsLiveData: LiveData<List<Article>> = _newsItemsLiveData

    private val _eventsLiveData: MutableLiveData<List<Event>> = MutableLiveData()
    val eventsLiveData: LiveData<List<Event>> = _eventsLiveData

    private val _userLiveData: MutableLiveData<UserDb?> = MutableLiveData()
    val userLiveData: LiveData<UserDb?> = _userLiveData

    private val _friendsLiveData: MutableLiveData<List<Friend>> = MutableLiveData()
    val friendsLiveData: LiveData<List<Friend>> = _friendsLiveData

    var wasNavigateToLogin: Boolean = false
    init {
        _newsItemsLiveData.value = mutableListOf()
        fetchNews()
        _userLiveData.value = null
    }

    fun getUser(email: String){
        viewModelScope.launch {
            wasNavigateToLogin = true
            _userLiveData.value = repository.getUserByEmail(email)
        }
    }

    fun fetchNews(){
        viewModelScope.launch {
            Fetch().fetchNews().observeForever { newsResponse ->
                _newsItemsLiveData.value = newsResponse
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            if(_userLiveData.value != null){
                try {
                    val userId = _userLiveData.value!!.id
                    repository.insertEvent(EventConverter.modelEventToDbEvent(event, userId))
                    loadEventsForDate(event.date, event.idUser)
                } catch (e: Exception){
                    println(e)
                }
            }

        }
    }

    fun loadEventsForDate(date: String, userId1: Int) {
        viewModelScope.launch {
            if(_userLiveData.value != null) {
                val userId = _userLiveData.value!!.id
                val userEvents  = repository.getEventsByDateAndUserId(userId, date)

                val friends  = repository.getFriendsForUser(userId1)

                val friendEvents = mutableListOf<EventDb>()
                for (friend in friends) {
                    val friendId = if (friend.userId1 == userId) friend.userId2 else friend.userId1
                    val friendEventsForDate = repository.getEventsByDateAndUserId(friendId, date)
                    friendEvents.addAll(friendEventsForDate)
                }

                val combinedEvents = (userEvents + friendEvents).distinctBy { it.id }
                if (combinedEvents != null) {
                    _eventsLiveData.value = EventConverter.dbEventToModelEvent(combinedEvents)
                }
            }
        }
    }

    fun addFriend(userId1: Int, userId2: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
//            val checkFriends = repository.getFriendsForUser(userId1)
//            if(checkFriends.isNotEmpty()){
//                callback(false)
//                return@launch
//            }
            repository.addFriend(FriendDb(userId1 = userId1, userId2 = userId2))
            callback(true)
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            if(_userLiveData.value != null) {
                val userId = _userLiveData.value!!.id
                val allUsers = repository.getAllUsers(userId)
                _friendsLiveData.value = FriendConverter.fromUserDbList(allUsers)
            }
        }
    }

    fun updateShowToAllForUserEvents(showToAll: Boolean) {
        viewModelScope.launch {
            _userLiveData.value?.let { user ->
                repository.updateShowToAllForUserEvents(user.id, showToAll)
            }
        }
    }

    fun updateUserShowToAllFriends(showToAllFriends: Boolean) {
        viewModelScope.launch {
            _userLiveData.value?.let { user ->
                user.showToAllFriends = showToAllFriends
                repository.updateUserShowToAllFriends(user.id, showToAllFriends)
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            try {
                val userId = _userLiveData.value?.id
                if (userId != null && userId == event.idUser) {
                    repository.deleteEventById(event.id)
                    loadEventsForDate(event.date, userId)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun scheduleEventNotification(event: Event, context: Context) {
        val eventTime = "${event.date} ${event.startTime}"
        EventScheduler.scheduleEvent(context, event.id, event.name, event.description, eventTime)
    }
}