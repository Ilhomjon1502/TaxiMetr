package uz.ilhomjon.taximetr.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import uz.ilhomjon.taximetr.utils.MyData
import uz.ilhomjon.taximetr.utils.MyFindLocation

class MyLocationService : Service() {

    lateinit var myFindLocation: MyFindLocation
    override fun onCreate() {
        super.onCreate()
        myFindLocation = MyFindLocation(applicationContext)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myFindLocation.checkSettingsAndStartUpdates()
        startForeground(1, MyData.createNotification(applicationContext, "Taxi ilova: ","ushbu dasturga tezroq kiring"))

        return START_STICKY
    }
}