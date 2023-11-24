package com.example.cryptocoins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptocoins.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: CryptoAdapter
    private lateinit var data: ArrayList<CrptoModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiData
        data = ArrayList()
        rvAdapter = CryptoAdapter(data)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = rvAdapter

        binding.search.addTextChangedListener (object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val filterData = ArrayList<CrptoModel>()
                for (item in data){
                    if (item.name.lowercase(Locale.getDefault())
                        .contains(p0.toString().lowercase(Locale.getDefault()))){
                        filterData.add(item)
                    }
                }
                if (filterData.isEmpty()){
                    Toast.makeText(this@MainActivity, "No Data Available", Toast.LENGTH_SHORT).show()
                }else{
                    rvAdapter.changeData(filterData = filterData)
                }
            }
        })
    }

    val apiData: Unit
        get() {
            val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
            val queue = Volley.newRequestQueue(this)

            // Creating a JsonObjectRequest
            val jsonObjectRequest = object: JsonObjectRequest(Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val dataArray = response.getJSONArray("data")
                        for (i in 0 until dataArray.length()){
                            val dataObject = dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val quote = dataObject.getJSONObject("quote")
                            val USD = quote.getJSONObject("USD")
                            val price = String.format("$"+"%.2f",USD.getDouble("price"))

                            data.add(CrptoModel(name,symbol, price))
                        }
                        rvAdapter.notifyDataSetChanged()
                    }catch (e:Exception){
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }

                },
                Response.ErrorListener { error ->
                    // Error handling
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-CMC_PRO_API_KEY"] = "c5599a12-1e0f-4606-8790-8fa53079f7d7"
                    return headers
                }
            }

            // Add the request to the RequestQueue
            queue.add(jsonObjectRequest)
        }

}