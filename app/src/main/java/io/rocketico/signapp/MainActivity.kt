package io.rocketico.signapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mikepenz.materialdrawer.DrawerBuilder
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import io.rocketico.signapp.fragment.MainFragment
import io.rocketico.signapp.fragment.OnFragmentInteractionListener
import io.rocketico.signapp.utils.Cc


class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container,MainFragment.newInstance()).commit();
        DrawerBuilder().withActivity(this).build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
