package uz.ilhomjon.taximetr

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.github.florent37.runtimepermission.kotlin.askPermission
import uz.ilhomjon.taximetr.databinding.ActivityMainBinding
import uz.ilhomjon.taximetr.service.MyLocationService
import uz.ilhomjon.taximetr.utils.MyData

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        askPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.FOREGROUND_SERVICE){
            //all permissions already granted or just granted
            binding.btnStart.setOnClickListener {
                MyData.minimalka = binding.edtMinimalka.text.toString().toInt()
                MyData.kutishSumma = binding.edtKutishSumma.text.toString().toInt()
                finish()
                startActivity(Intent(this, TaxiActivity::class.java))
            }
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }



    }
}