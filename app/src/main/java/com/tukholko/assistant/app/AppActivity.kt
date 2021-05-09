package com.tukholko.assistant.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.zxing.integration.android.IntentIntegrator
import com.tukholko.assistant.R
import com.tukholko.assistant.app.fragments.Cart
import com.tukholko.assistant.app.fragments.Profile
import com.tukholko.assistant.app.fragments.Right
import com.tukholko.assistant.app.service.NetworkService
import com.tukholko.assistant.app.service.barcode.Capture
import com.tukholko.assistant.auth.LoginActivity
import com.tukholko.assistant.model.Product
import com.tukholko.assistant.model.Shop
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppActivity : AppCompatActivity() {
    // Fragment-related variables
    private val cartFragment = Cart()
    private val profileFragment = Profile()
    private val mapFragment = Right()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = cartFragment

    // Map-related variables
    private var mapView: MapView? = null
    private val MAPKIT_API_KEY = "306a59ee-f6bc-4f75-8b61-90999e65022e"
    private var cameraTarget = CameraPosition(Point(53.900995, 27.552102), 11f, 0f, 0f)
    private var mapFirstTimeInit = true
    private var mapRestart = true
    private var mapObjects: MapObjectCollection? = null

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var btScan: TextView? = null

    lateinit var toolbar: ActionBar

    private var selectedShop : Int? = null

    private fun initializeMap() {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult.contents != null) {
            Toast.makeText(
                    applicationContext,
                    intentResult.contents,
                    Toast.LENGTH_SHORT
            ).show()
            addProductToCart(intentResult.contents)
        }
    }

    fun addProductToCart(productData : String) {
        RecyclerAdapter.addProduct(Product(productData, 1.1, "Belarus", "This is a soup!", 2.4))
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

    private val mapObjectListener =
            MapObjectTapListener { mapObject, point ->
                val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
                val ctx = ContextThemeWrapper(this, R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(ctx).inflate(
                        R.layout.bottom_sheet,
                        findViewById<LinearLayout>(R.id.bottom_sheet)
                )
                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.findViewById<TextView>(R.id.shop_name)?.text = (mapObject.userData as HashMap<*, *>)["Shop name"].toString()
                bottomSheetDialog.findViewById<TextView>(R.id.shop_city_and_country)?.text = (mapObject.userData as HashMap<*, *>)["Shop country and city"].toString()
                bottomSheetDialog.findViewById<TextView>(R.id.shop_address)?.text = (mapObject.userData as HashMap<*, *>)["Shop address"].toString()
                bottomSheetDialog.findViewById<Button>(R.id.button_select_shop)?.setOnClickListener {
                    selectedShop = (mapObject.userData as HashMap<*, *>)["Local ID"].toString().toInt()
                    bottomSheetDialog.dismiss();
                }
                bottomSheetDialog.show()
                true
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        initializeMap()

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_app)

        initializeScanButton()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, mapFragment, "MAP FRAGMENT").hide(mapFragment)
            add(R.id.container, cartFragment, "CART FRAGMENT")
            add(R.id.container, profileFragment, "PROFILE FRAGMENT").hide(profileFragment)
        }.commit()

        addProductToCart("1")
        addProductToCart("2")
        addProductToCart("3")
        addProductToCart("4")
        addProductToCart("5")
        addProductToCart("6")
        addProductToCart("7")
        addProductToCart("8")
        addProductToCart("9")
        addProductToCart("10")
        addProductToCart("11")
    }

    fun initializeMapWithShops() {
        NetworkService.getInstance()
                .shopAPI
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
                        Toast.makeText(
                                applicationContext,
                                t.message,
                                Toast.LENGTH_SHORT
                        ).show()
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
            mark.addTapListener(mapObjectListener)
            val userDataMap = HashMap<String, String>()
            userDataMap["Local ID"] = shopPoint.localShopID.toString()
            userDataMap["Shop name"] = shopPoint.localShopName
            userDataMap["Shop country and city"] = shopPoint.country + ", " + shopPoint.city
            userDataMap["Shop address"] = shopPoint.address
            mark.userData = userDataMap
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_left -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(cartFragment).commit()
                activeFragment = cartFragment
                return@OnNavigationItemSelectedListener true;
            }
            R.id.navigation_central -> {
                fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit()
                activeFragment = profileFragment
                return@OnNavigationItemSelectedListener true;
            }
            R.id.navigation_map -> {
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
                return@OnNavigationItemSelectedListener true;
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

    fun signOut() {
        auth.signOut()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }
}