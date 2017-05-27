package com.petarmarijanovic.parkingspot

import android.arch.lifecycle.LifecycleActivity
import android.arch.persistence.room.*
import android.os.Bundle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MainActivity : LifecycleActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    
    val userDao = ParkingSpotApplication.Companion.database!!.userDao()
    
    
    userDao.all.subscribeOn(Schedulers.io())
        .subscribe({ it.listIterator().forEach { Log.d("Petarr", it.toString()) } },
                   { Log.d("Petarr Error", it.toString()) })
    
    Observable.just("a")
        .subscribeOn(Schedulers.io())
        .doOnEach { userDao.insertAll(User(firstName = "Petar", lastName = "Marijanovic")) }
        .subscribe({ Log.d("Petarr", "Saved") },
                   { Log.d("Petarr SError", it.toString()) })
    
  }
  
}

@Entity
data class User(@PrimaryKey(autoGenerate = true) var uid: Int = 0,
                @ColumnInfo(name = "first_name") var firstName: String = "",
                @ColumnInfo(name = "last_name") var lastName: String = "")

@Dao
interface UserDao {
  @get:Query("SELECT * FROM user")
  val all: Flowable<List<User>>
  
  // TODO LiveData
  
  //  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
  //  fun loadAllByIds(userIds: IntArray): List<User>
  //
  //  @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
  //  fun findByName(first: String, last: String): User
  
  @Insert
  fun insertAll(vararg users: User)
  
  @Delete
  fun delete(user: User)
}

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
}
