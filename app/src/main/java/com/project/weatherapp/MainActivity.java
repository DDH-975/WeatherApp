package com.project.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button searchBtn;
    private AutoCompleteTextView autoCompleteTextView;

    public void IntentPut(String str, Intent intent) {
        if (str.equals("서울특별시")||str.equals("서울")) {
            intent.putExtra("location", "서울특별시");
            intent.putExtra("nx", 60);
            intent.putExtra("ny", 127);
            Log.d("인텐트 전송", "서울특별시 전송");

        } else if (str.equals("부산광역시")||str.equals("부산")) {
            intent.putExtra("location", "부산광역시");
            intent.putExtra("nx", 98);
            intent.putExtra("ny", 76);

        } else if (str.equals("대구광역시")||str.equals("대구")) {
            intent.putExtra("location", "대구광역시");
            intent.putExtra("nx", 89);
            intent.putExtra("ny", 90);

        } else if (str.equals("인천광역시")||str.equals("인천")) {
            intent.putExtra("location", "인천광역시");
            intent.putExtra("nx", 55);
            intent.putExtra("ny", 124);

        } else if (str.equals("광주광역시")||str.equals("광주")) {
            intent.putExtra("location", "광주광역시");
            intent.putExtra("nx", 58);
            intent.putExtra("ny", 74);

        } else if (str.equals("대전광역시")||str.equals("대전")) {
            intent.putExtra("location", "대전광역시");
            intent.putExtra("nx", 67);
            intent.putExtra("ny", 100);

        } else if (str.equals("울산광역시")||str.equals("울산")) {
            intent.putExtra("location", "울산광역시");
            intent.putExtra("nx", 102);
            intent.putExtra("ny", 84);

        } else if (str.equals("제주특별자치도")||str.equals("제주도")||str.equals("제주")) {
            intent.putExtra("location", "제주특별자치도");
            intent.putExtra("nx", 52);
            intent.putExtra("ny", 38);

        } else {
            Toast.makeText(getApplicationContext(), "유효하지 않은 입력입니다.", Toast.LENGTH_SHORT).show();
        }

        Log.d("인텐트 전송", "인텐트 전송완료");
        Log.d("str : ", str);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchBtn = (Button) findViewById(R.id.searchButton);
//        editTextCity = (EditText) findViewById(R.id.editTextCity);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.ACTextView);
        String[] suggestions = new String[]{
                "서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "제주특별자치도"
        };

        // ArrayAdapter를 사용하여 자동완성 데이터를 제공
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, suggestions);

        // AutoCompleteTextView에 어댑터 설정
        autoCompleteTextView.setAdapter(adapter);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = autoCompleteTextView.getText().toString();
                String noSpaces = str.replaceAll("\\s", "");

                Intent intent = new Intent(MainActivity.this, ShowWeather.class);
                IntentPut(noSpaces, intent);
                startActivity(intent);

            }
        });

    }

}