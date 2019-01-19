package uz.shukurov.izohlilugat.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import uz.shukurov.izohlilugat.R;


/**
 * Created by dds86 on 12-Nov-17.
 */

public class CreateDatabase {

    public void createDatabaseFirstTimeAppLaunch(Context context) {
        final String LOG_TAG = "autolog";
        DBHelper dbHelper;

        String stringCyrillic   = "А<>а<>Ә<>ә<>Б<>б<>В<>в<>Г<>г<>Ғ<>ғ<>Д<>д<>Е<>е<>Ё<>ё<>Ж<>ж<>З<>з<>И<>и<>Й<>й<>К<>к<>Қ<>қ<>Л<>л<>М<>м<>Н<>н<>Ң<>ң<>О<>о<>Ө<>ө<>П<>п<>Р<>р<>С<>с<>Т<>т<>У<>у<>У<>у<>Ү<>ү<>Ф<>ф<>Ҳ<>ҳ<>Һ<>һ<>Ц<>ц<>Ч<>ч<>Ш<>ш<>Щ<>щ<>Ъ<>ъ<>Й<>й<>И<>и<>Ь<>ь<>Э<>э<>Ю<>ю<>Я<>я<>Ə<>ə";
        String stringSaebiz     = "A<>a<>Ae<>ae<>B<>b<>V<>v<>G<>g<>Gh<>gh<>D<>d<>E<>e<>E<>e<>Zh<>zh<>Z<>z<>I<>i<>J<>j<>K<>k<>Q<>q<>L<>l<>M<>m<>N<>n<>Ng<>ng<>O<>o<>Oe<>oe<>P<>p<>R<>r<>S<>s<>T<>t<>W<>w<>U<>u<>Ue<>ue<>F<>f<>H<>h<>H<>h<>C<>c<>Ch<>ch<>Sh<>sh<>Sh<>sh<><><>Y<>y<>I<>i<><><>E<>e<>Iy<>iy<>Ia<>ia<>Ae<>ae";
        String stringLatin      = "A<>a<>A'<>a'<>B<>b<>V<>v<>G<>g<>G'<>g'<>D<>d<>E<>e<>I'o<>i'o<>J<>j<>Z<>z<>I'<>i'<>I'<>i'<>K<>k<>Q<>q<>L<>l<>M<>m<>N<>n<>N'<>n'<>O<>o<>O'<>o'<>P<>p<>R<>r<>S<>s<>T<>t<>Y'<>y'<>U<>u<>U'<>u'<>F<>f<>H<>h<>H<>h<>C<>c<>C'<>c'<>S'<>s'<>S's'<>S's'<><><>Y<>y<>I<>i<><><>E<>e<>I'y'<>i'y'<>I'a<>i'a<>A'<>a'";
        String stringDiacritic  = "A<>a<>À<>à<>B<>b<>V<>v<>G<>g<>Ǵ<>ǵ<>D<>d<>E<>e<>È<>è<>J<>j<>Z<>z<>Í<>í<>Í<>í<>K<>k<>Ḱ<>ḱ<>L<>l<>M<>m<>N<>n<>Ń<>ń<>O<>o<>Ó<>ó<>P<>p<>R<>r<>S<>s<>T<>t<>Ý<>ý<>U<>u<>Ú<>ú<>F<>f<>H<>h<>H<>h<>C<>c<>Ć<>ć<>Ś<>ś<>Ś<>ś<><><>Y<>y<>I<>i<><><>E<>e<>Íý<>íý<>Íа<>íа<>À<>à";

        String[] arrayCyrillic = stringCyrillic.split("<>");
        String[] arraySaebiz = stringSaebiz.split("<>");
        String[] arrayLatin = stringLatin.split("<>");
        String[] arrayDiacritics = stringDiacritic.split("<>");


        dbHelper = new DBHelper(context);

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // проверка существования записей

        // создаем объект для данных
        ContentValues cv = new ContentValues();


        Log.d(LOG_TAG, "--- Insert in mytable: ---");
        for (int i = 0; i < arrayCyrillic.length; i++) {
            cv.put("cyrillic", arrayCyrillic[i]);
            cv.put("latin", arrayLatin[i]);
            cv.put("saebiz", arraySaebiz[i]);
            cv.put("diacritic", arrayDiacritics[i]);
            db.insert(context.getString(R.string.mytable), null, cv);

        }

        dbHelper.close();

    }
}
