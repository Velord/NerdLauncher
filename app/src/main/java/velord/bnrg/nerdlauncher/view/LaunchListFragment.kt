package velord.bnrg.nerdlauncher.view

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import velord.bnrg.nerdlauncher.R
import velord.bnrg.nerdlauncher.viewModel.LaunchListViewModel

private const val TAG = "LaunchListFragment"

class LaunchListFragment : Fragment() {

    companion object {
        fun newInstance() = LaunchListFragment()
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LaunchListViewModel::class.java)
    }

    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.launch_list_fragment, container, false).apply {
            initViews(this)
            setupAdapter()
        }
    }

    private fun initViews(view: View) {
        rv = view.findViewById(R.id.app_recycler_view)
        rv.layoutManager = LinearLayoutManager(activity)
    }


    private fun setupAdapter() {
        val startupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val pm = activity!!.packageManager
        val activities = pm.queryIntentActivities(startupIntent, 0)
        activities.sortWith(Comparator { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(pm).toString(),
                b.loadLabel(pm).toString()
            )
        })

        rv.adapter = ActivityAdapter(activities)
        Log.i(TAG, "${activities.size} activities")
    }

    private class ActivityHolder(itemView: View):
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val nameTextView: TextView = itemView.findViewById(R.id.nameApp)
        private val iconImageView: ImageView = itemView.findViewById(R.id.iconApp)
        private lateinit var resolveInfo: ResolveInfo

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val activityInfo = resolveInfo.activityInfo

            val intent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(activityInfo.applicationInfo.packageName,
                    activityInfo.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            p0?.let {
                it.context.apply {
                    startActivity(intent)
                }
            }
        }

        fun bindActivity(resolveInfo: ResolveInfo) {
            this.resolveInfo = resolveInfo
            val pm = itemView.context.packageManager
            //set icon
            GlobalScope.launch {
                setAppIcon(pm)
            }
            //set name
            val appName = resolveInfo.loadLabel(pm).toString()
            nameTextView.text = appName
        }

        private suspend fun setAppIcon(pm: PackageManager) =
            withContext(Dispatchers.Main) {
                resolveInfo.loadIcon(pm).toBitmap().apply {
                    iconImageView.setImageBitmap(this)
                }
            }

    }

    private class ActivityAdapter(val activities: List<ResolveInfo>) :
        RecyclerView.Adapter<ActivityHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(
                R.layout.activity_item, parent, false)

            return ActivityHolder(view)
        }

        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
           activities[position].apply {
               holder.bindActivity(this)
           }
        }

        override fun getItemCount(): Int = activities.size
    }
}
