package com.example.gestindeproyectos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.gestindeproyectos.auth.AuthActivity
import com.example.gestindeproyectos.databinding.ActivityMainBinding
import com.example.gestindeproyectos.db.DB
import com.example.gestindeproyectos.model.Collaborator
import com.example.gestindeproyectos.model.CollaboratorType
import com.example.gestindeproyectos.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    // Firebase Storage
    private lateinit var storage: FirebaseStorage

    // Current user
    private val currentUser: FirebaseUser
        get() = auth.currentUser!!

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance()

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_collaborators, R.id.nav_projects, R.id.nav_forum, R.id.nav_profile), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        profileViewModel.userProfilePicture.observe(this) {
            val headerView = navView.getHeaderView(0)
            val userPhoto = headerView.findViewById<ImageView>(R.id.user_photo)

            userPhoto.setImageDrawable(it)
        }

        profileViewModel.userName.observe(this) {
            val headerView = navView.getHeaderView(0)
            val userName = headerView.findViewById<TextView>(R.id.user_name)

            userName.text = it
        }

        val headerView = navView.getHeaderView(0)
        val userEmail = headerView.findViewById<TextView>(R.id.user_email)
        userEmail.text = currentUser.email

        checkUserLoggedIn()
        checkUserInDB()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkUserLoggedIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // User is not signed in, redirect to AuthActivity
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User is signed in, update UI
            Snackbar.make(binding.root, "Welcome ${currentUser.email}", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun checkUserInDB() {
        // Check if the user already exists in the database
        // if not send the user to the profile view
        // to complete the registration
        val currentUserEmail = auth.currentUser?.email

        DB.instance.fetchCollaborator(currentUserEmail!!).thenAccept { collaborator ->
            if (collaborator == null) {
                // User is not in the database, redirect to ProfileFragment
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.nav_profile)
            } else {
                Log.d(TAG, "User is in the database")
                if (collaborator.getType() == CollaboratorType.MANAGER) {
                    Log.d(TAG, "User is a manager")
                    // Enable the Collaborators menu item
                    val navView: NavigationView = binding.navView
                    val menu = navView.menu
                    val collaboratorsItem = menu.findItem(R.id.nav_collaborators)
                    collaboratorsItem.isVisible = true

                    // NOTE: There is an issue that when the collaboratorsItem is set to visible
                    // the Collaborators item is highlighted in the menu, but the actual
                    // fragment is the HomeFragment.
                    // This is the temporary (probably permanent haha) fix for this issue.
                    val navController = findNavController(R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.nav_home)
                }
            }
        }
    }

    fun signOut(item: MenuItem) {
        auth.signOut()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
