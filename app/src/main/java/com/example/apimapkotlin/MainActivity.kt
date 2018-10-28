package com.example.apimapkotlin

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.orhanobut.hawk.Hawk
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class MainActivity : AppCompatActivity(),UserDataAdapter.OnItemClickListener {


    var TAG:String= this.javaClass.simpleName
    //RecyclerView setting



    lateinit var recyclerView:RecyclerView
    lateinit var adapter: UserDataAdapter
    lateinit var UserDataList: ArrayList<UserData>


    //Call API
    lateinit var queue: RequestQueue
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Hawk.init(this).build()

        progressDialog = ProgressDialog(this)
        queue = Volley.newRequestQueue(this)

        //RecyclerView setting
        recyclerView = findViewById(R.id.rv1)
        //better performance
        recyclerView.setHasFixedSize(true)
        //grid view
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        //check Internet

        if (isNetworkConnected()) {

            //Online call API or Caching

            if (!GlobalConstants.caching) {
                Log.i(TAG,"With Network , no Cache")
                //Case : Just Enter the Apps -- call API to renew the data
                retrieve_user_dataAPI()
            } else {


                //Case : By Caching during visiting the apps
                if (Hawk.get(GlobalConstants.StoreUserData) as String != null) {

                    //Case : Have Cached Data

                    Log.i(TAG,"With Network , With Cache, With Hawk")
                    try {

                        UserDataList = ArrayList()

                        val response = Hawk.get(GlobalConstants.StoreUserData) as String
                        //array that stores all the object in API Documentation
                        val jsonArray = JSONArray(response)

                        //Loop all the object of the array
                        for (i in 0 until jsonArray.length()) {
                            val userData = jsonArray.getJSONObject(i)


                            //Extrieve what we want by Keys
                            val location = userData.getJSONObject("location")

                            val latitude = location.getDouble("latitude")
                            val longitude = location.getDouble("longitude")

                            val picture = userData.getString("picture")
                            val _id = userData.getString("_id")
                            val name = userData.getString("name")
                            val email = userData.getString("email")

                            UserDataList.add(UserData(latitude, longitude, picture, _id, name, email))
                        }


                        adapter = UserDataAdapter(this, UserDataList)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(this@MainActivity)

                    } catch (e: JSONException) {

                        Log.e(TAG, "Unexpected JSON response from  Hawk")

                        Log.i(TAG, "JSONException in Hawk")
                        e.printStackTrace()


                    }

                } else {
                    //Case : No Cached Data

                    Log.i(TAG,"With Network , With Cache,No Hawk")
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.title_no_internet)
                    builder.setMessage(R.string.no_internet)
                    builder.setPositiveButton(R.string.disclaimer_ok, null)
                    builder.create().show()
                }
            }

        } else {

            //Offline Check Hawk and use the Cached Data
            if (Hawk.get(GlobalConstants.StoreUserData) as String != null) {

                //Case : Have Cached Data

                Log.i(TAG,"Without Network ,With Hawk")
                try {

                    UserDataList = ArrayList()

                    val response = Hawk.get(GlobalConstants.StoreUserData) as String
                    //array that stores all the object in API Documentation
                    val jsonArray = JSONArray(response)

                    //Loop all the object of the array
                    for (i in 0 until jsonArray.length()) {
                        val userData = jsonArray.getJSONObject(i)


                        //Extrieve what we want by Keys
                        val location = userData.getJSONObject("location")

                        val latitude = location.getDouble("latitude")
                        val longitude = location.getDouble("longitude")

                        val picture = userData.getString("picture")
                        val _id = userData.getString("_id")
                        val name = userData.getString("name")
                        val email = userData.getString("email")

                        UserDataList.add(UserData(latitude, longitude, picture, _id, name, email))
                    }


                    adapter = UserDataAdapter(this, UserDataList)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(this@MainActivity)

                } catch (e: JSONException) {

                    Log.e(TAG, "Unexpected JSON response from  Hawk")

                    Log.i(TAG, "JSONException in Hawk")
                    e.printStackTrace()


                }

            } else {
                //Case : No Cached Data

                Log.i(TAG,"Without Network ,No Hawk")
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.title_no_internet)
                builder.setMessage(R.string.no_internet)
                builder.setPositiveButton(R.string.disclaimer_ok, null)
                builder.create().show()
            }
        }


    }

    private fun retrieve_user_dataAPI() {

        val url = "http://www.json-generator.com/api/json/get/cfdlYqzrfS"
        Log.i(TAG, "Call API")
        val queue = Volley.newRequestQueue(this)

        val stringRequest = object : StringRequest(Request.Method.GET, url,
                { response ->
                    Log.d(TAG, response)

                    try {
                        UserDataList = ArrayList()
                        Log.i(TAG, "SS")
                        //array that stores all the object in API Documentation


                      Hawk.put(GlobalConstants.StoreUserData, response as String)
                        val jsonArray = JSONArray(response)

                        //Loop all the object of the array
                        for (i in 0 until jsonArray.length()) {
                            val userData = jsonArray.getJSONObject(i)


                            //Extrieve what we want by Keys
                            val location = userData.getJSONObject("location")

                            val latitude = location.getDouble("latitude")
                            val longitude = location.getDouble("longitude")

                            val picture = userData.getString("picture")
                            val _id = userData.getString("_id")
                            val name = userData.getString("name")
                            val email = userData.getString("email")

                            UserDataList.add(UserData(latitude, longitude, picture, _id, name, email))
                        }


                        progressDialog.dismiss()

                        GlobalConstants.caching = true
                        adapter = UserDataAdapter(this, UserDataList)

                        recyclerView.setAdapter(adapter)

                        //remark MainActivity.this = "this@ActiivtyName"
                        //also SecondActivity.class = SecondActivity::class.java
                        adapter.setOnItemClickListener(this@MainActivity)


                    } catch (e: JSONException) {
                        progressDialog.dismiss()

                        Log.e(TAG, "Unexpected JSON response from  API")

                        Log.i(TAG, "JSONException")
                        e.printStackTrace()


                    }
                },
                { error ->
                    progressDialog.dismiss()


                    Log.e(TAG, "Login API didn't work with status code: " + (if (error.networkResponse != null) error.networkResponse.statusCode else " null network response") + " " + error.cause)

                    if (error.message != null) {
                        Log.e(TAG, error.message)
                    }

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.system_error)
                    builder.setMessage(R.string.server_communication_error)

                    builder.setPositiveButton(R.string.disclaimer_ok, null)
                    builder.create().show()

                    Log.i(TAG, "error")
                }) {


            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        queue.add(stringRequest)
        progressDialog = ProgressDialog(this)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
        progressDialog.setIcon(null)
        progressDialog.setTitle("")
        progressDialog.setMessage(getString(R.string.cms_progress_dialog_msg))
        progressDialog.show()
    }

    override fun onItemClick(position: Int) {
        GlobalConstants.UserData = UserDataList[position]
        startActivity(Intent(baseContext, DetailUserActivity::class.java))
        finish()

    }

    //TESTING
    fun startFrag(container:Int,fragment:Fragment,fm:FragmentManager){

        val transaction:FragmentTransaction = fm.beginTransaction();
        transaction.replace(container,fragment)
                .addToBackStack(null)
                .commit()

    }


    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null
    }

//    override fun onBackPressed() {
//        // Catch back action and pops from backstack
//        // (if you called previously to addToBackStack() in your transaction)
//        if (fragmentManager.backStackEntryCount > 0) {
//            fragmentManager.popBackStack()
//
//        }
//    }

}
