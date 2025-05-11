import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.Album
import com.example.myapplication.AlbumDao

@Database(entities = [Album::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
