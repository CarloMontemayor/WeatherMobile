package com.example.weatherapp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle;
    var search: ImageView? = null
    var tempText: TextView? = null
    var descriptionText: TextView? = null
    var humidityText: TextView? = null
    var locationText: TextView? = null
    var textField: EditText? = null
    var gifImageView: GifImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        search = findViewById(R.id.search);
        tempText = findViewById(R.id.tempText);
        descriptionText = findViewById(R.id.descriptionText);
        humidityText = findViewById(R.id.humidityText);
        locationText = findViewById(R.id.locationText);
        textField = findViewById(R.id.textField);
        gifImageView = findViewById(R.id.gif);

        search?.setOnClickListener(View.OnClickListener { getWeatherData(textField?.text.toString().trim { it <= ' ' }) });

        getWeatherData("Roxas");
        textField?.setHint("Enter City");

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> drawerLayout.closeDrawers();
                R.id.item2 -> Toast.makeText(applicationContext,
                    "Feature will be available soon", Toast.LENGTH_SHORT).show()
                R.id.item3 -> Toast.makeText(applicationContext,
                    "Feature will be available soon", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun getWeatherData(name: String) {
        val apiInterface: ApiInterface = APIClient.getClient().create(ApiInterface::class.java)
        val call = apiInterface.getWeatherData(name)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful()) {

                    try {
                        val result = response.body()?.string();
                        val jsonObject = JSONObject(result);

                        locationText?.text = name.capitalizeWords();
                        tempText?.text = jsonObject.getJSONObject("main").getString("temp") + " \u2103";
                        descriptionText?.text = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description").capitalizeWords();
                        humidityText?.text = jsonObject.getJSONObject("main").getString("temp_max") + " \u00B0" + "/" + jsonObject.getJSONObject("main").getString("temp_min") + " \u00B0";
                        println("--------");
                        println(descriptionText?.text);
                        if (descriptionText?.text!!.contains("rain", true)){
                            playGif("rain");
                        }
                        else if (descriptionText?.text!!.contains("clouds", true)){
                            playGif("clouds");
                        }
                    } catch (e: Exception) {
                        println("567567efsef");
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("123123123123");
            }
        })
    }


    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun playGif(name: String) {
        val fadeout: Animation = AlphaAnimation(1f, 1f)
        fadeout.duration = 2500 // You can modify the duration here
        fadeout.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (name.contains("rain")){
                    gifImageView!!.setBackgroundResource(R.mipmap.rain)
                    println("rain");
                }
                else if (name.contains("clouds")){
                    gifImageView!!.setBackgroundResource(R.mipmap.clouds)
                    println("clouds");
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
        })
        gifImageView!!.startAnimation(fadeout)
    }
}