package velord.bnrg.nerdlauncher.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

val replaceFragment: (FragmentManager, Int, Fragment) -> Unit =
    { fm, layout, fragment ->
        fm.beginTransaction()
            .replace(layout, fragment)
            .addToBackStack(null)
            .commit()
    }

val initFragment: (FragmentManager, Fragment, Int) -> Unit =
    {fm,  fragment, containerId ->
        val currentFragment =
            fm.findFragmentById(containerId)

        if (currentFragment == null)
            addFragment(fm, fragment, containerId)
    }

val addFragment: (FragmentManager, Fragment, Int) -> Unit =
    { fm,  fragment, containerId ->
        fm.beginTransaction()
            .add(containerId, fragment)
            .commit()
    }