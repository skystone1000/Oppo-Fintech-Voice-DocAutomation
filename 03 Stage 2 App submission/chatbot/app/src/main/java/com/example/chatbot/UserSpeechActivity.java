package com.example.chatbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class UserSpeechActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    private static int count = 0;
    public EditText name;
    public EditText age;
    public EditText income;
    public DatabaseReference databaseArtists;
    public static String username;
    public static String userage;

    public static String userdob;
    public static String userincome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userspeech);

        //getting the reference of artists node

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        income = (EditText) findViewById(R.id.income);
        //list to store artists
        List artists = new ArrayList<>();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(UserSpeechActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(UserSpeechActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(UserSpeechActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                else {
                    // Permission has already been granted
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    speechRecog.startListening(intent);

                }
                Log.d("tag", "onclick listening to speaker");

            }
        });
        Log.d("tag", "on create");


        initializeTextToSpeech();
        initializeSpeechRecognizer();


    }


    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0) {
                    makeText(UserSpeechActivity.this, getString(R.string.tts_no_engines), LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.US);
                    speak("Hello there, Lets start filling our form, i will be asking questions, please provide the answers, ,first question, is what is your name ?");
                }
            }
        });
        Log.d("tag", "initialize text to speech");
    }


    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result_arr.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
            Log.d("tag", "initializeSpeechRecognizer");
        }
    }


    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();
        Log.d("tag", "on process results");
//        Handle at least four sample cases

//        First: What is your Name?
//        Second: What is the time?
//        Third: Is the earth flat or a sphere?
//        Fourth: Open a browser and open url
        if (count == 0) {

            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("name") != -1) {

                    speak("okay next question is.");
                    count = count + 1;
                    username = result_message;
                    Log.d("this is my array", "arr: " + result_message);
                    speak("what is your age in years now");
                    Log.d("tag", "username" + username);
                }
            }
            Log.d("tag", "count set to 1");
        }
        if (count == 1) {
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("age") != -1) {
                    speak("okay next question is.");
                    count = count + 1;
                    userage = result_message;
                    speak("what is your date of birth now");
                }
            }
        }
        if (count == 2) {
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("date of birth") != -1) {
                    speak("okay next question is");
                    count = count + 1;
                    userdob = result_message;
                    speak("what is your annual income");
                }
            }
        }
        if (count == 3) {
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("income") != -1) {
                    userincome = result_message;
                    speak("thank you for your response");
                    count = 0;

                }
            }
        }


    }


    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
        Log.d("tag", "speaking");
    }

    @Override
    protected void onPause() {
        speak("please give the answer for the question");
        tts.shutdown();
        addArtist();
        super.onPause();

        username = NULL;
        userage = NULL;
        userdob = NULL;
        userincome = NULL;
        Log.d("tag", "on pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser

        Log.d("tag", "onResume");

    }

    /*
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addArtist() {
        //checking if the value is provided
        if (!TextUtils.isEmpty(username)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            Artist artist = new Artist(id, username, userage, userdob, userincome);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again


            //displaying a success toast
            Toast.makeText(this, "NEW USER IS CREATED", Toast.LENGTH_LONG).show();
        }
        else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a information for the loan through voice input", Toast.LENGTH_LONG).show();
        }
        Log.d("tag", "firebaseuseraddition");
    }

}





