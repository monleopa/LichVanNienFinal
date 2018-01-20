package com.example.user.calendarlunar;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class CalendarLunar extends AppCompatActivity {

    private static TextView txtMonth, txtDate, txtTG, txtContent,txtDay,txtNamAm, txtNam, txtNgay, txtThang2,txtThangAm;
    private static ImageButton btnWeather, btnChiTiet, btnLich, btnDoiNgay, btnCongViec;

    public static String[] can = new String[]{
            "Giáp","Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý"
    };
    public static String[] chi = new String[]{
            "Tý","Sửu","Dần","Mão","Thìn","Tỵ","Ngọ","Mùi","Thân","Dậu","Tuất","Hợi"
    };

//    public static String[] thuHienThi = new String[]{
//            "Thứ Hai","Thứ Ba","Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy", "Chủ Nhật"
//    };

    private String DATABASE_NAME = "dbChiTiet.sqlite";
    private String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase database3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_lunar);
        intit();

        Calendar calendar = Calendar.getInstance();

        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int thu = calendar.get(Calendar.DAY_OF_WEEK);

        txtDay.setText(thuHienThi(thu));
        hienthiNgayThang(day,month,year);
        docDB();
        showdanhngon();

        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarLunar.this,Weather.class);
                startActivity(intent);
            }
        });

        btnDoiNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CalendarLunar.this);
                dialog.setTitle("Chọn ngày muốn xem");
                dialog.setCancelable(false); // không cho click ra ngoài hộp thoại
                dialog.setContentView(R.layout.changedate);

                final DatePicker datePicker2 = dialog.findViewById(R.id.datePicker2);
                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                Button btnXem = dialog.findViewById(R.id.btnXem);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                btnXem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int ngay2 = datePicker2.getDayOfMonth();
                        int thang2 = datePicker2.getMonth()+1;
                        int nam2 = datePicker2.getYear();

                        System.out.println(ngay2 + " " + thang2 + " " + nam2);

                        Calendar calendar1 = new GregorianCalendar(nam2,thang2-1,ngay2);
                        int thu2 = calendar1.get(Calendar.DAY_OF_WEEK);
                        txtDay.setText(thuHienThi(thu2));
                        hienthiNgayThang(ngay2,thang2,nam2);
                        showdanhngon();

                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        btnChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarLunar.this, ChiTiet.class);
                intent.putExtra("ngay",txtDate.getText());
                intent.putExtra("thangnam",txtMonth.getText());
                startActivity(intent);
            }
        });

        btnCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarLunar.this, CongViec.class);
                startActivity(intent);
            }
        });

    }

    private String thuHienThi(int i){
        String s = "";
        switch (i){
            case Calendar.MONDAY: s = "Thứ Hai"; break;
            case Calendar.TUESDAY: s = "Thứ Ba"; break;
            case Calendar.WEDNESDAY: s = "Thứ Tư"; break;
            case Calendar.THURSDAY: s = "Thứ Năm"; break;
            case Calendar.FRIDAY: s = "Thứ Sáu"; break;
            case Calendar.SATURDAY: s = "Thứ Bảy"; break;
            case Calendar.SUNDAY: s = "Chủ Nhật"; break;

        }

        return s;
    }

    private void showdanhngon() {
            database3 = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
            Cursor cursor3 = database3.query("danhngon",null,null,null,null,null,null);
            Random rd = new Random();
            int idrand = rd.nextInt(59) + 1;
            while(cursor3.moveToNext()){
                int id = cursor3.getInt(0);
                if (id == idrand){
                    String dn = cursor3.getString(1);
                    String tg = cursor3.getString(2);
                    txtContent.setText(dn);
                    txtTG.setText("("+tg+")");
                }
            }
            cursor3.close();
    }

    private void hienthiNgayThang(int day, int month, int year){
        String month2 = String.valueOf(month);
        String day2 = String.valueOf(day);
        String year2 = String.valueOf(year);

        txtMonth.setText("Tháng: "+month2+"/"+year2);
        txtDate.setText(day2);

        convertSolar2Lunar(day,month,year);
    }
    private void intit() {
        txtMonth = findViewById(R.id.txtMonth);
        txtDate = findViewById(R.id.txtDate);
        txtContent = findViewById(R.id.txtContent);
        txtTG = findViewById(R.id.txtTG);
        txtDay = findViewById(R.id.txtDay);
        txtNamAm = findViewById(R.id.txtNamAm);
        txtNam = findViewById(R.id.txtNam);
        txtNgay = findViewById(R.id.txtNgay);
        txtThang2 = findViewById(R.id.txtThang2);
        txtThangAm = findViewById(R.id.txtThangAm);
        btnWeather = findViewById(R.id.btnWeather);
        btnChiTiet = findViewById(R.id.btnChiTiet);
        btnLich = findViewById(R.id.btnLich);
        btnDoiNgay = findViewById(R.id.btnDoiNgay);
        btnCongViec = findViewById(R.id.btnCongViec);
    }
    private static long getJulius(int ngay, int thang, int nam){
        int a, y, m, jd;

        a = (14 - thang) / 12;
        y = nam + 4800 - a;
        m = thang + 12 * a - 3;

        jd = ngay + ((153 * m + 2) / 5) + 365 * y + (y / 4)  - (y / 100) + (y / 400) - 32045;

        if (jd < 2299161)
        {
            jd = ngay + ((153 * m + 2) / 5) + 365 * y + (y / 4)  - 32083;
        }
        return jd;
    }
    private static int getNewMoonDay(int k){
        double PI = 3.14;
        double T, T2, T3, dr, Jd1, M, Mpr, F, C1, deltat, JdNew;
        T = k / 1236.85;
        T2 = T * T;
        T3 = T2 * T;
        dr = PI / 180;
        double timeZone = 7.0;
        Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3;
        // Mean new moon
        Jd1 = Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr);
        // Sun's mean anomaly
        M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3;
        // Moon's mean anomaly
        Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3;
        // Moon's argument of latitude
        F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3;
        C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M);
        C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
        C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
        C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr));
        C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M));
        C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr));
        C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M));

        if (T < -11)
        {
            deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3;
        }
        else
        {
            deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
        }
        JdNew = Jd1 + C1 - deltat;

        return (int)(JdNew + 0.5 + timeZone / 24);
    }
    private static int getSunLongitude(int jdn){
        double timeZone = 7.0;
        double PI = 3.14;
        double T, T2, dr, M, L0, DL, L;
        // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T = (jdn - 2451545.5 - timeZone / 24) / 36525;
        T2 = T * T;
        // degree to radian
        dr = PI / 180;
        // mean anomaly, degree
        M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2;
        // mean longitude, degree
        L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2;
        DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        L = L0 + DL; // true longitude, degree
        L = L * dr;
        // Normalize to (0, 2*PI)
        L = L - PI * 2 * (int)(L / (PI * 2));
        return (int)(L / PI * 6);
    }
    private static int getLunarMonthll(int intNam){
        double k, off, nm, sunLong;
        off = getJulius(31, 12, intNam) - 2415021;
        k = (int)(off / 29.530588853);
        nm = getNewMoonDay((int)k);
        // sun longitude at local midnight
        sunLong = getSunLongitude((int)nm);
        if (sunLong >= 9)
        {
            nm = getNewMoonDay((int)k - 1);
        }
        return (int)nm;
    }
    private static int getLeapMonthOffset(double a11){
        double last, arc;
        int k, i;
        k = (int)((a11 - 2415021.076998695) / 29.530588853 + 0.5);
        last = 0;
        // We start with the month following lunar month 11
        i = 1;
        arc = getSunLongitude((int)getNewMoonDay((int)(k + i)));
        do
        {
            last = arc;
            i++;
            arc = getSunLongitude((int)getNewMoonDay((int)(k + i)));
        } while (arc != last && i < 14);
        return i - 1;
    }
    public static void convertSolar2Lunar(int intNgay, int intThang, int intNam){
        double dayNumber, monthStart, a11, b11, lunarDay, lunarMonth, lunarYear;
        //double lunarLeap;
        int k, diff;
        dayNumber = getJulius(intNgay,intThang,intNam);
        k = (int)((dayNumber - 2415021.076998695) / 29.530588853);
        monthStart = getNewMoonDay(k + 1);
        if (monthStart > dayNumber)
        {
            monthStart = getNewMoonDay(k);
        }

        a11 = getLunarMonthll(intNam);
        b11 = a11;

        if (a11 >= monthStart)
        {
            lunarYear = intNam;
            a11 = getLunarMonthll(intNam - 1);
        }
        else
        {
            lunarYear = intNam + 1;
            b11 = getLunarMonthll(intNam + 1);
        }

        lunarDay = dayNumber - monthStart + 1;
        diff = (int)((monthStart - a11) / 29);
        //lunarLeap = 0;
        lunarMonth = diff + 11;

        if (b11 - a11 > 365)
        {
            int leapMonthDiff = getLeapMonthOffset(a11);
            if (diff >= leapMonthDiff)
            {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff)
                {
                    //lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12)
        {
            lunarMonth = lunarMonth - 12;
        }
        if (lunarMonth >= 11 && diff < 4)
        {
            lunarYear -= 1;
        }
        int Ngay = (int) lunarDay;
        int Thang = (int) lunarMonth;
        int Nam = (int) lunarYear;

        String ngay = String.valueOf(Ngay);
        String thang = String.valueOf(Thang);
        String nam = String.valueOf(Nam);

        String canchiNam = getCanChiYear(Nam);
        String canchiThang = getCanChiMonth(Thang,Nam);


        txtNgay.setText(ngay);
        txtNamAm.setText(nam);
        txtThangAm.setText(thang);
        txtNam.setText(canchiNam);
        txtThang2.setText(canchiThang);

    }
    public static String getCanChiYear(int year) {
        int sodu_can = (year + 6) % 10;
        int sodu_chi = (year + 8) % 12;
        return can[sodu_can] + " " + chi[sodu_chi];
    }
    public static String getCanChiMonth(int thangAm,int namAm){     // tham so truyen vao la ngày tháng năm âm lịch
        int cant=0,chit=0;

        switch (thangAm){
            case 1: case 2: case 3: case 4: case 5:
            case 6: case 7: case 8: case 9: case 10: {
                chit = thangAm + 1;
            }break;
            case 11: case 12:{
                chit = thangAm - 11;
            } break;
        }

        int sodu_can = (namAm + 6) % 10;

        switch (sodu_can){
            case 1:case 6: {
                switch (thangAm){
                    case 1:case 2:case 3:case 4:case 5:case 6:{
                        cant = thangAm + 3;
                    }break;
                    case 7:case 8:case 9:case 10:case 11:case 12:{
                        cant = thangAm - 7;
                    }break;
                }
            }break;

            case 2:case 7:{
                switch (thangAm){
                    case 1:case 2:case 3:case 4:{
                        cant = thangAm + 5;
                    }break;
                    default: cant = thangAm - 5;

                }
            }break;

            case 3:case 8:{
                switch (thangAm){
                    case 1:case 2:{
                        cant = thangAm + 7;
                    }break;
                    case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:case 11:case 12:{
                        cant = thangAm - 3;
                    }break;
                }
            } break;

            case 4:case 9:{
                switch (thangAm){
                    case 11:case 12:{
                        cant = thangAm - 11;
                    }break;
                    default: cant = thangAm - 1;
                }
            }break;

            case 5:case 10:{
                switch (thangAm){
                    case 9:case 10:case 11:case 12:{
                        cant = thangAm - 9;
                    }break;
                    default: cant = thangAm + 1;
                }
            }break;

        }

        return can[cant] + " " + chi[chit];
    }

    private void docDB() {
        File dbfile = getDatabasePath(DATABASE_NAME);
        if(!dbfile.exists()){
            try {
                CopyDBFromAssets();
                // Toast.makeText(this,"thanh cong", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                //  Toast.makeText(this,"that bai", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void CopyDBFromAssets() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layLink();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);

            if(!f.exists()){
                f.mkdir();
            }

            OutputStream myOutput =  new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer)) > 0){
                myOutput.write(buffer, 0 , length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("Loi",e.toString());
        }
    }
    private String layLink(){
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}
