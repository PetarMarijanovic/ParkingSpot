package com.petarmarijanovic.parkingspot

import android.app.Application
import android.arch.persistence.room.Room
import com.facebook.stetho.Stetho

/** Created by petar on 25/05/2017. */
class ParkingSpotApplication : Application() {
  
  companion object {
    var database: AppDatabase? = null
  }
  
  override fun onCreate() {
    super.onCreate()
    
    Stetho.initializeWithDefaults(this)
    
    ParkingSpotApplication.database =
        Room.databaseBuilder(this, AppDatabase::class.java, "parking_spot_db").build()
  }
}