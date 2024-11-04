package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        // The value of a volatile variable is never cached, and all reads and writes are to and from the main memory,
        // which ensures that value of Instance is always up-to-date and is the same for all execution threads.
        // It means changes made by one thread to Instance are immediately visible to all other threads
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            // Use synchronized block to avoid race condition (multiple threads accessing the database
            // at the same time)
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = InventoryDatabase::class.java,
                    name = "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }.also { Instance = it }
        }
    }
}