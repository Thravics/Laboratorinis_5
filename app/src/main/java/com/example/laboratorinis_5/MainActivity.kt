package com.example.laboratorinis_5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.laboratorinis_5.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCurrencyData().start()
    }

    private fun fetchCurrencyData(): Thread
    {
      return Thread {
          val url = URL("https://open.er-api.com/v6/latest/eur")
          val connection = url.openConnection() as HttpsURLConnection

          if(connection.responseCode == 200)
          {
              val inputSystem = connection.inputStream
              val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
              val request = Gson().fromJson(inputStreamReader, Request::class.java)
              updateUI(request)
              inputStreamReader.close()
              inputSystem.close()
          }
          else
          {
            binding.Currency.text = "Failed Connection"
          }

        }
    }

    private fun updateUI(request: Request)
    {
        runOnUiThread {
            kotlin.run{
                binding.lastUpdated.text = request.time_last_update_utc
                binding.usd.text = String.format("USD: %.2f",  request.rates.USD)
                binding.inr.text = String.format("INR: %.2f",  request.rates.INR)
                binding.gbp.text = String.format("GBP: %.2f",  request.rates.GBP)
                binding.rub.text = String.format("RUB: %.2f",  request.rates.RUB)
            }
        }
    }
}