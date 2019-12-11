package com.example.chatbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class UserSpeechActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userspeech);


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
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    speechRecog.startListening(intent);

                }
                Log.d("tag","onclick listening to speaker");

            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();
        Log.d("tag","on create");

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
                    speak("Hello there, I am ready to start our conversation, i will be asking questions, please provide the answers, first question, is what is your name ?");
                }
            }
        });
        Log.d("tag","initialize text to speech");
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
            Log.d("tag","initializeSpeechRecognizer");
        }
    }


    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();
        Log.d("tag","on process results");
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
                    speak("what is your age in years now");
                }
            }
            Log.d("tag","count set to 1");
        }
        if (count == 1) {
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("age") != -1) {
                    speak("okay next question is.");
                    count = count + 1;
                    speak("what is your date of birth now");
                }
            }
        }
        if (count == 2) {
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("date of birth") != -1) {
                    speak("okay next question is");
                    count = count + 1;
                    speak("what is your annual income");
                }
            }
        }
        if(count ==3){
            if (result_message.indexOf("my") != -1) {
                if (result_message.indexOf("income") != -1) {
                    speak("thank you for your response");
                    count = 0;

                }
            }}


    }


    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
        Log.d("tag","speaking");
    }

    @Override
    protected void onPause() {
        super.onPause();
        speak("please give the answer for the question");
        tts.shutdown();
        Log.d("tag","on pause");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeTextToSpeech();
        initializeSpeechRecognizer();
        Log.d("tag","onResume");

    }


}





