package hr.ferit.igorradic.reversechordy;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Databasehelper extends SQLiteOpenHelper {

    public static final String TAG = Databasehelper.class.getSimpleName();
    static String DB_NAME = "MojaBaza.db";
    private final Context myContext;
    String outFileName = "";
    private String DB_PATH;
    private SQLiteDatabase db;

    public Databasehelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        ContextWrapper cw = new ContextWrapper(context);
        DB_PATH = cw.getFilesDir().getAbsolutePath() + "/databases/";
        Log.e(TAG, "Databasehelper: DB_PATH " + DB_PATH);
        outFileName = DB_PATH + DB_NAME;
        File file = new File(DB_PATH);
        Log.e(TAG, "Databasehelper: " + file.exists());
        if (!file.exists()) {
            file.mkdir();
        }
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //nista ne radi jer baza postoji vec
        } else {
            //stvori praznu bazu i kopira moju u nju
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            try {
                copyDataBase();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

    private void copyDataBase() throws IOException {

        Log.i("Database",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;

        InputStream myInput = null;
        try {
            myInput = myContext.getAssets().open(DB_NAME);


            myOutput = new FileOutputStream(DB_PATH + DB_NAME);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "New database has been copied to device!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataBase() throws SQLException {

        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.e(TAG, "openDataBase: Open " + db.isOpen());
    }

    public String SearchForChord(String entry){

        String TABLE_NAME = "akordi";
        String[] columns = {"ime_akorda"};
        String where;
        String[] condition;


        entry = entry.trim(); // za izbacivanje nepotrebnih space-ova

        entry = entry.toUpperCase(); //prebaci ih sve u velika slova
        entry = entry.replace("BB", "A#"); //zamjeni zbog base podataka (da B ne bi bio u Bb)

        String entryNoDuplicate; // varijabla
        entryNoDuplicate = RemoveDuplicateTones(entry); // obrise sve ponavljajuce



        String buffer = entryNoDuplicate; // za provjeru broja spaceova, ako je jedan ili dva nema rezultata

        int spaceCount = 0;

        for (char c : buffer.toCharArray()) {   // brojanje spaceova, ako ima manje spaceova srusi se app
            if (c == ' ') {
                spaceCount++;
            }
        }

        if(spaceCount < 2){
            return "No result.";
        }



        condition = entryNoDuplicate.split("\\s+"); //razdvoji svaki ton u posebno polje u string polju

        if(condition[1] == null){
            return "No result.";
        }
        else if(condition[2] == null){
            return "No result.";
        }

        condition[0] = "%" + condition[0] + " %";
        condition[1] = "%" + condition[1] + " %";
        condition[2] = "%" + condition[2] + " %";


        if(condition.length > 4){
            return "Error.";
        }
        else if(condition.length == 3) {
            where = "tonovi LIKE ? AND tonovi LIKE ? AND tonovi LIKE ?";
        }
        else{
            where = "tonovi LIKE ? AND tonovi LIKE ? AND tonovi LIKE ? AND tonovi LIKE ?";
            condition[3] = "%" + condition[3] + " %";
        }


        Cursor c = db.query(TABLE_NAME, columns, where, condition, null, null, null);

        if(c.moveToNext()){
            String a = c.getString(0);
            c.close();
            return a;
        }
        return "No result.";
    }

    public String RemoveDuplicateTones(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split(" "))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", " ");
    }


    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase arg0) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}