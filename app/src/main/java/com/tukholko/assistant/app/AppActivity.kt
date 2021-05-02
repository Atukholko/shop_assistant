package com.tukholko.assistant.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.tukholko.assistant.R
import com.tukholko.assistant.app.fragments.Central
import com.tukholko.assistant.app.fragments.Left
import com.tukholko.assistant.app.fragments.Right
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppActivity : AppCompatActivity() {
    private val leftFragment = Left()
    private val centralFragment = Central()
    private val mapFragment = Right()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = centralFragment

    // Map-related variables
    private var mapView: MapView? = null
    private val MAPKIT_API_KEY = "306a59ee-f6bc-4f75-8b61-90999e65022e"
    private var cameraTarget = CameraPosition(Point(53.900995, 27.552102), 11f, 0f, 0f)
    private var mapFirstTimeInit = true
    private var mapRestart = true
    private var mapObjects: MapObjectCollection? = null

    private var btScan: TextView? = null

    lateinit var toolbar: ActionBar

    private fun initializeMap() {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult.contents != null) {
            val toast = Toast.makeText(
                applicationContext,
                intentResult.contents,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun initializeScanButton() {
        btScan = findViewById<TextView>(R.id.button_scan)
        btScan!!.setOnClickListener(View.OnClickListener {
            val intentIntegrator = IntentIntegrator(this@AppActivity)
            intentIntegrator.setOrientationLocked(true)
            intentIntegrator.captureActivity = Capture::class.java
            intentIntegrator.initiateScan()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeMap()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app)

        initializeScanButton()

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, leftFragment, "LEFT FRAGMENT").hide(leftFragment)
            add(R.id.container, mapFragment, "MAP FRAGMENT").hide(mapFragment)
            add(R.id.container, centralFragment, "CENTRAL NAHUI")
        }.commit()
    }

    private fun initializeMapWithShops() {
        NetworkService.getInstance()
            .jsonApi
            .shops
            .enqueue(object : Callback<List<Shop?>?> {
                override fun onResponse(
                    call: Call<List<Shop?>?>,
                    response: Response<List<Shop?>?>
                ) {
                    val shops = response.body()
                    markShops(shops as List<Shop>)
                }

                override fun onFailure(call: Call<List<Shop?>?>, t: Throwable) {
                    val toast = Toast.makeText(
                        applicationContext,
                        t.message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            })
    }

    private fun markShops(shops: List<Shop>) {
        for (shopPoint in shops) {
            val mark = mapObjects!!.addPlacemark(Point(shopPoint.latitude, shopPoint.longitude))
            mark.opacity = 1f
            val `is` = IconStyle()
            `is`.scale = 1.1f
            mark.setIcon(ImageProvider.fromResource(this, R.drawable.shop), `is`)
            mark.isDraggable = false
            mark.userData = shopPoint.localShopID.toString() + ", " + shopPoint.localShopName
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_left -> {
                toolbar.title = "Shopping cart"
                fragmentManager.beginTransaction().hide(activeFragment).show(leftFragment).commit()
                activeFragment = leftFragment
            }
            R.id.navigation_central -> {
                toolbar.title = "Profile page"
                fragmentManager.beginTransaction().hide(activeFragment).show(centralFragment)
                    .commit()
                activeFragment = centralFragment
            }
            R.id.navigation_map -> {
                toolbar.title = "Shop map"
                fragmentManager.beginTransaction().hide(activeFragment).show(mapFragment).commit()
                if (mapFirstTimeInit) {
                    mapView = findViewById<MapView>(R.id.map_view)
                    mapView!!.map.move(cameraTarget)
                    mapObjects = mapView!!.map.mapObjects.addCollection()
                    initializeMapWithShops()
                    mapFirstTimeInit = false
                }
                if (mapRestart) {
                    onMapOpen()
                    mapRestart = false
                }
                activeFragment = mapFragment
            }
        }
        false
    }

    override fun onStart() {
        super.onStart()
        mapRestart = true
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        if (mapView != null) {
            mapView!!.onStop()
        }
        MapKitFactory.getInstance().onStop()
    }

    private fun onMapOpen() {
        mapView!!.onStart()
    }

}