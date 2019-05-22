package com.dev.pedscn.trackyourspot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_add_spot.*
import java.io.File

class AddSpotActivity : AppCompatActivity() {

    private lateinit var selectedBodySide: String
    private lateinit var selectedBodyPart: String
    private lateinit var fullPhotoPath: String
    private lateinit var spotImageName: String
    private lateinit var editTextWidget: TextInputLayout
    private lateinit var editText: EditText

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.namebar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_info -> {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Naming your spot")
            alertDialog.setMessage(
                "This is the first photo of your spot.\n" +
                        "\n1. If you are happy with the photo, add a name and press \u2713 \n" +
                        "\n2. You can also retake the photo by pressing the back arrow"
            )
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, _ -> dialog.dismiss() }
            alertDialog.show()
            true
        }

        R.id.action_checkmark -> {
            val editName = editText.text.toString()
            if (editName.isBlank()) {
                editTextWidget.error = "Name cannot be blank"
            } else if (editName.length > 19) {
                editTextWidget.error = "Name must be under 20 characters"
            } else if (!editName.matches(Regex(pattern = "^[a-zA-Z0-9 ]+\$"))) {
                editTextWidget.error = "Only letters and numbers allowed"
            } else {
                val processedEditName = editName.replace(" ", "-")
                moveImageFile(spotImageName, processedEditName, selectedBodySide, selectedBodyPart)
                val intent = Intent(this, OldSpotListActivity::class.java)
                intent.putExtra("selectedBodyPart", selectedBodyPart)
                intent.putExtra("selectedBodySide", selectedBodySide)
                startActivity(intent)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_spot)
        setSupportActionBar(add_spot_screen_toolbar as Toolbar)
        title = "Spot Preview"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fullPhotoPath = intent.getStringExtra("fullPhotoPath")
        selectedBodySide = intent.getStringExtra("selectedBodySide")
        selectedBodyPart = intent.getStringExtra("selectedBodyPart")
        spotImageName = intent.getStringExtra("spotImageName")

        Glide.with(this@AddSpotActivity)
            .load(File(fullPhotoPath))
            .apply(RequestOptions().fitCenter())
            .into(spot_image)
        editTextWidget = spot_name_widget
        editText = spot_name_edittext
        editTextWidget.setHintTextAppearance(R.style.CustomHintEnabled)
        editTextWidget.isErrorEnabled = true
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory)
            for (child in fileOrDirectory.listFiles()!!)
                deleteRecursive(child)
        fileOrDirectory.delete()
    }

    //Is the moveImageFile method even needed???? Apart from renaming //Is it dangerous?
    private fun moveImageFile(
        spotImageName: String,
        newSpotName: String,
        selectedBodySide: String,
        selectedBodyPart: String
    ) { //Need better way of dealing with temps
        val photoDirectory = fullPhotoPath.removeSuffix(spotImageName)
        //Move image from temp folder to a new *newSpotName* folder
        val newDirPath =
            photoDirectory.replace("temp", "$selectedBodySide/$selectedBodyPart/$newSpotName")
        val newDir = File(newDirPath)
        //Check if the directory exists already, create it otherwise
        if (!newDir.exists()) newDir.mkdirs()
        //Make app available in the device's Gallery
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(fullPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
            //Rename the file to the new path
            f.renameTo(File(newDirPath + spotImageName))
            //Delete the temporary folder
            val tempFolder = File(photoDirectory)
            deleteRecursive(tempFolder)
        }
    }

    override fun onBackPressed() {
        deletePicAndClose()
    }

    private fun deletePicAndClose() {
        File(fullPhotoPath).delete()
        val intent = Intent(this, OldSpotListActivity::class.java)
        intent.putExtra("selectedBodyPart", selectedBodyPart)
        intent.putExtra("selectedBodySide", selectedBodySide)
        startActivity(intent)
    }
}
