package velord.bnrg.nerdlauncher.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import velord.bnrg.nerdlauncher.R
import velord.bnrg.nerdlauncher.utils.initFragment

private const val TAG = "NerdLauncherActivity"

class NerdLauncherActivity : AppCompatActivity() {

    private val sf = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)

        initLaunchFragment()
    }

    private fun initLaunchFragment() {
        initFragment(sf, LaunchListFragment(), R.id.fragment_container)
    }
}
