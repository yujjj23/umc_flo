import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.Album
import com.example.myapplication.AlbumDao
import com.example.myapplication.Like
import com.example.myapplication.LikeDao

//@Database(entities = [Album::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun albumDao(): AlbumDao
//}

//@Database(entities = [Album::class, Like ::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun albumDao(): AlbumDao
//    companion object {
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "album_database"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
