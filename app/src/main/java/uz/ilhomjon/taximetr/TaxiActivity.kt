package uz.ilhomjon.taximetr

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import uz.ilhomjon.taximetr.databinding.ActivityTaxiBinding
import uz.ilhomjon.taximetr.service.MyLocationService
import uz.ilhomjon.taximetr.utils.MyData
import java.text.SimpleDateFormat
import java.util.Date

class TaxiActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTaxiBinding.inflate(layoutInflater) }
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(runnable, 1000)

        ContextCompat.startForegroundService(this, Intent(this, MyLocationService::class.java))

        binding.apply {

            MyData.locationLiveData.observe(this@TaxiActivity){
                tvLocation.text = "${it.latitude}, ${it.longitude}"
            }
            MyData.umumiyKmLiveData.observe(this@TaxiActivity){
                var s = (it).toString().substring(0, it.toString().indexOf('.')+2).toDouble()
                tvUmmumiySumma.text = "${s*MyData.kmSumm} som"
                tvKm.text = "${s} km"
            }

            MyData.tezlikLiveData.observe(this@TaxiActivity){
//                var t = it.toString().substring(0, it.toString().indexOf('.')+2).toDouble()
                tvTezlik.text = "${it*3.6} km/h"
            }

            btnWait.setBackgroundColor(Color.GREEN)
            btnWait.setOnClickListener {
                if (MyData.isWait){
                    MyData.isWait = false
                    btnWait.text = "Kutishni boshlash"
                    btnWait.setBackgroundColor(Color.GREEN)
                }else{
                    MyData.isWait = true
                    binding.btnWait.text = "Kutishni to'xtatish"
                    MyData.kutishVaqti = 0
                    btnWait.setBackgroundColor(Color.RED)
                }
            }

            btnFinish.setOnClickListener {
                stopService(Intent(this@TaxiActivity, MyLocationService::class.java))
            }
        }
    }

    private val runnable = object : Runnable{
        override fun run() {
            binding.tvTime.text = SimpleDateFormat("HH:mm:ss").format(Date())
            handler.postDelayed(this, 1000)
        }
    }
}