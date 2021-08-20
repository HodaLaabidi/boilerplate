package com.boilerplate.ui.spalsh

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.boilerplate.R
import com.boilerplate.databinding.ActivityOnBoardingBinding
import com.boilerplate.ui.BaseActivity
import com.boilerplate.ui.auth.AuthViewModel
import com.boilerplate.ui.auth.AuthViewModelFactory
import com.boilerplate.ui.auth.LoginActivity
import com.boilerplate.util.invisible
import com.boilerplate.util.show
import kotlinx.android.synthetic.main.activity_on_boarding.*
import org.kodein.di.generic.instance


class OnBoardingActivity : BaseActivity() {

    private val factory : AuthViewModelFactory by instance()

    var sliderImg: List<Slider>? = arrayListOf(
        Slider(
            R.drawable.ic_launcher_background,"Une plateforme 100%\n" +
                "dédiée à votre beauté" , "Une Appli Mobile pour passer votre devis de chirurgie esthétique en toute sécurité et en quelques clics."),
        Slider(R.drawable.ic_launcher_background,"Un traitement rapide \n" +
                "de votre demande" , "Notre équipe saura étudier rapidement votre demande tout en prenant en considération vos besoins et vos souhaits."),
        Slider(R.drawable.ic_launcher_background,"Une prise en charge \n" +
                "personnalisée" , "Nous sommes à votre disposition pour répondre à toutes vos questions concernant la chirurgie plastique que vous souhaitez faire avec Medespoir."),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityOnBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        val viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        initViewPager()
        btnLoginIn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            } else {

                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }

            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            0 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
    }

    fun initViewPager(){
        var viewPagerAdapter = ViewPagerAdapter(sliderImg!!, this@OnBoardingActivity)
        viewPage.adapter = viewPagerAdapter
        indicator.setupWithViewPager(viewPage);
        //viewPage.autoScroll(3000)
        indicator.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 2){
                    btnLoginIn.show()
                }else btnLoginIn.invisible()
            }
        })
    }


}
